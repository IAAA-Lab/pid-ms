package es.unizar.iaaa.pid.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Entity Feature")
@Entity
@Table(name = "feature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Feature {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
	
	@ManyToOne
	private Namespace namespace;
	
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

    @NotNull
    @Column(name = "feature_type", nullable = false)
    private String featureType;

    @Column(name = "geometry_property")
    private String geometryProperty;

    @Column(name = "begin_lifespan_version_property")
    private String beginLifespanVersionProperty;

    @Column(name = "features_threshold")
    private Integer featuresThreshold;
    
    @Column(name = "hits_request")
    private Boolean hitsRequest;

    @Column(name = "factor_k")
    private Integer factorK;

    @Column(name = "xpath")
    private String xpath;

    @Column(name = "name_item")
    private String nameItem;

    @Embedded
    private BoundingBox boundingBox;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSrsName() {
		return srsName;
	}

	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}
	
	public Feature srsName(String srsName){
		this.srsName = srsName;
		return this;
	}

	public String getSchemaUri() {
		return schemaUri;
	}

	public void setSchemaUri(String schemaUri) {
		this.schemaUri = schemaUri;
	}
	
	public Feature schemaUri(String schemaUri) {
		this.schemaUri = schemaUri;
		return this;
	}

	public String getSchemaUriGML() {
		return schemaUriGML;
	}

	public void setSchemaUriGML(String schemaUriGML) {
		this.schemaUriGML = schemaUriGML;
	}
	
	public Feature schemaUriGML(String schemaUriGML) {
		this.schemaUriGML = schemaUriGML;
		return this;
	}

	public String getSchemaUriBase() {
		return schemaUriBase;
	}

	public void setSchemaUriBase(String schemaUriBase) {
		this.schemaUriBase = schemaUriBase;
	}
	
	public Feature schemaUriBase(String schemaUriBase) {
		this.schemaUriBase = schemaUriBase;
		return this;
	}

	public String getSchemaPrefix() {
		return schemaPrefix;
	}

	public void setSchemaPrefix(String schemaPrefix) {
		this.schemaPrefix = schemaPrefix;
	}
	
	public Feature schemaPrefix(String schemaPrefix) {
		this.schemaPrefix = schemaPrefix;
		return this;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	
	public Feature featureType(String featureType) {
		this.featureType = featureType;
		return this;
	}

	public String getGeometryProperty() {
		return geometryProperty;
	}

	public void setGeometryProperty(String geometryProperty) {
		this.geometryProperty = geometryProperty;
	}
	
	public Feature geometryProperty(String geometryProperty) {
		this.geometryProperty = geometryProperty;
		return this;
	}

	public String getBeginLifespanVersionProperty() {
		return beginLifespanVersionProperty;
	}

	public void setBeginLifespanVersionProperty(String beginLifespanVersionProperty) {
		this.beginLifespanVersionProperty = beginLifespanVersionProperty;
	}
	
	public Feature beginLifespanVersionProperty(String beginLifespanVersionProperty) {
		this.beginLifespanVersionProperty = beginLifespanVersionProperty;
		return this;
	}

	public Integer getFeaturesThreshold() {
		return featuresThreshold;
	}

	public void setFeaturesThreshold(Integer featuresThreshold) {
		this.featuresThreshold = featuresThreshold;
	}
	
	public Feature featuresThreshold(Integer featuresThreshold) {
		this.featuresThreshold = featuresThreshold;
		return this;
	}

	public Boolean getHitsRequest() {
		return hitsRequest;
	}

	public void setHitsRequest(Boolean hitsRequest) {
		this.hitsRequest = hitsRequest;
	}
	
	public Feature hitsRequest(boolean hitsRequest){
		this.hitsRequest = hitsRequest;
		return this;
	}

	public Integer getFactorK() {
		return factorK;
	}

	public void setFactorK(Integer factorK) {
		this.factorK = factorK;
	}
	
	public Feature factorK(Integer factorK) {
		this.factorK = factorK;
		return this;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	
	public Feature xpath(String xpath) {
		this.xpath = xpath;
		return this;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}
	
	public Feature nameItem(String nameItem) {
		this.nameItem = nameItem;
		return this;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	public Feature boundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
		return this;
	}
	
	public String getQualifiedFeatureType() {
        return schemaPrefix + ":" + featureType;
    }

    public String getQualifiedGeometryProperty() {
        return schemaPrefix + ":" + geometryProperty;
    }
    
    public Namespace getNamespace() {
		return namespace;
	}

	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}
	
	public Feature namespace(Namespace namespace){
		this.namespace = namespace;
		return this;
	}

	@Override
    public String toString() {
        return "Feature{" +
            ", srsName='" + getSrsName() + "'" +
            ", schemaUri='" + getSchemaUri() + "'" +
            ", schemaUriGML='" + getSchemaUriGML() + "'" +
            ", schemaUriBase='" + getSchemaUriBase() + "'" +
            ", schemaPrefix='" + getSchemaPrefix() + "'" +
            ", featureType='" + getFeatureType() + "'" +
            ", geometryProperty='" + getGeometryProperty() + "'" +
            ", beginLifespanVersionProperty='" + getBeginLifespanVersionProperty() + "'" +
            ", featuresThreshold='" + getFeaturesThreshold() + "'" +
            ", factorK='" + getFactorK() + "'" +
            ", xpath='" + getXpath() + "'" +
            ", nameItem='" + getNameItem() + "'" +
            ", boundingBox='" + getBoundingBox() + "'" +
            "}";
    }

}
