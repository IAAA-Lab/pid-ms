package es.unizar.iaaa.pid.cucumber.stepdefs;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static es.unizar.iaaa.pid.domain.enumeration.ResourceType.DATASET;
import static es.unizar.iaaa.pid.domain.enumeration.ResourceType.SPATIAL_OBJECT;

public class FixturesConstructorPid {
    public static PersistentIdentifier creaConjunto(String code) {
        Instant now = Instant.now();
        Instant renew = now.plus(1, ChronoUnit.DAYS);

        Identifier identifier = new Identifier()
            .defaultNamespace()
            .localId(code);
        Registration registration = new Registration();
        registration.setItemStatus(ItemStatus.ISSUED);
        registration.setProcessStatus(ProcessStatus.NONE);
        registration.setLastChangeDate(now);
        registration.setRegistrationDate(now);
        registration.setNextRenewalDate(renew);
        Resource resource = new Resource().resourceType(DATASET).locator("");

        return new PersistentIdentifier()
            .identifier(identifier)
            .resource(resource)
            .registration(registration)
            .resolverProxyMode(false);
    }

    public static PersistentIdentifier creaObjetoEspacial(String namespace, String localId, @SuppressWarnings("SameParameterValue") String gmlId) {
        Instant now = Instant.now();
        Instant renew = now.plus(1, ChronoUnit.DAYS);

        Identifier identifier = new Identifier()
            .namespace(namespace)
            .localId(localId)
            .alternateId(gmlId);

        Registration registration = new Registration();
        registration.setItemStatus(ItemStatus.ISSUED);
        registration.setProcessStatus(ProcessStatus.NONE);
        registration.setLastChangeDate(now);
        registration.setRegistrationDate(now);
        registration.setNextRenewalDate(renew);
        Resource resource = new Resource()
            .resourceType(SPATIAL_OBJECT)
            .locator("");

        return new PersistentIdentifier()
            .identifier(identifier)
            .resource(resource)
            .registration(registration)
            .resolverProxyMode(false);
    }

    public static PersistentIdentifier creaObjetoEspacialVersionado(String namespace, String localId, String versionId, @SuppressWarnings("SameParameterValue") String gmlId) {
        Instant now = Instant.now();
        Instant renew = now.plus(1, ChronoUnit.DAYS);

        Identifier identifier = new Identifier()
            .namespace(namespace)
            .localId(localId)
            .versionId(versionId)
            .alternateId(gmlId);

        Registration registration = new Registration();
        registration.setItemStatus(ItemStatus.ISSUED);
        registration.setProcessStatus(ProcessStatus.NONE);
        registration.setLastChangeDate(now);
        registration.setRegistrationDate(now);
        registration.setNextRenewalDate(renew);

        Resource resource = new Resource()
            .resourceType(SPATIAL_OBJECT)
            .locator("");

        return new PersistentIdentifier()
            .identifier(identifier)
            .resource(resource)
            .registration(registration)
            .resolverProxyMode(false);
    }
}
