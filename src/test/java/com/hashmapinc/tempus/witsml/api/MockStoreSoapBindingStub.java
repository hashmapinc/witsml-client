package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.witsml.client.AbstractStoreSoapBindingStub;
import com.sun.org.apache.regexp.internal.RE;

import javax.xml.rpc.holders.StringHolder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.stream.Collectors;

public class MockStoreSoapBindingStub extends AbstractStoreSoapBindingStub {

    public short WMLS_AddToStore(String WMLtypeIn, String XMLin, String optionsIn, String capabilitiesIn, StringHolder suppMsgOut) throws RemoteException {
        return 0;
    }

    public short WMLS_DeleteFromStore(String WMLtypeIn, String queryIn, String optionsIn, String capabilitiesIn, StringHolder suppMsgOut) throws RemoteException {
        return 0;
    }

    public String WMLS_GetBaseMsg(short returnValueIn) throws RemoteException {
        return null;
    }

    public short WMLS_GetCap(String optionsIn, StringHolder capabilitiesOut, StringHolder suppMsgOut) throws RemoteException {
        try {
            if (optionsIn.equals("dataVersion=1.4.1.1")) {
                capabilitiesOut.value = getReturnData("caps1411.xml", "1.4.1.1");
                return 1;
            } else {
                capabilitiesOut.value = getReturnData("caps1311.xml", "1.3.1.1");
                return 1;
            }
        } catch (IOException ex) {
            return -1;
        }
    }

    public short WMLS_GetFromStore(String WMLtypeIn, String queryIn, String optionsIn, String capabilitiesIn, StringHolder XMLout, StringHolder suppMsgOut) throws RemoteException {
        try {
            if (optionsIn.equals("dataVersion=1.4.1.1")) {
                XMLout.value = getReturnData(MockObjectType.valueOf(WMLtypeIn.toUpperCase()).toString(), "1.4.1.1");
                return 1;
            } else {
                XMLout.value = getReturnData(MockObjectType.valueOf(WMLtypeIn.toUpperCase()).toString(), "1.3.1.1");
                return 1;
            }
        } catch (IOException ex) {
            return -1;
        }
    }

    public String WMLS_GetVersion() throws RemoteException {
        return "1.3.1.1,1.4.1.1";
    }

    public short WMLS_UpdateInStore(String WMLtypeIn, String XMLin, String optionsIn, String capabilitiesIn, StringHolder suppMsgOut) throws RemoteException {
        return 0;
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
