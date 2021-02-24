package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import com.github.casside.pac4jx.clients.pac4j.Pac4jXProperties.WeChatWork;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的企业微信Client构造器，帮你快速实例化一个client
 */
@Slf4j
public class WeChatWorkClientBuilder {

    /**
     * 企业微信profile service
     */
    private static UserProfileService newUserProfileService() {
        return new UserProfileService();
    }

    private static WeChatWorkProfileDefinition newWeChatWorkProfileDefinition() {
        WeChatWorkProfileDefinition weChatWorkProfileDefinition = new WeChatWorkProfileDefinition();
        weChatWorkProfileDefinition.setUserService(newUserProfileService());
        return weChatWorkProfileDefinition;
    }

    public static WeChatWorkClient buildWeChatWorkClient(String callbackUrl, WeChatWork weChatWorkProp) {
        final WeChatWorkClient client = new WeChatWorkClient(weChatWorkProp.getCorpId(), weChatWorkProp.getSecret(),
                                                             newWeChatWorkProfileDefinition());
        client.setCallbackUrl(callbackUrl);
        client.setName(weChatWorkProp.getPac4jId());
        client.setProfileAttrs(weChatWorkProp.getProfileAttrs());
        Map<String, String> customParams = Optional.ofNullable(client.getCustomParams()).orElse(new HashMap<>());
        customParams.put("agentid", weChatWorkProp.getAgentId());
        client.setCustomParams(customParams);

        log.debug("Created client [{}] with identifier [{}]", client.getName(), client.getKey());
        return client;
    }


}
