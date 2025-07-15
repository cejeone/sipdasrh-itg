package com.sipdasrh.awas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Service Awas.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final ExternalGis externalGis = new ExternalGis();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }
    public ExternalGis getExternalGis() { return externalGis; }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class ExternalGis {
        private String userName;
        private String password;
        private String urlToken;
        private String urlService;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUrlToken() {
            return urlToken;
        }

        public void setUrlToken(String urlToken) {
            this.urlToken = urlToken;
        }

        public String getUrlService() {
            return urlService;
        }

        public void setUrlService(String urlService) {
            this.urlService = urlService;
        }
    }
    // jhipster-needle-application-properties-property-class
}
