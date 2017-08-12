package es.unizar.iaaa.pid.domain;

import es.unizar.iaaa.pid.domain.enumeration.NamespaceStatus;
import es.unizar.iaaa.pid.domain.enumeration.RenewalPolicy;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entity Namespace
 */
@ApiModel(description = "Entity Namespace")
@Entity
@Table(name = "namespace")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Namespace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "namespace", nullable = false)
    private String namespace;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "public_namespace", nullable = false)
    private Boolean publicNamespace;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "renewal_policy", nullable = false)
    private RenewalPolicy renewalPolicy;

    @Enumerated(EnumType.STRING)
    @Column(name = "namespace_status")
    private NamespaceStatus namespaceStatus;

    @Embedded
    private Registration registration;

    @Embedded
    private Source source;

    @ManyToOne
    private Organization owner;

    @Version
    @Column(name = "version_lock", nullable = false)
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public Namespace namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTitle() {
        return title;
    }

    public Namespace title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isPublicNamespace() {
        return publicNamespace;
    }

    public Namespace publicNamespace(Boolean publicNamespace) {
        this.publicNamespace = publicNamespace;
        return this;
    }

    public void setPublicNamespace(Boolean publicNamespace) {
        this.publicNamespace = publicNamespace;
    }

    public RenewalPolicy getRenewalPolicy() {
        return renewalPolicy;
    }

    public Namespace renewalPolicy(RenewalPolicy renewalPolicy) {
        this.renewalPolicy = renewalPolicy;
        return this;
    }

    public void setRenewalPolicy(RenewalPolicy renewalPolicy) {
        this.renewalPolicy = renewalPolicy;
    }

    public NamespaceStatus getNamespaceStatus() {
        return namespaceStatus;
    }

    public Namespace namespaceStatus(NamespaceStatus namespaceStatus) {
        this.namespaceStatus = namespaceStatus;
        return this;
    }

    public void setNamespaceStatus(NamespaceStatus namespaceStatus) {
        this.namespaceStatus = namespaceStatus;
    }

    public Registration getRegistration() {
        return registration;
    }

    public Namespace registration(Registration registration) {
        this.registration = registration;
        return this;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Source getSource() {
        return source;
    }

    public Namespace source(Source source) {
        this.source = source;
        return this;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Organization getOwner() {
        return owner;
    }

    public Namespace owner(Organization organization) {
        this.owner = organization;
        return this;
    }

    public void setOwner(Organization organization) {
        this.owner = organization;
    }


    public Integer getVersion() {
        return version;
    }

    public Namespace version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Namespace namespace = (Namespace) o;
        if (namespace.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), namespace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Namespace{" +
            "id=" + getId() +
            ", namespace='" + getNamespace() + "'" +
            ", title='" + getTitle() + "'" +
            ", publicNamespace='" + isPublicNamespace() + "'" +
            ", renewalPolicy='" + getRenewalPolicy() + "'" +
            ", namespaceStatus='" + getNamespaceStatus() + "'" +
            ", registration='" + getRegistration() + "'" +
            ", source='" + getSource() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
