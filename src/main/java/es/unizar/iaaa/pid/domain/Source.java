package es.unizar.iaaa.pid.domain;

import es.unizar.iaaa.pid.domain.enumeration.MethodType;
import es.unizar.iaaa.pid.domain.enumeration.SourceType;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Embeddable
public class Source {

    @Enumerated(EnumType.STRING)
    @Column(name = "method_type")
    private MethodType methodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private SourceType sourceType;

    @Column(name = "endpoint_location")
    private String endpointLocation;

    @Column(name = "srs_name")
    private String srsName;

    @Column(name = "schema_uri")
    private String schemaUri;

    @Column(name = "schema_uri_gml")
    private String schemaUriGML;

    @Column(name = "schema_uri_base")
    private String schemaUriBase;

    @Column(name = "schema_prefix")
    private String schemaPrefix;

    @Column(name = "feature_type")
    private String featureType;

    @Column(name = "geometry_property")
    private String geometryProperty;

    @Column(name = "begin_lifespan_version_property")
    private String beginLifespanVersionProperty;

    @Column(name = "features_threshold")
    private Integer featuresThreshold;

    @Column(name = "resolver_proxy_mode")
    private Boolean resolverProxyMode;

    @Column(name = "hits_request")
    private Boolean hitsRequest;

    @Column(name = "factor_k")
    private Integer factorK;

    @Column(name = "xpath")
    private String xpath;

    @Column(name = "name_item")
    private String nameItem;

    @Min(value = 0)
    @Column(name = "max_num_request")
    private Integer maxNumRequest;

    @Embedded
    private BoundingBox boundingBox;

    public MethodType getMethodType() {
        return methodType;
    }

    public Source methodType(MethodType methodType) {
        this.methodType = methodType;
        return this;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public Source sourceType(SourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getEndpointLocation() {
        return endpointLocation;
    }

    public Source endpointLocation(String endpointLocation) {
        this.endpointLocation = endpointLocation;
        return this;
    }

    public void setEndpointLocation(String endpointLocation) {
        this.endpointLocation = endpointLocation;
    }

    public String getSrsName() {
        return srsName;
    }

    public Source srsName(String srsName) {
        this.srsName = srsName;
        return this;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    public String getSchemaUri() {
        return schemaUri;
    }

    public Source schemaUri(String schemaUri) {
        this.schemaUri = schemaUri;
        return this;
    }

    public void setSchemaUri(String schemaUri) {
        this.schemaUri = schemaUri;
    }

    public String getSchemaUriGML() {
        return schemaUriGML;
    }

    public Source schemaUriGML(String schemaUriGML) {
        this.schemaUriGML = schemaUriGML;
        return this;
    }

    public void setSchemaUriGML(String schemaUriGML) {
        this.schemaUriGML = schemaUriGML;
    }

    public String getSchemaUriBase() {
        return schemaUriBase;
    }

    public Source schemaUriBase(String schemaUriBase) {
        this.schemaUriBase = schemaUriBase;
        return this;
    }

    public void setSchemaUriBase(String schemaUriBase) {
        this.schemaUriBase = schemaUriBase;
    }

    public String getSchemaPrefix() {
        return schemaPrefix;
    }

    public Source schemaPrefix(String schemaPrefix) {
        this.schemaPrefix = schemaPrefix;
        return this;
    }

    public void setSchemaPrefix(String schemaPrefix) {
        this.schemaPrefix = schemaPrefix;
    }

    public String getFeatureType() {
        return featureType;
    }

    public Source featureType(String featureType) {
        this.featureType = featureType;
        return this;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getGeometryProperty() {
        return geometryProperty;
    }

    public Source geometryProperty(String geometryProperty) {
        this.geometryProperty = geometryProperty;
        return this;
    }

    public void setGeometryProperty(String geometryProperty) {
        this.geometryProperty = geometryProperty;
    }

    public String getBeginLifespanVersionProperty() {
        return beginLifespanVersionProperty;
    }

    public Source beginLifespanVersionProperty(String beginLifespanVersionProperty) {
        this.beginLifespanVersionProperty = beginLifespanVersionProperty;
        return this;
    }

    public void setBeginLifespanVersionProperty(String beginLifespanVersionProperty) {
        this.beginLifespanVersionProperty = beginLifespanVersionProperty;
    }

    public Integer getFeaturesThreshold() {
        return featuresThreshold;
    }

    public Source featuresThreshold(Integer featuresThreshold) {
        this.featuresThreshold = featuresThreshold;
        return this;
    }

    public void setFeaturesThreshold(Integer featuresThreshold) {
        this.featuresThreshold = featuresThreshold;
    }

    public Boolean isResolverProxyMode() {
        return resolverProxyMode;
    }

    public Source resolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
        return this;
    }

    public void setResolverProxyMode(Boolean resolverProxyMode) {
        this.resolverProxyMode = resolverProxyMode;
    }

    public Boolean isHitsRequest() {
        return hitsRequest;
    }

    public Source hitsRequest(Boolean hitsRequest) {
        this.hitsRequest = hitsRequest;
        return this;
    }

    public void setHitsRequest(Boolean hitsRequest) {
        this.hitsRequest = hitsRequest;
    }

    public Integer getFactorK() {
        return factorK;
    }

    public Source factorK(Integer factorK) {
        this.factorK = factorK;
        return this;
    }

    public void setFactorK(Integer factorK) {
        this.factorK = factorK;
    }

    public String getXpath() {
        return xpath;
    }

    public Source xpath(String xpath) {
        this.xpath = xpath;
        return this;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getNameItem() {
        return nameItem;
    }

    public Source nameItem(String nameItem) {
        this.nameItem = nameItem;
        return this;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public Integer getMaxNumRequest() {
        return maxNumRequest;
    }

    public Source maxNumRequest(Integer maxNumRequest) {
        this.maxNumRequest = maxNumRequest;
        return this;
    }

    public void setMaxNumRequest(Integer maxNumRequest) {
        this.maxNumRequest = maxNumRequest;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Source boundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
        return this;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getQualifiedFeatureType() {
        return schemaPrefix + ":" + featureType;
    }

    public String getQualifiedGeometryProperty() {
        return schemaPrefix + ":" + geometryProperty;
    }

    @Override
    public String toString() {
        return "Source{" +
            "methodType='" + getMethodType() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", endpointLocation='" + getEndpointLocation() + "'" +
            ", srsName='" + getSrsName() + "'" +
            ", schemaUri='" + getSchemaUri() + "'" +
            ", schemaUriGML='" + getSchemaUriGML() + "'" +
            ", schemaUriBase='" + getSchemaUriBase() + "'" +
            ", schemaPrefix='" + getSchemaPrefix() + "'" +
            ", featureType='" + getFeatureType() + "'" +
            ", geometryProperty='" + getGeometryProperty() + "'" +
            ", beginLifespanVersionProperty='" + getBeginLifespanVersionProperty() + "'" +
            ", featuresThreshold='" + getFeaturesThreshold() + "'" +
            ", resolverProxyMode='" + isResolverProxyMode() + "'" +
            ", hitsRequest='" + isHitsRequest() + "'" +
            ", factorK='" + getFactorK() + "'" +
            ", xpath='" + getXpath() + "'" +
            ", nameItem='" + getNameItem() + "'" +
            ", maxNumRequest='" + getMaxNumRequest() + "'" +
            ", boundingBox='" + getBoundingBox() + "'" +
            "}";
    }}
