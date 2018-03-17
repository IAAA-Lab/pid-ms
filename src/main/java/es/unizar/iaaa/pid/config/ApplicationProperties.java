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
    	private int maxNumErrors;
    	private int maxNumTimeouts;

		public int getMaxNumErrors() {
			return maxNumErrors;
		}
		public void setMaxNumErrors(int maxNumErrors) {
			this.maxNumErrors = maxNumErrors;
		}
		public int getMaxNumTimeouts() {
			return maxNumTimeouts;
		}
		public void setMaxNumTimeouts(int maxNumTimeouts) {
			this.maxNumTimeouts = maxNumTimeouts;
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
