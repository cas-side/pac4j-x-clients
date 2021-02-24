package com.github.casside.pac4jx.clients.pac4j.core;

import com.github.casside.pac4jx.clients.pac4j.core.client.ClientRepository;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.finder.ClientFinder;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;

/**
 * 通过接口来获取 Client 的配置，然后生成 Client
 */
@RequiredArgsConstructor
public class ClientRepositoryClientFinder implements ClientFinder {

    private String clientNameParameter = Pac4jConstants.DEFAULT_CLIENT_NAME_PARAMETER;

    final ClientRepository clientRepository;

    @Override
    public List<Client> find(Clients clients, WebContext context, String clientNames) {
        Client client = clientRepository.get(clientNames, context);

        final String defaultClientName = context.getRequestParameter(this.clientNameParameter);
        Client       defaultClient     = clientRepository.get(defaultClientName, context);

        List<Client> foundClients = Lists.newArrayList();
        if (client != null) {
            foundClients.add(client);
        }
        if (defaultClient != null) {
            foundClients.add(defaultClient);
        }

        return foundClients;
    }

    public void setClientNameParameter(String clientNameParameter) {
        this.clientNameParameter = clientNameParameter;
    }
}
