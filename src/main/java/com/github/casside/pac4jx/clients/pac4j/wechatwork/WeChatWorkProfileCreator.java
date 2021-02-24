package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.creator.OAuth20ProfileCreator;

/**
 *
 */
public class WeChatWorkProfileCreator extends OAuth20ProfileCreator<OAuth20Profile> {

    public WeChatWorkProfileCreator(OAuth20Configuration configuration, IndirectClient client) {
        super(configuration, client);
    }

    @Override
    protected void signRequest(final OAuth20Service service, final OAuth2AccessToken accessToken,
                               final OAuthRequest request) {
        service.signRequest(accessToken, request);
        if (this.configuration.isTokenAsHeader()) {
            request.addHeader(HttpConstants.AUTHORIZATION_HEADER, HttpConstants.BEARER_HEADER_PREFIX + accessToken.getAccessToken());
        }

        // 企业微信里，用户ID 由 授权码code 和 AccessToken 来获取的
        request.addParameter("code", WeChatWorkCodeOnceHolder.release());
    }

//    /**
//     * 通过 token 获取 user profile， 然后根据 userid 从数据库查询关联的用户数据
//     */
//    @Override
//    protected OAuth20Profile retrieveUserProfileFromToken(WebContext context, OAuth2AccessToken accessToken) {
//        return super.retrieveUserProfileFromToken(context, accessToken);
//    }
}
