package com.github.casside.pac4jx.clients.pac4j;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ConfigurationProperties(prefix = "pac4j-x")
public class Pac4jXProperties {


    String host;
    /**
     * 回调URL
     */
    String callbackUrl;

    ClientProperties client;

    /**
     * 当前服务的配置
     */
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class ClientProperties {

        List<OIDCProperties> oidcs;

        List<WeChatWork> weChatWorks;
    }

    /**
     * 服务基础配置
     */
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static abstract class ServerBasicProperties {

        /**
         * 用来唯一标识服务
         */
        @NotNull
        String pac4jId;
    }

    /**
     * OIDC Protocol
     */
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class OIDCProperties extends ServerBasicProperties {

        String              serverHost;
        String              clientId;
        String              secret;
        /**
         * 发现URI
         */
        String              discoveryURI;
        String              logoutUrl;
        String              scope;
        /**
         * 使用的签名算法
         */
        String              preferredJwsAlgorithm;
        Map<String, String> customParams;

    }

    /**
     * 企业微信
     */
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class WeChatWork extends ServerBasicProperties {

        String              corpId;
        String              secret;
        String              agentId;
        String              principalAttributeId;
        Map<String, String> profileAttrs;
    }


}
