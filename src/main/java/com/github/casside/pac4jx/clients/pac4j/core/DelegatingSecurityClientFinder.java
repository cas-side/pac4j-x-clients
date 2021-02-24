package com.github.casside.pac4jx.clients.pac4j.core;

import org.pac4j.core.client.finder.DefaultSecurityClientFinder;

public class DelegatingSecurityClientFinder extends DelegatingClientFinder {

    public DelegatingSecurityClientFinder() {
        DefaultSecurityClientFinder defaultSecurityClientFinder = new DefaultSecurityClientFinder();
        super.clientFinders.add(defaultSecurityClientFinder);
    }

}
