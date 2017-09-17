package es.unizar.iaaa.pid.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public static class Resolver {
        private String metadataBase;

        public void setMetadataBase(String metadataBase) {
            this.metadataBase = metadataBase;
        }

        public String getMetadataBase() {
            return metadataBase;
        }

    }

    public static class Harvester {
    	private int MAX_NUM_ERRORS;
    	private int MAX_NUM_TIMEOUTS;
    	
		public int getMAX_NUM_ERRORS() {
			return MAX_NUM_ERRORS;
		}
		public void setMAX_NUM_ERRORS(int mAX_NUM_ERRORS) {
			MAX_NUM_ERRORS = mAX_NUM_ERRORS;
		}
		public int getMAX_NUM_TIMEOUTS() {
			return MAX_NUM_TIMEOUTS;
		}
		public void setMAX_NUM_TIMEOUTS(int mAX_NUM_TIMEOUTS) {
			MAX_NUM_TIMEOUTS = mAX_NUM_TIMEOUTS;
		}
    }
    
    private Resolver resolver;

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public Resolver getResolver() {
        return resolver;
    }
    
    private Harvester harvester;
    
    public void setHarvester(Harvester harvester){
    	this.harvester = harvester;
    }
    
    public Harvester getHarvester(){
    	return harvester;
    }
    
}
