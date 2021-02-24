package com.github.casside.pac4jx.clients.pac4j.core;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.finder.ClientFinder;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.TechnicalException;

public abstract class DelegatingClientFinder implements ClientFinder {

    protected List<ClientFinder> clientFinders = new ArrayList<>();

    @Override
    public List<Client> find(Clients clients, WebContext context, String clientNames) {

        List<Client> foundClients = Lists.newArrayList();

        clientFinders.forEach(clientFinder -> {
            try {
                foundClients.addAll(clientFinder.find(clients, context, clientNames));
            } catch (TechnicalException e) {
                if (!e.getMessage().toLowerCase().contains("no client found")) {
                    throw e;
                }
            }
        });

        return foundClients;
    }

    public void addClientFinder(ClientFinder clientFinder) {
        this.clientFinders.add(clientFinder);
    }
}
