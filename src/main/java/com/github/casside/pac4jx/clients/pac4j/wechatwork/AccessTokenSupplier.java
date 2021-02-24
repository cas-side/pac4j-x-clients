package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import com.github.scribejava.core.model.OAuth2AccessToken;

public interface AccessTokenSupplier {

    public OAuth2AccessToken get(String appId, String appSecret, String token);

}
