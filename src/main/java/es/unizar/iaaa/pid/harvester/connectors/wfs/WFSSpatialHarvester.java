package es.unizar.iaaa.pid.harvester.connectors.wfs;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.NodeRecorder;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.Resource;
import es.unizar.iaaa.pid.domain.Source;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import es.unizar.iaaa.pid.domain.enumeration.MethodType;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
import es.unizar.iaaa.pid.harvester.connectors.SpatialHarvester;
import es.unizar.iaaa.pid.harvester.connectors.wfs.WFSResponse.ResponseStatus;
import es.unizar.iaaa.pid.service.ChangeService;

@Component
@Scope("prototype")
public class WFSSpatialHarvester implements SpatialHarvester {
    private static final Logger LOGGER = LoggerFactory.getLogger(WFSSpatialHarvester.class);
    private final ChangeService changeService;
    private Task task;
    private Source source;

    @Autowired
    public WFSSpatialHarvester(ChangeService changeService) {
        this.changeService = changeService;
    }

    @Override
    public void setTask(Task task) {
        this.task = task;
        this.source = task.getNamespace().getSource();
    }

    @Override
    public int getHitsTotal(String feature,BoundingBox boundingBox) {
    	String request;
    	WFSResponse response;

    	if(source.getMethodType() == MethodType.POST){
	        request = WFSClient.createWfsGetFeatureRequestBodyPost(feature, source, boundingBox, "hits");
	        response = WFSClient.executeRequestPOST(source.getEndpointLocation(), request);
    	}
    	else{
    		request = WFSClient.createWfsGetFeatureRequestGet(feature, source, boundingBox, "hits");
    		response = WFSClient.executeRequestGET(request);
    	}

    	if(response.getResponseStatus() == ResponseStatus.FAIL){
    		return -1;
    	}
    	else if(response.getResponseStatus() == ResponseStatus.TIMEOUT){
    		return -2;
    	}
        
        VTDGen document = response.getDocument();
        VTDNav nav = document.getNav();
        Integer numResults = getNumberMatched(nav);
        return numResults == null ? -1 : numResults;
    }

