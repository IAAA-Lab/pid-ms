package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.domain.enumeration.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Fixtures {
    public static Namespace namespace() {
        Source source = new Source();
        source.setSourceType(SourceType.WFS);
        source.setEndpointLocation("http://www.ign.es/wfs-inspire/ngbe");
        source.setSchemaUri("http://inspire.ec.europa.eu/schemas/gn/4.0");
        source.setSchemaPrefix("gn");
        source.setFeatureType("NamedPlace");
        source.setGeometryProperty("geometry");
        source.setFeaturesThreshold(10000);

        Instant now = Instant.now();
        Registration registration = new Registration();
        registration.setRegistrationDate(now);
        registration.setNextRenewalDate(now.plus(1, ChronoUnit.DAYS));
        registration.setLastChangeDate(now);
        registration.setItemStatus(ItemStatus.ISSUED);
        registration.setProcessStatus(ProcessStatus.NONE);

        Namespace namespace = new Namespace();
        namespace.setNamespace("ES.IGN.NGBE");
        namespace.setPublicNamespace(false);
        namespace.setSource(source);
        namespace.setRegistration(registration);
        namespace.setRenewalPolicy(RenewalPolicy.ONE_MINUTE);

        return namespace;
    }

    public static Task task(Namespace namespace) {
        Task task = new Task();
        task.setStatus(TaskStatus.IN_QUEUE);
        task.setType(ProcessStatus.HARVEST);
        task.setTimestamp(Instant.now());
        task.setNamespace(namespace);
        return task;
    }

    public static Identifier identifier() {
        return new Identifier()
            .namespace("namespace")
            .localId("localId")
            .versionId("versionId");
    }

    public static Change change(Identifier identifier, Task task) {
        Change change = new Change();
        change.setIdentifier(identifier);
        change.setTask(task);
        change.setChangeTimestamp(Instant.now());
        change.setResource(new Resource().resourceType(ResourceType.SPATIAL_OBJECT).locator("http://example.com"));
        return change;
    }
}
