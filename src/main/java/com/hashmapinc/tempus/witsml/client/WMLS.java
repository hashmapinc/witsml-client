package com.hashmapinc.tempus.witsml.client;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface WMLS extends Service {
    public String getStoreSoapPortAddress();

    public StoreSoapPort_PortType getStoreSoapPort() throws ServiceException;

    public StoreSoapPort_PortType getStoreSoapPort(URL portAddress) throws ServiceException;
}
