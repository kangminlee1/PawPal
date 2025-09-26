package dev.kangmin.pawpal.golbal.security.oauth.info;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getProviderEmail();
    String getProviderName();
}
