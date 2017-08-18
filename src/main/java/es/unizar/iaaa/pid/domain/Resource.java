package es.unizar.iaaa.pid.domain;

import es.unizar.iaaa.pid.domain.enumeration.ResourceType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Resource {

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private ResourceType resourceType;

    @Column(name = "locator")
    private String locator;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Resource resourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getLocator() {
        return locator;
    }

    public Resource locator(String locator) {
        this.locator = locator;
        return this;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    @Override
    public String toString() {
        return "Resource{" +
            ", resourceType='" + getResourceType() + "'" +
            ", locator='" + getLocator() + "'" +
            "}";
    }}
