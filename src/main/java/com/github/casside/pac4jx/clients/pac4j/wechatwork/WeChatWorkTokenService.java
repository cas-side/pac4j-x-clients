package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import lombok.Setter;

/**
 * 允许开发自定义 token 管理器 by {@link #getAccessTokenSupplier}
 */
public class WeChatWorkTokenService extends SimpleWeChatWorkService {

    @Setter
    private AccessTokenSupplier getAccessTokenSupplier;

    public WeChatWorkTokenService(DefaultApi20 api, String apiKey, String apiSecret, String callback,
                                  String scope, String state, String responseType, String userAgent,
                                  HttpClientConfig httpClientConfig,
                                  HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    @Override
    public OAuth2AccessToken getAccessToken(String code) throws IOException, InterruptedException, ExecutionException {
        if (getAccessTokenSupplier == null) {
            return super.getAccessToken(code);
        }
        return getAccessTokenSupplier.get(getApiKey(), getApiSecret(), code);
    }

}
