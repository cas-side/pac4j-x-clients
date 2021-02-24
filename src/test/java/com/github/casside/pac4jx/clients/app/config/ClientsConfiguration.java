package com.github.casside.pac4jx.clients.app.config;

import com.github.casside.pac4jx.clients.pac4j.Pac4jXProperties;
import com.github.casside.pac4jx.clients.pac4j.Pac4jXProperties.OIDCProperties;
import com.github.casside.pac4jx.clients.pac4j.wechatwork.WeChatWorkClient;
import com.nimbusds.jose.JWSAlgorithm;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.oidc.profile.OidcProfile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 构建 WeChatWorkClient
 *
 * @see WeChatWorkClient
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@ConditionalOnBean(Pac4jXProperties.class)
@RequiredArgsConstructor
@Configuration
public class ClientsConfiguration {

    private final Pac4jXProperties pac4jXProperties;

    @Bean
    public Config config() {

        final Clients clients = new Clients(pac4jXProperties.getCallbackUrl());

        List<OidcClient> oidcClients = buildOIDCClients();

        List<Client> clientList = new ArrayList<>(oidcClients.size());
        clientList.addAll(oidcClients);
        clients.setClients(clientList);

        return new Config(clients);
    }

    public List<OidcClient> buildOIDCClients() {
        return pac4jXProperties.getClient().getOidcs().stream().map(this::buildOIDCClient).collect(Collectors.toList());
    }

    public OidcClient<OidcProfile, OidcConfiguration> buildOIDCClient(OIDCProperties oidcProperties) {

        // BMSK OIDC
        final OidcConfiguration oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setClientId(oidcProperties.getClientId());
        oidcConfiguration.setSecret(oidcProperties.getSecret());
        oidcConfiguration.setScope(oidcProperties.getScope());
        oidcConfiguration.setDiscoveryURI(oidcProperties.getDiscoveryURI());
        oidcConfiguration.setLogoutUrl(oidcProperties.getLogoutUrl());
        // TODO ...
        oidcConfiguration.setPreferredJwsAlgorithm(JWSAlgorithm.RS256);
//        oidcConfiguration.setPreferredJwsAlgorithm(oidcProperties.getPreferredJwsAlgorithm());
        Optional.ofNullable(oidcProperties.getCustomParams()).ifPresent(oidcConfiguration::setCustomParams);

//        JSONObject metaJson = new JSONObject();
//        oidcConfiguration.setProviderMetadata(OIDCProviderMetadata.parse("{}"));
//        oidcConfiguration.getProviderMetadata().setTokenEndpointAuthMethods(Lists.newArrayList(ClientAuthenticationMethod.CLIENT_SECRET_POST));

        OidcClient<OidcProfile, OidcConfiguration> oidcClient = new OidcClient<>(oidcConfiguration);
        oidcClient.setName(oidcProperties.getPac4jId());
        return oidcClient;
    }

// TODO 定义 Client, Config

}
