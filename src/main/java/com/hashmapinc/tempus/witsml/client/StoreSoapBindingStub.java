/**
 * StoreSoapBindingStub.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:05:37 GMT)
 * <p>
 * %AXIS2_HOME%/bin/wsdl2java.sh -b -s -uri StoreSoapBinding.wsdl -p com.hashmapinc.tempus.witsml.client -d jaxbri
 */
package com.hashmapinc.tempus.witsml.client;


import com.hashmapinc.tempus.witsml.message._120.*;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.ds.jaxb.JAXBOMDataSource;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.util.jaxb.JAXBUtils;
import org.apache.axiom.util.jaxb.UnmarshallerAdapter;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.FaultMapKey;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.OutInAxisOperation;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.wsdl.WSDLConstants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;

/*
 *  StoreSoapBindingStub java implementation
 */
public class StoreSoapBindingStub extends AbstractStoreSoapBindingStub {

    private static int counter = 0;

    //http://yourorg.com/yourwebservice
    private static final JAXBContext wsContext;

    static {
        JAXBContext jc;
        jc = null;

        try {
            jc = JAXBContext.newInstance(WMLSGetFromStore.class,
                    WMLSGetFromStoreResponse.class,
                    WMLSGetBaseMsg.class,
                    WMLSGetBaseMsgResponse.class,
                    WMLSGetVersion.class,
                    WMLSGetVersionResponse.class,
                    WMLSDeleteFromStore.class,
                    WMLSDeleteFromStoreResponse.class,
                    WMLSGetCap.class,
                    WMLSGetCapResponse.class,
                    WMLSAddToStore.class,
                    WMLSAddToStoreResponse.class,
                    WMLSUpdateInStore.class,
                    WMLSUpdateInStoreResponse.class);
        } catch (JAXBException ex) {
            System.err.println("Unable to create JAXBContext: " +
                    ex.getMessage());
            ex.printStackTrace(System.err);
            Runtime.getRuntime().exit(-1);
        } finally {
            wsContext = jc;
        }
    }

    protected AxisOperation[] _operations;

    //hashmaps to keep the fault mapping
    private HashMap faultExceptionNameMap = new HashMap();
    private HashMap faultExceptionClassNameMap = new HashMap();
    private HashMap faultMessageMap = new HashMap();
    private QName[] opNameArray = null;

    /**
     *Constructor that takes in a configContext
     */
    public StoreSoapBindingStub(
            ConfigurationContext configurationContext,
            String targetEndpoint) throws AxisFault {
        this(configurationContext, targetEndpoint, false);
    }

