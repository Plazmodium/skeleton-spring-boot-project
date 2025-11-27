package rest.skeleton.spring.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    /** Enable security for modifying endpoints. */
    private boolean enabled = false;

    private Jwt jwt = new Jwt();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public static class Jwt {
        /** Toggle JWT filter stub. */
        private boolean enabled = false;
        /** For skeleton: accept any token when true. */
        private boolean acceptAnyToken = false;
        /** Base64-encoded secret for HMAC signing (HS256). Dev/test default only. */
        private String secret = "c3ByaW5nLXJlc3Qtc2tlbGV0b24tZGVmYXVsdC1qb3Qtc2VjcmV0LWFzLWJhc2U2NA=="; // "spring-rest-skeleton-default-jot-secret-as-base64"
        /** Token issuer string. */
        private String issuer = "rest-skeleton";
        /** Expiration in seconds for issued tokens. */
        private long expirationSeconds = 3600;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isAcceptAnyToken() {
            return acceptAnyToken;
        }

        public void setAcceptAnyToken(boolean acceptAnyToken) {
            this.acceptAnyToken = acceptAnyToken;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public long getExpirationSeconds() {
            return expirationSeconds;
        }

        public void setExpirationSeconds(long expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
        }
    }
}
