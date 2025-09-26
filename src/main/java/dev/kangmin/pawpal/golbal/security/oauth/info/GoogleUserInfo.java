package dev.kangmin.pawpal.golbal.security.oauth.info;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProviderName() {
        return (String) attributes.get("name");
    }

}