    /**
     * Constructor that takes in a configContext  and useseperate listner
     */
    public StoreSoapBindingStub(
            ConfigurationContext configurationContext,
            String targetEndpoint, boolean useSeparateListener)
            throws AxisFault {
        //To populate AxisService
        populateAxisService();
        populateFaults();

        _serviceClient = new ServiceClient(configurationContext,
                _service);

        _serviceClient.getOptions()
                .setTo(new EndpointReference(
                        targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    /**
     * Default Constructor
     */
    public StoreSoapBindingStub(
            ConfigurationContext configurationContext)
            throws AxisFault {
        this(configurationContext, "http://yourorg.com/yourwebservice");
    }

    /**
     * Default Constructor
     */
    public StoreSoapBindingStub() throws AxisFault {
        this("http://yourorg.com/yourwebservice");
    }

    /**
     * Constructor taking the target endpoint
     */
    public StoreSoapBindingStub(String targetEndpoint)
            throws AxisFault {
        this(null, targetEndpoint);
    }

    private static synchronized String getUniqueSuffix() {
        // reset the counter if it is greater than 99999
        if (counter > 99999) {
            counter = 0;
        }

        counter = counter + 1;

        return Long.toString(System.currentTimeMillis()) +
                "_" + counter;
    }

    private void populateAxisService() throws AxisFault {
        //creating the Service with a unique name
        _service = new AxisService(
                "StoreSoapBinding" + getUniqueSuffix());
        addAnonymousOperations();

        //creating the operations
        AxisOperation __operation;

        _operations = new AxisOperation[7];

        __operation = new OutInAxisOperation();

        __operation.setName(new QName(
                "http://www.witsml.org/wsdl/120", "wMLS_GetFromStore"));
        _service.addOperation(__operation);

        _operations[0] = __operation;

        __operation = new OutInAxisOperation();

        __operation.setName(new QName(
                "http://www.witsml.org/wsdl/120", "wMLS_GetBaseMsg"));
        _service.addOperation(__operation);

        _operations[1] = __operation;

        __operation = new OutInAxisOperation();

        __operation.setName(new QName(
                "http://www.witsml.org/wsdl/120", "wMLS_GetVersion"));
        _service.addOperation(__operation);

        _operations[2] = __operation;

        __operation = new OutInAxisOperation();

        __operation.setName(new QName(
                "http://www.witsml.org/wsdl/120", "wMLS_DeleteFromStore"));
        _service.addOperation(__operation);

        _operations[3] = __operation;

        __operation = new OutInAxisOperation();

        __operation.setName(new QName(
                "http://www.witsml.org/wsdl/120", "wMLS_GetCap"));
        _service.addOperation(__operation);

        _operations[4] = __operation;

        __operation = new OutInAxisOperation();

        __operation.setName(new QName(
                "http://www.witsml.org/wsdl/120", "wMLS_AddToStore"));
        _service.addOperation(__operation);

        _operations[5] = __operation;

        __operation = new OutInAxisOperation();

        __operation.setName(new QName(
                "http://www.witsml.org/wsdl/120", "wMLS_UpdateInStore"));
        _service.addOperation(__operation);

        _operations[6] = __operation;
    }

    //populates the faults
    private void populateFaults() {
    }

    /**
     * Auto generated method signature
     *
     * @see StoreSoapBindingStub#wMLS_GetFromStore
     * @param wMLS_GetFromStore
     */
    @Override
    public WMLSGetFromStoreResponse wMLS_GetFromStore(
            WMLSGetFromStore wMLS_GetFromStore)
            throws RemoteException {
        MessageContext _messageContext = new MessageContext();

        try {
            OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
            _operationClient.getOptions()
                    .setAction("http://www.witsml.org/action/120/Store.WMLS_GetFromStore");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            // create SOAP envelope with that payload
            SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions()
                            .getSoapVersionURI()),
                    wMLS_GetFromStore,
                    optimizeContent(
                            new QName(
                                    "http://www.witsml.org/wsdl/120",
                                    "wMLS_GetFromStore")),
                    new QName(
                            "http://www.witsml.org/message/120", "WMLS_GetFromStore"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody()
                            .getFirstElement(),
                    WMLSGetFromStoreResponse.class);

            return (WMLSGetFromStoreResponse) object;
        } catch (AxisFault f) {
            OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(
                        new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetFromStore"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetFromStore"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetFromStore"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,
                                messageClass);
                        Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender()
                        .cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see StoreSoapBindingStub#wMLS_GetBaseMsg
     * @param wMLS_GetBaseMsg
     */
    @Override
    public WMLSGetBaseMsgResponse wMLS_GetBaseMsg(
            WMLSGetBaseMsg wMLS_GetBaseMsg)
            throws RemoteException {
        MessageContext _messageContext = new MessageContext();

        try {
            OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
            _operationClient.getOptions()
                    .setAction("http://www.witsml.org/action/120/Store.WMLS_GetBaseMsg");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            // create SOAP envelope with that payload
            SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions()
                            .getSoapVersionURI()),
                    wMLS_GetBaseMsg,
                    optimizeContent(
                            new QName(
                                    "http://www.witsml.org/wsdl/120", "wMLS_GetBaseMsg")),
                    new QName(
                            "http://www.witsml.org/message/120", "WMLS_GetBaseMsg"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody()
                            .getFirstElement(),
                    WMLSGetBaseMsgResponse.class);

            return (WMLSGetBaseMsgResponse) object;
        } catch (AxisFault f) {
            OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(
                        new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetBaseMsg"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetBaseMsg"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetBaseMsg"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,
                                messageClass);
                        Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender()
                        .cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see StoreSoapBindingStub#wMLS_GetVersion
     * @param wMLS_GetVersion
     */
    @Override
    public WMLSGetVersionResponse wMLS_GetVersion(
            WMLSGetVersion wMLS_GetVersion)
            throws RemoteException {
        MessageContext _messageContext = new MessageContext();

        try {
            OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
            _operationClient.getOptions()
                    .setAction("http://www.witsml.org/action/120/Store.WMLS_GetVersion");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            // create SOAP envelope with that payload
            SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions()
                            .getSoapVersionURI()),
                    wMLS_GetVersion,
                    optimizeContent(
                            new QName(
                                    "http://www.witsml.org/wsdl/120", "wMLS_GetVersion")),
                    new QName(
                            "http://www.witsml.org/message/120", "WMLS_GetVersion"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody()
                            .getFirstElement(),
                    WMLSGetVersionResponse.class);

            return (WMLSGetVersionResponse) object;
        } catch (AxisFault f) {
            OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(
                        new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetVersion"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetVersion"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetVersion"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,
                                messageClass);
                        Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender()
                        .cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see StoreSoapBindingStub#wMLS_DeleteFromStore
     * @param wMLS_DeleteFromStore
     */
    @Override
    public WMLSDeleteFromStoreResponse wMLS_DeleteFromStore(
            WMLSDeleteFromStore wMLS_DeleteFromStore)
            throws RemoteException {
        MessageContext _messageContext = new MessageContext();

        try {
            OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
            _operationClient.getOptions()
                    .setAction("http://www.witsml.org/action/120/Store.WMLS_DeleteFromStore");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            // create SOAP envelope with that payload
            SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions()
                            .getSoapVersionURI()),
                    wMLS_DeleteFromStore,
                    optimizeContent(
                            new QName(
                                    "http://www.witsml.org/wsdl/120",
                                    "wMLS_DeleteFromStore")),
                    new QName(
                            "http://www.witsml.org/message/120",
                            "WMLS_DeleteFromStore"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody()
                            .getFirstElement(),
                    WMLSDeleteFromStoreResponse.class);

            return (WMLSDeleteFromStoreResponse) object;
        } catch (AxisFault f) {
            OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(
                        new FaultMapKey(
                                faultElt.getQName(), "WMLS_DeleteFromStore"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_DeleteFromStore"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_DeleteFromStore"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,
                                messageClass);
                        Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender()
                        .cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see StoreSoapBindingStub#wMLS_GetCap
     * @param wMLS_GetCap
     */
    @Override
    public WMLSGetCapResponse wMLS_GetCap(
            WMLSGetCap wMLS_GetCap)
            throws RemoteException {
        MessageContext _messageContext = new MessageContext();

        try {
            OperationClient _operationClient = _serviceClient.createClient(_operations[4].getName());
            _operationClient.getOptions()
                    .setAction("http://www.witsml.org/action/120/Store.WMLS_GetCap");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            // create SOAP envelope with that payload
            SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions()
                            .getSoapVersionURI()),
                    wMLS_GetCap,
                    optimizeContent(
                            new QName(
                                    "http://www.witsml.org/wsdl/120", "wMLS_GetCap")),
                    new QName(
                            "http://www.witsml.org/message/120", "WMLS_GetCap"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody()
                            .getFirstElement(),
                    WMLSGetCapResponse.class);

            return (WMLSGetCapResponse) object;
        } catch (AxisFault f) {
            OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(
                        new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetCap"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetCap"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_GetCap"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,
                                messageClass);
                        Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender()
                        .cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see StoreSoapBindingStub#wMLS_AddToStore
     * @param wMLS_AddToStore
     */
    @Override
    public WMLSAddToStoreResponse wMLS_AddToStore(
            WMLSAddToStore wMLS_AddToStore)
            throws RemoteException {
        MessageContext _messageContext = new MessageContext();

        try {
            OperationClient _operationClient = _serviceClient.createClient(_operations[5].getName());
            _operationClient.getOptions()
                    .setAction("http://www.witsml.org/action/120/Store.WMLS_AddToStore");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            // create SOAP envelope with that payload
            SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions()
                            .getSoapVersionURI()),
                    wMLS_AddToStore,
                    optimizeContent(
                            new QName(
                                    "http://www.witsml.org/wsdl/120", "wMLS_AddToStore")),
                    new QName(
                            "http://www.witsml.org/message/120", "WMLS_AddToStore"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody()
                            .getFirstElement(),
                    WMLSAddToStoreResponse.class);

            return (WMLSAddToStoreResponse) object;
        } catch (AxisFault f) {
            OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(
                        new FaultMapKey(
                                faultElt.getQName(), "WMLS_AddToStore"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_AddToStore"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_AddToStore"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,
                                messageClass);
                        Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender()
                        .cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see StoreSoapBindingStub#wMLS_UpdateInStore
     * @param wMLS_UpdateInStore
     */
    @Override
    public WMLSUpdateInStoreResponse wMLS_UpdateInStore(
            WMLSUpdateInStore wMLS_UpdateInStore)
            throws RemoteException {
        MessageContext _messageContext = new MessageContext();

        try {
            OperationClient _operationClient = _serviceClient.createClient(_operations[6].getName());
            _operationClient.getOptions()
                    .setAction("http://www.witsml.org/action/120/Store.WMLS_UpdateInStore");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            // create SOAP envelope with that payload
            SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions()
                            .getSoapVersionURI()),
                    wMLS_UpdateInStore,
                    optimizeContent(
                            new QName(
                                    "http://www.witsml.org/wsdl/120",
                                    "wMLS_UpdateInStore")),
                    new QName(
                            "http://www.witsml.org/message/120",
                            "WMLS_UpdateInStore"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody()
                            .getFirstElement(),
                    WMLSUpdateInStoreResponse.class);

            return (WMLSUpdateInStoreResponse) object;
        } catch (AxisFault f) {
            OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(
                        new FaultMapKey(
                                faultElt.getQName(), "WMLS_UpdateInStore"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_UpdateInStore"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new FaultMapKey(
                                faultElt.getQName(), "WMLS_UpdateInStore"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,
                                messageClass);
                        Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[]{messageClass});
                        m.invoke(ex, new Object[]{messageObject});

                        throw new RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender()
                        .cleanup(_messageContext);
            }
        }
    }

    private boolean optimizeContent(QName opName) {
        if (opNameArray == null) {
            return false;
        }

        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;
            }
        }

        return false;
    }

    private OMElement toOM(
            WMLSGetFromStore param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetFromStore param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSGetFromStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetFromStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSGetBaseMsg param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetBaseMsg param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSGetBaseMsgResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetBaseMsgResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSGetVersion param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetVersion param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSGetVersionResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetVersionResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSDeleteFromStore param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSDeleteFromStore param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSDeleteFromStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSDeleteFromStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSGetCap param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetCap param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSGetCapResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSGetCapResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSAddToStore param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSAddToStore param, boolean optimizeContent,
            QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSAddToStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSAddToStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSUpdateInStore param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSUpdateInStore param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    private OMElement toOM(
            WMLSUpdateInStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        OMFactory factory = OMAbstractFactory.getOMFactory();

        Object object = param;
        JAXBOMDataSource source = new JAXBOMDataSource(wsContext,
                new JAXBElement(elementQName, object.getClass(),
                        object));
        OMNamespace namespace = factory.createOMNamespace(elementQName.getNamespaceURI(),
                null);

        return factory.createOMElement(source, elementQName.getLocalPart(),
                namespace);
    }

    private SOAPEnvelope toEnvelope(
            SOAPFactory factory,
            WMLSUpdateInStoreResponse param,
            boolean optimizeContent, QName elementQName)
            throws AxisFault {
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(toOM(param, optimizeContent, elementQName));

        return envelope;
    }

    /**
     *  get the default envelope
     */
    private SOAPEnvelope toEnvelope(
            SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private Object fromOM(OMElement param,
                          Class type) throws AxisFault {
        try {
            JAXBContext context = wsContext;
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UnmarshallerAdapter adapter = JAXBUtils.getUnmarshallerAdapter(param.getXMLStreamReaderWithoutCaching());
            unmarshaller.setAttachmentUnmarshaller(adapter.getAttachmentUnmarshaller());

            return unmarshaller.unmarshal(adapter.getReader(), type).getValue();
        } catch (JAXBException bex) {
            throw AxisFault.makeFault(bex);
        }
    }
}