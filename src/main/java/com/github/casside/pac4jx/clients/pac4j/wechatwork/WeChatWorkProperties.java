//package com.github.casside.pac4jx.clients.pac4j.wechatwork;
//
//import java.util.HashMap;
//import java.util.Map;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.FieldDefaults;
//
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Getter
//@Setter
//public class WeChatWorkProperties {
//
//    public WeChatWorkProperties() {
//        this.setCustomParams(new HashMap<>());
//    }
//
//    private String id;
//
//    /**
//     * The client secret.
//     */
//    private String secret;
//
//    private String authUrl;
//
//    /**
//     * Token endpoint of the provider.
//     */
//    private String tokenUrl;
//
//    /**
//     * Profile endpoint of the provider.
//     */
//    private String profileUrl;
//
//    /**
//     * Profile path portion of the profile endpoint of the provider.
//     */
//    private String profilePath;
//
//    /**
//     * Http method to use when asking for profile.
//     */
//    private String profileVerb = "POST";
//
//    /**
//     * Profile attributes to request and collect in form of key-value pairs.
//     */
//    private Map<String, String> profileAttrs;
//
//    /**
//     * Custsom parameters in form of key-value pairs sent along in authZ requests, etc.
//     */
//    private Map<String, String> customParams;
//
//    /**
//     * agentid
//     */
//    public void setAgentid(String agentid) {
//        this.getCustomParams().put("agentid", agentid);
//    }
//
//
//}
