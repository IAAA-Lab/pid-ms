package es.unizar.iaaa.pid.harvester.connectors.wfs;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ximpleware.AutoPilot;
import com.ximpleware.NodeRecorder;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.Resource;
import es.unizar.iaaa.pid.domain.Source;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
import es.unizar.iaaa.pid.harvester.connectors.ValidatorById;
import es.unizar.iaaa.pid.harvester.connectors.wfs.WFSResponse.ResponseStatus;
import es.unizar.iaaa.pid.service.ChangeService;

@Component
@Scope("prototype")
public class WFSValidatorById implements ValidatorById {
    private static final Logger LOGGER = LoggerFactory.getLogger(WFSValidatorById.class);

    private Task task;

    private Source source;

    private final ChangeService changeService;

    @Autowired
    public WFSValidatorById(ChangeService changeService) {
        this.changeService = changeService;
    }

    @Override
    public void setTask(Task task) {
        this.task = task;
        this.source = task.getNamespace().getSource();
    }

    @Override
    public int validateGmlId(Feature feature, Identifier identifier) {
        String request = WFSClient.createWfsGetFeatureById(source, identifier);
        WFSResponse response = WFSClient.executeRequestGET(request);
        if (response.getResponseStatus() == ResponseStatus.FAIL || response.getResponseStatus() == ResponseStatus.TIMEOUT) {
            log("{} causes ERROR", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
            return -1;
        }

        VTDGen document = response.getDocument();
        VTDNav nav = document.getNav();
        AutoPilot ap = new AutoPilot(nav);
        ap.selectElementNS(feature.getSchemaUri(), feature.getFeatureType());

        Identifier remoteIdentifier = null;
        try {
            if (!ap.iterate()) {
                log("{} is NOT_FOUND", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
                response(ChangeAction.NOT_FOUND, identifier,feature);
                return -1;
            }
            nav.push();
            NodeRecorder member = new NodeRecorder(nav);
            member.record();

            AutoPilot apx = new AutoPilot(nav);
            apx.selectElementNS("http://inspire.ec.europa.eu/schemas/base/3.3", "localId");
            String localId = null;
            if (apx.iterate()) {
                localId = nav.toNormalizedString(nav.getText());
            }
            member.resetPointer();
            member.iterate();

            apx = new AutoPilot(nav);
            apx.selectElementNS("http://inspire.ec.europa.eu/schemas/base/3.3", "namespace");
            String namespace = null;
            if (apx.iterate()) {
                namespace = nav.toNormalizedString(nav.getText());
            }
            member.resetPointer();
            member.iterate();

            apx = new AutoPilot(nav);
            apx.selectElementNS("http://inspire.ec.europa.eu/schemas/base/3.3", "versionId");
            String versionId = null;
            if (apx.iterate()) {
                versionId = nav.toNormalizedString(nav.getText());
            }
            member.resetPointer();
            member.iterate();

            apx = new AutoPilot(nav);
            apx.selectElementNS(feature.getSchemaUri(), feature.getBeginLifespanVersionProperty());
            Instant beginLifespanVersion = null;
            if (apx.iterate()) {
                beginLifespanVersion = Instant.parse(nav.toNormalizedString(nav.getText()));
            }
            member.resetPointer();
            member.iterate();

            apx = new AutoPilot(nav);
            apx.selectElementNS(feature.getSchemaUri(), feature.getFeatureType());
            String gmlId = null;
            if (apx.iterate()) {
                int gmlidx  = nav.getAttrValNS("http://www.opengis.net/gml/3.2", "id");
                if (gmlidx != -1) {
                    gmlId = nav.toNormalizedString(gmlidx);
                }
            }

            nav.pop();
            remoteIdentifier = new Identifier()
                .namespace(namespace)
                .localId(localId)
                .versionId(versionId)
                .beginLifespanVersion(beginLifespanVersion)
                .alternateId(gmlId);
        } catch (Exception e) {
            e.printStackTrace();
            response(ChangeAction.NOT_FOUND, identifier, feature);
            return -1;
        }


        if (identifier.sameAs(remoteIdentifier)) {
            if (remoteIdentifier.getEndLifespanVersion() != null) {
                log("{} is CANCELLED", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
                response(ChangeAction.CANCELLED, remoteIdentifier, feature);
            } else {
                log("{} is UNCHANGED", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
                response(ChangeAction.UNCHANGED, identifier, feature);
            }
        } else {
            log("{} is INCONSISTENT", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
            response(ChangeAction.INCONSISTENT, identifier, feature);
        }
        return 1;
    }

    private void response(ChangeAction action, Identifier identifier, Feature feature) {
        Resource resource = new Resource();
        resource.setResourceType(ResourceType.SPATIAL_OBJECT);
        resource.setLocator(WFSClient.createWfsGetFeatureById(source, identifier));

        Change change = new Change();
        change.setIdentifier(identifier);
        change.setResource(resource);
        change.setTask(this.task);
        change.setAction(action);
        change.setFeature(feature);
        change.setChangeTimestamp(Instant.now());
        changeService.createChange(change);

    }

    public Instant parse(String formattedDate) {
        return Instant.parse(formattedDate+"Z");
    }

    private DateTime extractDateTime(Element element, String ns, String name) {
        String s = extractContent(element, ns, name);
        return s != null ? DateTime.parse(s) : null;
    }

    private String extractContent(Element element, String ns, String name) {
        NodeList nl = element.getElementsByTagNameNS(ns, name);
        return nl.getLength() > 0 ? nl.item(0).getTextContent(): null;
    }

    private String extractAttribute(Element element,String nsAttribute, String nameAttribute) {
        Node n = element.getAttributes().getNamedItemNS(nsAttribute,nameAttribute);
        return n != null ? n.getNodeValue() : null;
    }

    void log(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }
}