    private Integer getNumberMatched(VTDNav nav) {
        AutoPilot ap = new AutoPilot();
        ap.bind(nav);
        String numResultString;
        try {
            ap.selectXPath("@numberMatched");
            int idx = ap.evalXPath();
            if (idx == -1) {
                return -1;
            }
            idx = nav.getAttrVal("numberMatched");
            numResultString = nav.toNormalizedString(idx);
            if ("unknown".equals(numResultString)) {
                return -1;
            }
            return Integer.parseInt(numResultString);
        } catch (NavException | XPathParseException | XPathEvalException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Optional<Integer> getNumberReturned(VTDNav nav) {
        AutoPilot ap = new AutoPilot();
        ap.bind(nav);
        String numResultString;
        try {
            ap.selectXPath("@numberReturned");
            int idx = ap.evalXPath();
            if (idx == -1) {
                return Optional.of(-1);
            }
            idx = nav.getAttrVal("numberReturned");
            numResultString = nav.toNormalizedString(idx);
            if ("unknown".equals(numResultString)) {
                return Optional.of(-1);
            }
            return Optional.of(Integer.parseInt(numResultString));
        } catch (NavException | XPathParseException | XPathEvalException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public int extractIdentifiers(String feature, BoundingBox boundingBox) {
    	String request;
    	WFSResponse response;

    	if(source.getMethodType() == MethodType.POST){
	        request = WFSClient.createWfsGetFeatureRequestBodyPost(feature, source, boundingBox, "results");
	        response = WFSClient.executeRequestPOST(source.getEndpointLocation(), request);
    	}
    	else{
    		request = WFSClient.createWfsGetFeatureRequestGet(feature, source, boundingBox, "results");
	        response = WFSClient.executeRequestGET(request);
    	}

    	if (response.getResponseStatus() == ResponseStatus.FAIL){
            return -1;
        }
        else if(response.getResponseStatus() == ResponseStatus.TIMEOUT){
        	return -2;
        }
        else if(response.getSrc().startsWith("<error>") || response.getSrc().contains("Runtime Error") 
        		|| response.getSrc().contains("ows:Exception")){
        	return -1;
        }

        //ñappa para hacer que funcione el catastro
        if(response.getSrc().contains("No se han encontrado inmuebles")){
        	return 0;
        }

        VTDGen document = response.getDocument();
        VTDNav nav = document.getNav();

        Optional<Integer> returned = getNumberReturned(nav);
        int valid = 0;
        int invalid = 0;
        int ignored = 0;
        log("{} are expected to be retrieved", returned);
        try {
            AutoPilot ap = new AutoPilot(nav);
            ap.declareXPathNameSpace("ns1", source.getXpath());
            ap.selectXPath("//ns1:"+source.getNameItem());
            while (ap.evalXPath() != -1) {
                try {
                    Identifier identifier = extractIdentifier(feature,nav);
                    Resource resource = new Resource();
                    resource.setResourceType(ResourceType.SPATIAL_OBJECT);
                    resource.setLocator(WFSClient.createWfsGetFeatureById(source, identifier));

                    Change change = new Change();
                    change.setIdentifier(identifier);
                    change.setResource(resource);
                    change.setTask(this.task);
                    change.setAction(ChangeAction.ISSUED);
                    change.setChangeTimestamp(Instant.now());
                    change.setFeature(feature);
                    changeService.createChange(change);
                    debug("{} is FOUND ", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
                    valid++;
                } catch (FailedExtractionException e) {
                    if (e.canSkip()) {
                        log("Failed extraction exception: {} (ignored)", e.getMessage());
                        ignored++;
                    } else {
                        error("Failed extraction exception: {}", e.getMessage());
                        invalid++;
                    }
                }
            }
        } catch (NavException | XPathParseException | XPathEvalException e) {
            error("Check response\n{}", response.getSrc(), e);
        }
        log("{} were retrieved, {} were ignored and {} were invalid", valid, ignored, invalid);
        if (invalid > 0 || !returned.isPresent()) {
        	warning("Check request\n{}", request);
        } else {
            final int totalValid = valid;
            returned.ifPresent(r -> {
                    if (r > 0 && r != totalValid) {
                        warning("Check request\n{}", request);
                    }
                }
            );
        }
        return valid;
    }

    private Identifier extractIdentifier(String feature, VTDNav nav) throws NavException, FailedExtractionException {

        nav.push();
        NodeRecorder member = new NodeRecorder(nav);
        member.record();

        String xlinkHref = null;
        int hrefidx = nav.getAttrValNS("http://www.w3.org/1999/xlink", "href");
        if (hrefidx != -1) {
            xlinkHref = nav.toNormalizedString(hrefidx);
        }

        member.resetPointer();
        member.iterate();

        AutoPilot apx1 = new AutoPilot(nav);
        apx1.selectElementNS(source.getSchemaUri(), feature);
        String gmlId = null;
        boolean isCorrectFeature = false;

        if(apx1.iterate()){
        	isCorrectFeature = true;
        	int gmlidx = nav.getAttrValNS(source.getSchemaUriGML(), "id");
            if (gmlidx != -1) {
                gmlId = nav.toNormalizedString(gmlidx);
            }
        }

        member.resetPointer();
        member.iterate();

        // int featureTypePos = nav.getAttrValNS(source.getSchemaUri(), source.getFeatureType());
        // if(featureTypePos != 1){;}

        AutoPilot apx = new AutoPilot(nav);
        apx.selectElementNS(source.getSchemaUriBase(), "localId");
        String localId = null;
        if (apx.iterate()) {
        	int pos = nav.getText();
        	if(pos != -1){
        		localId = nav.toNormalizedString(pos);
        	}
        }
        member.resetPointer();
        member.iterate();

        apx = new AutoPilot(nav);
        apx.selectElementNS(source.getSchemaUriBase(), "namespace");
        String namespace = null;
        if (apx.iterate()) {
        	int pos = nav.getText();
        	if(pos != -1){
        		 namespace = nav.toNormalizedString(pos);
        	}
        }
        member.resetPointer();
        member.iterate();

        apx = new AutoPilot(nav);
        apx.selectElementNS(source.getSchemaUriBase(), "versionId");
        String versionId = null;
        if (apx.iterate()) {
        	int pos = nav.getText();
        	if(pos != -1){
        		versionId = nav.toNormalizedString(pos);
        	}
        }
        member.resetPointer();
        member.iterate();

        apx = new AutoPilot(nav);
        apx.selectElementNS(source.getSchemaUri(), source.getBeginLifespanVersionProperty());
        Instant beginLifespanVersion = null;
        if (apx.iterate()) {
        	int pos = nav.getText();
        	if(pos != -1){
                beginLifespanVersion = parse(nav.toNormalizedString(pos));
        	}
        }

        nav.pop();

        if (xlinkHref != null) {
            throw new FailedExtractionException("Xlink found in member", true);
        }

        if(!isCorrectFeature){
        	throw new FailedExtractionException("It is not a " + feature + " feature", true);
        }

        if (gmlId == null) {
            throw new FailedExtractionException("Failed to extract a valid gmlId", false);
        }

        if (namespace == null) {
            throw new FailedExtractionException("Failed to extract a valid namespace", false);
        }

        if (localId == null) {
            throw new FailedExtractionException("Failed to extract a valid localId", false);
        }

        return new Identifier()
            .namespace(namespace)
            .localId(localId)
            .versionId(versionId)
            .beginLifespanVersion(beginLifespanVersion)
            .alternateId(gmlId);
    }

    public Instant parse(String formattedDate) {
        return Instant.parse(formattedDate+"Z");
    }

    private void log(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    private void error(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.error("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    private void debug(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.debug("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    private void warning(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.warn("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

//    private DateTime extractDateTime(Element element, String ns, String name) {
//        String s = extractContent(element, ns, name);
//        return s != null ? DateTime.parse(s) : null;
//    }

    private String extractContent(Element element, String ns, String name) {
        NodeList nl = element.getElementsByTagNameNS(ns, name);
        return nl.getLength() > 0 ? nl.item(0).getTextContent() : null;
    }

//    private String extractAttribute(Element element, String nsElement, String nameElement, String nsAttribute, String nameAttribute) {
//        NodeList nl = element.getElementsByTagNameNS(nsElement, nameElement);
//        if (nl.getLength() == 0) {
//            return null;
//        }
//        Node n = nl.item(0).getAttributes().getNamedItemNS(nsAttribute, nameAttribute);
//        return n != null ? n.getNodeValue() : null;
//    }

    private class FailedExtractionException extends Exception {
        private boolean skip;

        FailedExtractionException(String cause, boolean skip) {
            super(cause);
            this.skip = skip;
        }

        boolean canSkip() {
            return skip;
        }

    }
}
