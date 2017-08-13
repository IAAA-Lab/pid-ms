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

    private Resolver resolver;

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public Resolver getResolver() {
        return resolver;
    }
}
