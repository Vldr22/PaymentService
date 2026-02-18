package org.resume.paymentservice.contants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstants {

    public static final String BLACKLIST_PREFIX = "blacklist:";
    public static final String COOKIE_NAME = "auth_token";
    public static final String CLAIM_ROLE = "role";

    public static final String[] PUBLIC_PATHS = {
            "/api/auth/",
            "/api/webhooks/",
            "/swagger-ui",
            "/v3/api-docs"
    };

}
