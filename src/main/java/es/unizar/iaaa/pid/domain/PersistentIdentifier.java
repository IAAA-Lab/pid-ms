package es.unizar.iaaa.pid.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity PersistentIdentifier
 */
@ApiModel(description = "Entity PersistentIdentifier")
@Entity
@Table(name = "persistent_identifier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersistentIdentifier implements Serializable {

    // TODO move to app properties
    private static final String BASE = "urn:inspire:ES:";
    private static final String SEPARATOR = "/";

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    // This field is derived
    @NotNull
    @Column(name = "external_urn", nullable = false)
    private String externalUrn;

    @NotNull
    @ManyToOne(optional=false)
    private Feature feature;

    @Column(name = "resolver_proxy_mode")
    private Boolean resolverProxyMode;

    @Embedded
    private Identifier identifier;

    @Embedded
    private Registration registration;

    @Embedded
    private Resource resource;

    public PersistentIdentifier externalUrn(String externalUrn) {
        this.externalUrn = externalUrn;
        return this;
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalUrn() {
        return externalUrn;
    }

    public void setExternalUrn(String externalUrn) {
        this.externalUrn = externalUrn;
    }

    public Feature getFeature() {
        return feature;
    }

    public PersistentIdentifier feature(Feature feature) {
        this.feature = feature;
        return this;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Boolean isResolverProxyMode() {
        return resolverProxyMode;
    }

    public PersistentIdentifier resolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
        return this;
    }

    public void setResolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public PersistentIdentifier identifier(Identifier identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }


    public Resource getResource() {
        return resource;
    }

    public PersistentIdentifier resource(Resource resource) {
        this.resource = resource;
        return this;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Registration getRegistration() {
        return registration;
    }

    public PersistentIdentifier registration(Registration registration) {
        this.registration = registration;
        return this;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersistentIdentifier persistentIdentifier = (PersistentIdentifier) o;
        if (persistentIdentifier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), persistentIdentifier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersistentIdentifier{" +
            "id=" + getId() +
            ", externalUrn='" + getExternalUrn() + "'" +
            ", feature='" + getFeature() + "'" +
            ", resolverProxyMode='" + isResolverProxyMode() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", resource='" + getResource() + "'" +
            ", registration='" + getRegistration() + "'" +
            "}";
    }

    private String computeExternalUrnFromIdentifier() {
        return computeExternalUrnFromIdentifier(identifier);
    }

    public static String computeExternalUrnFromIdentifier(Identifier identifier) {
        return BASE +
            SEPARATOR +
            computeShortFromIdentifier(identifier);
    }

    public String shortId() {
        return computeShortFromIdentifier(this.getIdentifier());
    }

    public static String computeShortFromIdentifier(Identifier identifier) {
        StringBuilder sb = new StringBuilder();
        sb.append(identifier.getNamespace());
        sb.append(SEPARATOR);
        sb.append(identifier.getLocalId());
        if (identifier.getVersionId() != null) {
            sb.append(SEPARATOR);
            sb.append(identifier.getVersionId());
        }
        return sb.toString();
    }

    private UUID computeIdFromExternal() {
        return UUID.nameUUIDFromBytes(getExternalUrn().getBytes());
    }

    static public UUID computeSurrogateFromIdentifier(Identifier identifier) {
        return computeIdFromExternal(computeExternalUrnFromIdentifier(identifier));
    }

    private static UUID computeIdFromExternal(String external) {
        return UUID.nameUUIDFromBytes(external.getBytes());
    }

    public void autoId() {
        String externalUrn = computeExternalUrnFromIdentifier(identifier);
        UUID id = UUID.nameUUIDFromBytes(externalUrn.getBytes());
        setExternalUrn(externalUrn);
        setId(id);
    }
}
