package es.unizar.iaaa.pid.service.dto;


import es.unizar.iaaa.pid.domain.enumeration.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Namespace entity.
 */
public class NamespaceDTO implements Serializable {

    private Long id;

    @NotNull
    private String namespace;

    private String title;

    @NotNull
    private Boolean publicNamespace;

    @NotNull
    private RenewalPolicy renewalPolicy;

    private NamespaceStatus namespaceStatus;

    private ProcessStatus processStatus;

    private ItemStatus itemStatus;

    private Instant lastChangeDate;

    private Instant registrationDate;

    private Instant lastRevisionDate;

    private Instant nextRenewalDate;

    private Instant annullationDate;

    private MethodType methodType;

    private SourceType sourceType;

    private String endpointLocation;

    private Boolean resolverProxyMode;

    @Min(value = 0)
    private Integer maxNumRequest;

    private Long ownerId;

    private String ownerTitle;

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

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isPublicNamespace() {
        return publicNamespace;
    }

    public void setPublicNamespace(Boolean publicNamespace) {
        this.publicNamespace = publicNamespace;
    }

    public RenewalPolicy getRenewalPolicy() {
        return renewalPolicy;
    }

    public void setRenewalPolicy(RenewalPolicy renewalPolicy) {
        this.renewalPolicy = renewalPolicy;
    }

    public NamespaceStatus getNamespaceStatus() {
        return namespaceStatus;
    }

    public void setNamespaceStatus(NamespaceStatus namespaceStatus) {
        this.namespaceStatus = namespaceStatus;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Instant getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Instant getLastRevisionDate() {
        return lastRevisionDate;
    }

    public void setLastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    public Instant getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public Instant getAnnullationDate() {
        return annullationDate;
    }

    public void setAnnullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getEndpointLocation() {
        return endpointLocation;
    }

    public void setEndpointLocation(String endpointLocation) {
        this.endpointLocation = endpointLocation;
    }

    public Boolean isResolverProxyMode() {
        return resolverProxyMode;
    }

    public void setResolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
    }

    public Integer getMaxNumRequest() {
        return maxNumRequest;
    }

    public void setMaxNumRequest(Integer maxNumRequest) {
        this.maxNumRequest = maxNumRequest;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerTitle() {
        return ownerTitle;
    }

    public void setOwnerTitle(String ownerTitle) {
        this.ownerTitle = ownerTitle;
    }

    public Integer getVersion() {
        return version;
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

        NamespaceDTO namespaceDTO = (NamespaceDTO) o;
        if(namespaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), namespaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NamespaceDTO{" +
            "id=" + getId() +
            ", namespace='" + getNamespace() + "'" +
            ", title='" + getTitle() + "'" +
            ", publicNamespace='" + isPublicNamespace() + "'" +
            ", renewalPolicy='" + getRenewalPolicy() + "'" +
            ", namespaceStatus='" + getNamespaceStatus() + "'" +
            ", processStatus='" + getProcessStatus() + "'" +
            ", itemStatus='" + getItemStatus() + "'" +
            ", lastChangeDate='" + getLastChangeDate() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", lastRevisionDate='" + getLastRevisionDate() + "'" +
            ", nextRenewalDate='" + getNextRenewalDate() + "'" +
            ", annullationDate='" + getAnnullationDate() + "'" +
            ", methodType='" + getMethodType() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", endpointLocation='" + getEndpointLocation() + "'" +
            ", resolverProxyMode='" + isResolverProxyMode() + "'" +
            ", maxNumRequest='" + getMaxNumRequest() + "'" +
            ", version='" + getVersion() + "'" +
            ", ownerId='" + getOwnerId() + "'" +
            ", ownerTitle='" + getOwnerTitle() + "'" +
            "}";
    }
}
