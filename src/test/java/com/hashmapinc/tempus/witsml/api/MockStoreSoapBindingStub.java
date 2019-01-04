package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.witsml.client.AbstractStoreSoapBindingStub;
import com.hashmapinc.tempus.witsml.message._120.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.stream.Collectors;

public class MockStoreSoapBindingStub extends AbstractStoreSoapBindingStub {

    public WMLSAddToStoreResponse wMLS_AddToStore(WMLSAddToStore wMLS_AddToStore) throws RemoteException {
        WMLSAddToStoreResponse wmlsAddToStoreResponse = new WMLSAddToStoreResponse();
        wmlsAddToStoreResponse.setResult((short)0);
        return wmlsAddToStoreResponse;
    }

    public WMLSDeleteFromStoreResponse wMLS_DeleteFromStore(WMLSDeleteFromStore wMLS_DeleteFromStore) throws RemoteException {
        WMLSDeleteFromStoreResponse wmlsDeleteFromStoreResponse = new WMLSDeleteFromStoreResponse();
        wmlsDeleteFromStoreResponse.setResult((short)0);
        return wmlsDeleteFromStoreResponse;
    }

    public WMLSGetBaseMsgResponse wMLS_GetBaseMsg(WMLSGetBaseMsg wMLS_GetBaseMsg) throws RemoteException {
        return new WMLSGetBaseMsgResponse();
    }

    public WMLSGetCapResponse wMLS_GetCap(WMLSGetCap wMLS_GetCap) throws RemoteException {
        WMLSGetCapResponse wmlsGetCapResponse = new WMLSGetCapResponse();
        try {
            if (wMLS_GetCap.getOptionsIn().equals("dataVersion=1.4.1.1")) {
                wmlsGetCapResponse.setCapabilitiesOut(getReturnData("caps1411.xml", "1.4.1.1"));
                wmlsGetCapResponse.setResult((short) 1);
            } else {
                wmlsGetCapResponse.setCapabilitiesOut(getReturnData("caps1311.xml", "1.3.1.1"));
                wmlsGetCapResponse.setResult((short) 1);
            }
        } catch (IOException ex) {
            wmlsGetCapResponse.setResult((short) -1);
        }
        return wmlsGetCapResponse;
    }

    public WMLSGetFromStoreResponse wMLS_GetFromStore(WMLSGetFromStore wMLS_GetFromStore) throws RemoteException {
        WMLSGetFromStoreResponse wmlsGetFromStoreResponse = new WMLSGetFromStoreResponse();
        try{
            if(wMLS_GetFromStore.getOptionsIn().equals("dataVersion=1.4.1.1")) {
                wmlsGetFromStoreResponse.setXMLout(getReturnData(MockObjectType.valueOf(wMLS_GetFromStore.getWMLtypeIn().toUpperCase()).toString(), "1.4.1.1"));
                wmlsGetFromStoreResponse.setResult((short) 1);
            } else {
                wmlsGetFromStoreResponse.setXMLout(getReturnData(MockObjectType.valueOf(wMLS_GetFromStore.getWMLtypeIn().toUpperCase()).toString(), "1.3.1.1"));
                wmlsGetFromStoreResponse.setResult((short) 1);
            }
        } catch (IOException ex) {
            wmlsGetFromStoreResponse.setResult((short) -1);
        }
        return wmlsGetFromStoreResponse;
    }

    public WMLSGetVersionResponse wMLS_GetVersion(WMLSGetVersion wMLS_GetVersion) throws RemoteException {
        WMLSGetVersionResponse wmlsGetVersionResponse = new WMLSGetVersionResponse();
        wmlsGetVersionResponse.setResult("1.3.1.1,1.4.1.1");
        return wmlsGetVersionResponse;
    }

    public WMLSUpdateInStoreResponse wMLS_UpdateInStore(WMLSUpdateInStore wMLS_UpdateInStore) throws RemoteException {
        WMLSUpdateInStoreResponse wmlsUpdateInStoreResponse = new WMLSUpdateInStoreResponse();
        wmlsUpdateInStoreResponse.setResult((short) 0);
        return wmlsUpdateInStoreResponse;
    }

    private String getReturnData(String resourcePath, String version) throws IOException {
        StringBuilder resourceStr = new StringBuilder();
        if (version.toString().equals("1.3.1.1")) {
            resourceStr.append("1311/");
        } else {
            resourceStr.append("1411/");
        }
        String path = resourceStr.append(resourcePath).toString();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
