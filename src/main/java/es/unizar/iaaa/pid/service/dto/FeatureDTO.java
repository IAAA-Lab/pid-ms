package es.unizar.iaaa.pid.service.dto;

import javax.validation.constraints.NotNull;

public class FeatureDTO {

	private Long id;

    private String srsName;

    private String schemaUri;

    private String schemaUriGML;

    private String schemaUriBase;

    private String schemaPrefix;

    @NotNull
    private String featureType;

    private String geometryProperty;

    private String beginLifespanVersionProperty;

    private Integer featuresThreshold;

    private Boolean hitsRequest;

    private Integer factorK;

    private String xpath;

    private String nameItem;

    private Double minX;

    private Double minY;

    private Double maxX;

    private Double maxY;

    private Long namespaceId;

    private String namespaceName;

    public Long getId(){
    	return id;
    }

    public void setId(Long id){
    	this.id = id;
    }

	public String getSrsName() {
		return srsName;
	}

	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}

	public String getSchemaUri() {
		return schemaUri;
	}

	public void setSchemaUri(String schemaUri) {
		this.schemaUri = schemaUri;
	}

	public String getSchemaUriGML() {
		return schemaUriGML;
	}

	public void setSchemaUriGML(String schemaUriGML) {
		this.schemaUriGML = schemaUriGML;
	}

	public String getSchemaUriBase() {
		return schemaUriBase;
	}

	public void setSchemaUriBase(String schemaUriBase) {
		this.schemaUriBase = schemaUriBase;
	}

	public String getSchemaPrefix() {
		return schemaPrefix;
	}

	public void setSchemaPrefix(String schemaPrefix) {
		this.schemaPrefix = schemaPrefix;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getGeometryProperty() {
		return geometryProperty;
	}

	public void setGeometryProperty(String geometryProperty) {
		this.geometryProperty = geometryProperty;
	}

	public String getBeginLifespanVersionProperty() {
		return beginLifespanVersionProperty;
	}

	public void setBeginLifespanVersionProperty(String beginLifespanVersionProperty) {
		this.beginLifespanVersionProperty = beginLifespanVersionProperty;
	}

	public Integer getFeaturesThreshold() {
		return featuresThreshold;
	}

	public void setFeaturesThreshold(Integer featuresThreshold) {
		this.featuresThreshold = featuresThreshold;
	}

	public Boolean getHitsRequest() {
		return hitsRequest;
	}

	public void setHitsRequest(Boolean hitsRequest) {
		this.hitsRequest = hitsRequest;
	}

	public Integer getFactorK() {
		return factorK;
	}

	public void setFactorK(Integer factorK) {
		this.factorK = factorK;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

	public Double getMinX() {
		return minX;
	}

	public void setMinX(Double minX) {
		this.minX = minX;
	}

	public Double getMinY() {
		return minY;
	}

	public void setMinY(Double minY) {
		this.minY = minY;
	}

	public Double getMaxX() {
		return maxX;
	}

	public void setMaxX(Double maxX) {
		this.maxX = maxX;
	}

	public Double getMaxY() {
		return maxY;
	}

	public void setMaxY(Double maxY) {
		this.maxY = maxY;
	}

	public Long getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Long namespaceId) {
		this.namespaceId = namespaceId;
	}

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

	@Override
	public String toString() {
		return "FeatureDTO [srsName=" + srsName + ", schemaUri=" + schemaUri + ", schemaUriGML=" + schemaUriGML
				+ ", schemaUriBase=" + schemaUriBase + ", schemaPrefix=" + schemaPrefix + ", featureType=" + featureType
				+ ", geometryProperty=" + geometryProperty + ", beginLifespanVersionProperty="
				+ beginLifespanVersionProperty + ", featuresThreshold=" + featuresThreshold + ", hitsRequest="
				+ hitsRequest + ", factorK=" + factorK + ", xpath=" + xpath + ", nameItem=" + nameItem + ", minX="
				+ minX + ", minY=" + minY + ", maxX=" + maxX + ", maxY=" + maxY + ", namespaceId=" + namespaceId +
                  ", namespaceName=" + namespaceName +"]";
	}

}
