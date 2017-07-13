package com.hashmapinc.tempus.witsml.client;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;

public class WMLSLocator extends Service implements WMLS {

    public WMLSLocator() {
    }


    public WMLSLocator(EngineConfiguration config) {
        super(config);
    }

    public WMLSLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for StoreSoapPort
    private String StoreSoapPort_address = "http://yourorg.com/yourwebservice";

    public String getStoreSoapPortAddress() {
        return StoreSoapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String StoreSoapPortWSDDServiceName = "StoreSoapPort";

    public String getStoreSoapPortWSDDServiceName() {
        return StoreSoapPortWSDDServiceName;
    }

    public void setStoreSoapPortWSDDServiceName(String name) {
        StoreSoapPortWSDDServiceName = name;
    }

    public StoreSoapPort_PortType getStoreSoapPort() throws ServiceException {
       URL endpoint;
        try {
            endpoint = new URL(StoreSoapPort_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getStoreSoapPort(endpoint);
    }

    public StoreSoapPort_PortType getStoreSoapPort(URL portAddress) throws ServiceException {
        try {
            StoreSoapBindingStub _stub = new StoreSoapBindingStub(portAddress, this);
            _stub.setPortName(getStoreSoapPortWSDDServiceName());
            return _stub;
        }
        catch (AxisFault e) {
            return null;
        }
    }

    public void setStoreSoapPortEndpointAddress(String address) {
        StoreSoapPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (StoreSoapPort_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                StoreSoapBindingStub _stub = new StoreSoapBindingStub(new URL(StoreSoapPort_address), this);
                _stub.setPortName(getStoreSoapPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("StoreSoapPort".equals(inputPortName)) {
            return getStoreSoapPort();
        }
        else  {
            Remote _stub = getPort(serviceEndpointInterface);
            ((Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://www.witsml.org/wsdl/120", "WMLS");
    }

    private HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://www.witsml.org/wsdl/120", "StoreSoapPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws ServiceException {
        
if ("StoreSoapPort".equals(portName)) {
            setStoreSoapPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
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
