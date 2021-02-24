package com.github.casside.pac4jx.clients.pac4j.core;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.pac4j.core.client.finder.DefaultCallbackClientFinder;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DelegatingCallbackClientFinder extends DelegatingClientFinder {

    public DelegatingCallbackClientFinder() {
        DefaultCallbackClientFinder defaultCallbackClientFinder = new DefaultCallbackClientFinder();
        this.clientFinders.add(defaultCallbackClientFinder);
    }

}
