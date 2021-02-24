package com.github.casside.pac4jx.clients.pac4j.wechatwork;

/**
 * tnd
 *
 * 企业微信非标准oauth协议，code只能单独维护一下了
 *
 * 注意：code是一次性的
 */
public class WeChatWorkCodeOnceHolder {

    private static ThreadLocal<String> codes = new ThreadLocal<>();

    /**
     * used in cn.bmsk.cas.pac4j.oauth.qywx.QyWxAuthenticator#retrieveAccessToken(org.pac4j.core.context.WebContext,
     * org.pac4j.oauth.credentials.OAuthCredentials)
     */
    public static void set(String code) {
        codes.set(code);
    }

    /**
     * return and remove
     *
     * @return code
     */
    public static String release() {
        return codes.get();
    }

}
