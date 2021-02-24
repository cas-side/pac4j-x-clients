package com.github.casside.pac4jx.clients.pac4j.wechatwork;

import java.util.Collections;
import java.util.Map;

/**
 * 用来扩展企业微信Client，拿到企业微信用户ID后，想额外查询一些用户信息，都可以通过它来实现
 */
public class UserProfileService {

    /**
     * @param weChatWorkUserId 企业微信 UserId
     * @return 企业微信 user profile
     */
    public Map<String, Object> get(String weChatWorkUserId) {
        return Collections.emptyMap();
    }
}
