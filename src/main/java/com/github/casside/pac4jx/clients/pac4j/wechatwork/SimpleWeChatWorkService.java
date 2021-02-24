package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.validation.constraints.Null;

/**
 * 作用：<br/>
 *
 * - 构建 access_token 请求 <br/>
 *
 * - 维护 access_token <br/>
 *
 * - 构建 refresh_token 请求 <br/>
 *
 * - 请求签名 <br/>
 *
 * - 发送请求 <br/>
 *
 * 当前service并没有维护 accessToken，想自己维护token的话， see {@link WeChatWorkTokenService}
 */
public class SimpleWeChatWorkService extends OAuth20Service {

    public SimpleWeChatWorkService(DefaultApi20 api, String apiKey, String apiSecret, String callback, String scope,
                                   String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
                                   HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    @Override
    public String getVersion() {
        return "2.0";
    }

    /**
     * 重写获取accessToken方法，使用 accessToken 管理器来实现
     *
     * @param code 企业微信获取accessToken不需要code，只需要id、secret，所以这里写死 null
     */
    @Override
    public OAuth2AccessToken getAccessToken(String code) throws IOException, InterruptedException, ExecutionException {
        return super.getAccessToken(null);
    }

    @Override
    protected OAuthRequest createAccessTokenRequest(@Null String code) {
        DefaultApi20       api     = getApi();
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        request.addParameter(OAuthConstants.REDIRECT_URI, getCallback());
        request.addParameter("corpid", getApiKey());
        request.addParameter("corpsecret", getApiSecret());
        final String scope = getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        }
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
        return request;
    }
}
