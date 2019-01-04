package com.hashmapinc.tempus.witsml.client;

import javax.xml.rpc.ServiceException;

public interface WMLS{
    String getStoreSoapPortAddress();

    StoreSoapPort_PortType getStoreSoapPort() throws ServiceException;

    StoreSoapPort_PortType getStoreSoapPort(String portAddress) throws ServiceException;
}
