package com.hashmapinc.tempus.witsml.client;

import org.apache.axis2.AxisFault;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

public class WMLSLocator implements WMLS {

    public WMLSLocator() {
    }

    // Use to get a proxy class for StoreSoapPort
    private String StoreSoapPort_address = "http://yourorg.com/yourwebservice";

    public String getStoreSoapPortAddress() {
        return StoreSoapPort_address;
    }

    public void setStoreSoapPortEndpointAddress(String address) {
        StoreSoapPort_address = address;
    }

    public StoreSoapPort_PortType getStoreSoapPort() {
        return getStoreSoapPort(StoreSoapPort_address);
    }

    public StoreSoapPort_PortType getStoreSoapPort(String portAddress) {
        try {
            return new StoreSoapBindingStub(portAddress);
        } catch (AxisFault e) {
            return null;
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address) throws ServiceException {

        if ("StoreSoapPort".equals(portName)) {
            setStoreSoapPortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }
}
