package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import com.github.casside.pac4jx.clients.pac4j.OAuth20CredentialsCodeExtractor;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.pac4j.oauth.client.OAuth20Client;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.credentials.authenticator.OAuth20Authenticator;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.redirect.OAuth20RedirectActionBuilder;

/**
 * 企业微信 Client
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class WeChatWorkClient extends OAuth20Client<OAuth20Profile> {

    WeChatWorkApi weChatWorkApi;
    /**
     * 相当于主键ID
     */
    private String profileId;
    /**
     * attribute release
     */
    Map<String, String> profileAttrs;
    /**
     * 跳转到授权URL的时候需要加的自定义参数
     */
    Map<String, String> customParams;

    public WeChatWorkClient(final String key, final String secret, final WeChatWorkProfileDefinition weChatWorkProfileDefinition) {

        this.setConfiguration(new OAuth20Configuration());
        this.setKey(key);
        this.setSecret(secret);
        this.configuration.setProfileDefinition(weChatWorkProfileDefinition);
        this.profileId = weChatWorkProfileDefinition.getProfileId();
    }

    /**
     * 有点懒加载的意思，可以放到构造函数
     */
    @Override
    protected void clientInit() {
        if (weChatWorkApi == null) {
            weChatWorkApi = new WeChatWorkApi();
        }
        configuration.setApi(weChatWorkApi);
        configuration.setCustomParams(this.getCustomParams());

        WeChatWorkProfileDefinition profileDefinition = (WeChatWorkProfileDefinition) this.configuration.getProfileDefinition();
        profileDefinition.setProfileId(profileId);
        this.profileAttrs.forEach(profileDefinition::addProfileAttribute);

        defaultRedirectActionBuilder(new OAuth20RedirectActionBuilder(configuration, this));
        defaultCredentialsExtractor(new OAuth20CredentialsCodeExtractor(configuration, this));
        defaultAuthenticator(new OAuth20Authenticator(configuration, this));
        defaultProfileCreator(new WeChatWorkProfileCreator(configuration, this));

        super.clientInit();
    }

}
