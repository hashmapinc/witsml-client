package com.hashmapinc.tempus.witsml.client;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Vector;

public class StoreSoapBindingStub extends Stub implements StoreSoapPort_PortType {
    static OperationDesc[] _operations;

    static {
        _operations = new OperationDesc[7];
        _initOperationDesc1();
    }

    private Vector cachedSerClasses = new Vector();
    private Vector cachedSerQNames = new Vector();
    private Vector cachedSerFactories = new Vector();
    private Vector cachedDeserFactories = new Vector();

    public StoreSoapBindingStub() throws AxisFault {
        this(null);
    }

    public StoreSoapBindingStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public StoreSoapBindingStub(Service service) throws AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
    }

    private static void _initOperationDesc1() {
        OperationDesc oper;
        ParameterDesc param;
        oper = new OperationDesc();
        oper.setName("WMLS_AddToStore");
        param = new ParameterDesc(new QName("", "WMLtypeIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "XMLin"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "OptionsIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "CapabilitiesIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "SuppMsgOut"), ParameterDesc.OUT, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "short"));
        oper.setReturnClass(short.class);
        oper.setReturnQName(new QName("", "Result"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[0] = oper;

        oper = new OperationDesc();
        oper.setName("WMLS_DeleteFromStore");
        param = new ParameterDesc(new QName("", "WMLtypeIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "QueryIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "OptionsIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "CapabilitiesIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "SuppMsgOut"), ParameterDesc.OUT, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "short"));
        oper.setReturnClass(short.class);
        oper.setReturnQName(new QName("", "Result"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[1] = oper;

        oper = new OperationDesc();
        oper.setName("WMLS_GetBaseMsg");
        param = new ParameterDesc(new QName("", "ReturnValueIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("", "Result"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[2] = oper;

        oper = new OperationDesc();
        oper.setName("WMLS_GetCap");
        param = new ParameterDesc(new QName("", "OptionsIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "CapabilitiesOut"), ParameterDesc.OUT, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "SuppMsgOut"), ParameterDesc.OUT, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "short"));
        oper.setReturnClass(short.class);
        oper.setReturnQName(new QName("", "Result"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[3] = oper;

        oper = new OperationDesc();
        oper.setName("WMLS_GetFromStore");
        param = new ParameterDesc(new QName("", "WMLtypeIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "QueryIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "OptionsIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "CapabilitiesIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "XMLout"), ParameterDesc.OUT, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "SuppMsgOut"), ParameterDesc.OUT, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "short"));
        oper.setReturnClass(short.class);
        oper.setReturnQName(new QName("", "Result"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[4] = oper;

        oper = new OperationDesc();
        oper.setName("WMLS_GetVersion");
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("", "Result"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[5] = oper;

        oper = new OperationDesc();
        oper.setName("WMLS_UpdateInStore");
        param = new ParameterDesc(new QName("", "WMLtypeIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "XMLin"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "OptionsIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "CapabilitiesIn"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("", "SuppMsgOut"), ParameterDesc.OUT, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "short"));
        oper.setReturnClass(short.class);
        oper.setReturnQName(new QName("", "Result"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[6] = oper;

    }

    private Call createCall() throws RemoteException {
        try {
            Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            return _call;
        } catch (Throwable _t) {
            throw new AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public short WMLS_AddToStore(String WMLtypeIn, String XMLin, String optionsIn, String capabilitiesIn, javax.xml.rpc.holders.StringHolder suppMsgOut) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.witsml.org/action/120/Store.WMLS_AddToStore");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://www.witsml.org/message/120", "WMLS_AddToStore"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[]{WMLtypeIn, XMLin, optionsIn, capabilitiesIn});

        if (_resp instanceof RemoteException) {
            throw (RemoteException) _resp;
        } else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                suppMsgOut.value = (String) _output.get(new QName("", "SuppMsgOut"));
            } catch (Exception _exception) {
                suppMsgOut.value = (String) org.apache.axis.utils.JavaUtils.convert(_output.get(new QName("", "SuppMsgOut")), String.class);
            }
            try {
                return (Short) _resp;
            } catch (Exception _exception) {
                return (Short) org.apache.axis.utils.JavaUtils.convert(_resp, short.class);
            }
        }
    }

    public short WMLS_DeleteFromStore(String WMLtypeIn, String queryIn, String optionsIn, String capabilitiesIn, javax.xml.rpc.holders.StringHolder suppMsgOut) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.witsml.org/action/120/Store.WMLS_DeleteFromStore");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://www.witsml.org/message/120", "WMLS_DeleteFromStore"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[]{WMLtypeIn, queryIn, optionsIn, capabilitiesIn});

        if (_resp instanceof RemoteException) {
            throw (RemoteException) _resp;
        } else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                suppMsgOut.value = (String) _output.get(new QName("", "SuppMsgOut"));
            } catch (Exception _exception) {
                suppMsgOut.value = (String) org.apache.axis.utils.JavaUtils.convert(_output.get(new QName("", "SuppMsgOut")), String.class);
            }
            try {
                return (Short) _resp;
            } catch (Exception _exception) {
                return (Short) org.apache.axis.utils.JavaUtils.convert(_resp, short.class);
            }
        }
    }

    public String WMLS_GetBaseMsg(short returnValueIn) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.witsml.org/action/120/Store.WMLS_GetBaseMsg");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://www.witsml.org/message/120", "WMLS_GetBaseMsg"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[]{returnValueIn});

        if (_resp instanceof RemoteException) {
            throw (RemoteException) _resp;
        } else {
            extractAttachments(_call);
            try {
                return (String) _resp;
            } catch (Exception _exception) {
                return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
            }
        }
    }

    public short WMLS_GetCap(String optionsIn, javax.xml.rpc.holders.StringHolder capabilitiesOut, javax.xml.rpc.holders.StringHolder suppMsgOut) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.witsml.org/action/120/Store.WMLS_GetCap");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://www.witsml.org/message/120", "WMLS_GetCap"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[]{optionsIn});

        if (_resp instanceof RemoteException) {
            throw (RemoteException) _resp;
        } else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                capabilitiesOut.value = (String) _output.get(new QName("", "CapabilitiesOut"));
            } catch (Exception _exception) {
                capabilitiesOut.value = (String) org.apache.axis.utils.JavaUtils.convert(_output.get(new QName("", "CapabilitiesOut")), String.class);
            }
            try {
                suppMsgOut.value = (String) _output.get(new QName("", "SuppMsgOut"));
            } catch (Exception _exception) {
                suppMsgOut.value = (String) org.apache.axis.utils.JavaUtils.convert(_output.get(new QName("", "SuppMsgOut")), String.class);
            }
            try {
                return (Short) _resp;
            } catch (Exception _exception) {
                return (Short) org.apache.axis.utils.JavaUtils.convert(_resp, short.class);
            }
        }
    }

    public short WMLS_GetFromStore(String WMLtypeIn, String queryIn, String optionsIn, String capabilitiesIn, javax.xml.rpc.holders.StringHolder XMLout, javax.xml.rpc.holders.StringHolder suppMsgOut) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.witsml.org/action/120/Store.WMLS_GetFromStore");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://www.witsml.org/message/120", "WMLS_GetFromStore"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[]{WMLtypeIn, queryIn, optionsIn, capabilitiesIn});

        if (_resp instanceof RemoteException) {
            throw (RemoteException) _resp;
        } else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                XMLout.value = (String) _output.get(new QName("", "XMLout"));
            } catch (Exception _exception) {
                XMLout.value = (String) org.apache.axis.utils.JavaUtils.convert(_output.get(new QName("", "XMLout")), String.class);
            }
            try {
                suppMsgOut.value = (String) _output.get(new QName("", "SuppMsgOut"));
            } catch (Exception _exception) {
                suppMsgOut.value = (String) org.apache.axis.utils.JavaUtils.convert(_output.get(new QName("", "SuppMsgOut")), String.class);
            }
            try {
                return (Short) _resp;
            } catch (Exception _exception) {
                return (Short) org.apache.axis.utils.JavaUtils.convert(_resp, short.class);
            }
        }
    }

    public String WMLS_GetVersion() throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.witsml.org/action/120/Store.WMLS_GetVersion");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://www.witsml.org/message/120", "WMLS_GetVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[]{});

        if (_resp instanceof RemoteException) {
            throw (RemoteException) _resp;
        } else {
            extractAttachments(_call);
            try {
                return (String) _resp;
            } catch (Exception _exception) {
                return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
            }
        }
    }

    public short WMLS_UpdateInStore(String WMLtypeIn, String XMLin, String optionsIn, String capabilitiesIn, javax.xml.rpc.holders.StringHolder suppMsgOut) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.witsml.org/action/120/Store.WMLS_UpdateInStore");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("http://www.witsml.org/message/120", "WMLS_UpdateInStore"));

        setRequestHeaders(_call);
        setAttachments(_call);
        Object _resp = _call.invoke(new Object[]{WMLtypeIn, XMLin, optionsIn, capabilitiesIn});

        if (_resp instanceof RemoteException) {
            throw (RemoteException) _resp;
        } else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                suppMsgOut.value = (String) _output.get(new QName("", "SuppMsgOut"));
            } catch (Exception _exception) {
                suppMsgOut.value = (String) org.apache.axis.utils.JavaUtils.convert(_output.get(new QName("", "SuppMsgOut")), String.class);
            }
            try {
                return (Short) _resp;
            } catch (Exception _exception) {
                return (Short) org.apache.axis.utils.JavaUtils.convert(_resp, short.class);
            }
        }
    }

}
