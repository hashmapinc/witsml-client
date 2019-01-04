package com.hashmapinc.tempus.witsml.client;

import org.apache.axis2.client.Stub;

public abstract class AbstractStoreSoapBindingStub extends Stub implements StoreSoapPort_PortType {

    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
