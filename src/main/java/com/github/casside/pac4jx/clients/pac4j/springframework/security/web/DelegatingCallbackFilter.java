package com.github.casside.pac4jx.clients.pac4j.springframework.security.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.finder.ClientFinder;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.springframework.security.web.CallbackFilter;

/**
 * 包装 CallbackFilter，每次都动态设置 Pac4j.Config
 */
@RequiredArgsConstructor
public class DelegatingCallbackFilter implements Filter {

    final CallbackFilter callbackFilter;

    final List<ClientFinder> clientFinders;

    final Config config;

    private Config obtainConfig(WebContext context, String clientNames) {
        Clients clients = config.getClients();
//        List<Client> foundClients = clientFinder.find(clients, context, clientNames);
        List<Client> foundClients =
            clientFinders.stream().map(clientFinder -> {
                return clientFinder.find(clients, context, clientNames);
            }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());

        Config tmpConfig = cloneConfig(config);

        Clients newClients = new Clients();

        List<Client> newClientList = new ArrayList<>(foundClients);
        newClients.setClients(newClientList);
        tmpConfig.setClients(newClients);

        return tmpConfig;
    }

    private Config cloneConfig(Config config) {
        Config tmpConfig = new Config(config.getClients());
        tmpConfig.setAuthorizers(config.getAuthorizers());
        tmpConfig.setCallbackLogic(config.getCallbackLogic());
        tmpConfig.setAuthorizers(config.getAuthorizers());
        tmpConfig.setHttpActionAdapter(config.getHttpActionAdapter());
        tmpConfig.setLogoutLogic(config.getLogoutLogic());
        tmpConfig.setSecurityLogic(config.getSecurityLogic());
        tmpConfig.setSessionStore(config.getSessionStore());
        tmpConfig.setMatchers(config.getMatchers());
        tmpConfig.setProfileManagerFactory(config.getProfileManagerFactory());
        return tmpConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
        throws IOException, ServletException {
        final J2EContext context = new J2EContext((HttpServletRequest) req, (HttpServletResponse) resp, config.getSessionStore());

        callbackFilter.setConfig(obtainConfig(context, null));
        callbackFilter.doFilter(req, resp, filterChain);
    }

    @Override
    public void destroy() {

    }
}
