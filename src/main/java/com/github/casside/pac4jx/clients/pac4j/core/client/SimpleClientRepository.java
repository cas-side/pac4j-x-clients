package com.github.casside.pac4jx.clients.pac4j.core.client;

import java.util.HashMap;
import java.util.Map;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.WebContext;

public class SimpleClientRepository implements ClientRepository {

    private static Map<String, Client> clientMap = new HashMap<>();

    @Override
    public Client get(String clientName, WebContext context) {
        Client client = clientMap.get(clientName);

        if (client instanceof IndirectClient) {
            final IndirectClient indirectClient = (IndirectClient) client;
            indirectClient.init();
//            if (indirectClient.getCallbackUrlResolver().matches(indirectClient.getName(), context)) {
            return indirectClient;
//            }
        }
        return null;
    }

    public void addClient(Client client) {
        clientMap.put(client.getName(), client);
    }
}
