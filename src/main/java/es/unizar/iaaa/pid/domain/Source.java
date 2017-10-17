package es.unizar.iaaa.pid.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;

import es.unizar.iaaa.pid.domain.enumeration.MethodType;
import es.unizar.iaaa.pid.domain.enumeration.SourceType;

@Embeddable
public class Source {

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private SourceType sourceType;

    @Column(name = "endpoint_location")
    private String endpointLocation;

	@Enumerated(EnumType.STRING)
	@Column(name = "method_type")
	private MethodType methodType;
	
    @Min(value = 0)
    @Column(name = "max_num_request")
    private Integer maxNumRequest;
    
    @Column(name = "resolver_proxy_mode")
    private Boolean resolverProxyMode;
    
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
    
    public Source methodType(MethodType methodType){
    	this.methodType = methodType;
    	return this;
    }
    
    public MethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}
	
	public Integer getMaxNumRequest() {
		return maxNumRequest;
	}

	public void setMaxNumRequest(Integer maxNumRequest) {
		this.maxNumRequest = maxNumRequest;
	}
	
	public Source maxNumRequest(Integer maxNumRequest) {
		this.maxNumRequest = maxNumRequest;
		return this;
	}

    @Override
    public String toString() {
        return "Source{" +
            "sourceType='" + getSourceType() + "'" +
            ", endpointLocation='" + getEndpointLocation() + "'" +
            ", maxNumRequest='" + getMaxNumRequest() + "'" +
            ", methodType='" + getMethodType() + "'" +
            ", resolverProxyMode='" + isResolverProxyMode() + "'" +
            "}";
    }
}
