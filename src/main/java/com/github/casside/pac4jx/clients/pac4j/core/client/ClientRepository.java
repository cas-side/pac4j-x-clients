package com.github.casside.pac4jx.clients.pac4j.core.client;

import org.pac4j.core.client.Client;
import org.pac4j.core.context.WebContext;

public interface ClientRepository {

    public Client get(String clientName, WebContext context);

}
