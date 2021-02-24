package com.github.casside.pac4jx.clients.app.config;

import com.github.casside.pac4jx.clients.pac4j.Pac4jXProperties;
import com.github.casside.pac4jx.clients.pac4j.Pac4jXProperties.WeChatWork;
import com.github.casside.pac4jx.clients.pac4j.core.ClientRepositoryClientFinder;
import com.github.casside.pac4jx.clients.pac4j.core.DelegatingSecurityClientFinder;
import com.github.casside.pac4jx.clients.pac4j.core.client.SimpleClientRepository;
import com.github.casside.pac4jx.clients.pac4j.wechatwork.WeChatWorkApi;
import com.github.casside.pac4jx.clients.pac4j.wechatwork.WeChatWorkClient;
import com.github.casside.pac4jx.clients.pac4j.wechatwork.WeChatWorkClientBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpJedisConfigStorage;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.pac4j.core.client.finder.ClientFinder;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.springframework.annotation.AnnotationConfig;
import org.pac4j.springframework.component.ComponentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import redis.clients.jedis.JedisPool;

@Configuration
@Import({ComponentConfig.class, AnnotationConfig.class})
@EnableConfigurationProperties(Pac4jXProperties.class)
@RequiredArgsConstructor
@Slf4j
public class Pac4jConfig {

    private final Pac4jXProperties pac4jXProperties;

    @Autowired
    RedisProperties redisProperties;

    /**
     * simple client repo
     *
     * 手动设置 client finder
     */
    @Bean
    public ClientFinder simpleClientFinder() {
        SimpleClientRepository simpleClientRepository = new SimpleClientRepository();

        // just a demo
        // Add WeChatWorkClient to Repo
        WeChatWorkClient weChatWorkClient = obtainWeChatWorkClient();
        Optional.ofNullable(weChatWorkClient).ifPresent(client -> {
            client.setWeChatWorkApi(obtainWeChatWorkApi());
            simpleClientRepository.addClient(client);
        });

        return new ClientRepositoryClientFinder(simpleClientRepository);
    }

    private WeChatWorkClient obtainWeChatWorkClient() {
        String           callbackUrl = pac4jXProperties.getCallbackUrl();
        List<WeChatWork> weChatWorks = pac4jXProperties.getClient().getWeChatWorks();
        if (weChatWorks.isEmpty()) {
            return null;
        }
        return WeChatWorkClientBuilder.buildWeChatWorkClient(callbackUrl, weChatWorks.get(0));
    }

    /**
     * 手动指定 api
     */
    private WeChatWorkApi obtainWeChatWorkApi() {
        WeChatWorkApi weChatWorkApi = new WeChatWorkApi();
        // 配置自己的token管理器
        weChatWorkApi.setAccessTokenSupplier((appId, appSecret, token) -> {
            String                  host       = redisProperties.getHost();
            int                     port       = redisProperties.getPort();
            String                  password   = redisProperties.getPassword();
            int                     timeout    = Long.valueOf(redisProperties.getTimeout().getSeconds()).intValue();
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            JedisPool               jedisPool  = new JedisPool(poolConfig, host, port, timeout, password);

            // 可以考虑是否可以用 storage manager 管理
            WxCpJedisConfigStorage storage = new WxCpJedisConfigStorage(jedisPool);
            storage.setCorpId(appId);
            storage.setCorpSecret(appSecret);

            WxCpService wxCpService = new WxCpServiceImpl();
            wxCpService.setWxCpConfigStorage(storage);
            try {
                return new OAuth2AccessToken(wxCpService.getAccessToken());
            } catch (WxErrorException e) {
                log.error(e.getMessage(), e);
                throw new OAuthException(e);
            }
        });
        return weChatWorkApi;
    }

    @Bean
    public SecurityLogic securityLogic() {

        DelegatingSecurityClientFinder securityClientFinder = new DelegatingSecurityClientFinder();
        securityClientFinder.addClientFinder(simpleClientFinder());

        DefaultSecurityLogic<Object, WebContext> securityLogic = new DefaultSecurityLogic<>();
        securityLogic.setClientFinder(securityClientFinder);

        return securityLogic;
    }


}
