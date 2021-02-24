package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import com.alibaba.fastjson.JSONObject;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Response;
import java.io.IOException;

/**
 * 企业微信accessToken 解析器
 */
public class WeChatWorkTokenExtractor implements TokenExtractor<OAuth2AccessToken> {

    private WeChatWorkTokenExtractor() {
    }

    final static WeChatWorkTokenExtractor INSTANCE = new WeChatWorkTokenExtractor();

    @Override
    public OAuth2AccessToken extract(Response response) throws IOException, OAuthException {
        String     body    = response.getBody();
        JSONObject json    = JSONObject.parseObject(body);
        int        errcode = json.getInteger("errcode");
        if (errcode != 0) {
            throw new OAuthException(String.format("get access_token fail, body={}", body));
        }
        String accessToken = json.getString("access_token");
        int    expiresIn   = json.getInteger("expires_in");

        return new OAuth2AccessToken(accessToken, null, expiresIn, null, null, body);
    }
}
