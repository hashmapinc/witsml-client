package com.hashmapinc.tempus.witsml.client;

import javax.xml.rpc.holders.StringHolder;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StoreSoapPort_PortType extends Remote {
    public short WMLS_AddToStore(String WMLtypeIn, String XMLin, String optionsIn, String capabilitiesIn, StringHolder suppMsgOut) throws RemoteException;

    public short WMLS_DeleteFromStore(String WMLtypeIn, String queryIn, String optionsIn, String capabilitiesIn, StringHolder suppMsgOut) throws RemoteException;

    public String WMLS_GetBaseMsg(short returnValueIn) throws RemoteException;

    public short WMLS_GetCap(String optionsIn, StringHolder capabilitiesOut, StringHolder suppMsgOut) throws RemoteException;

    public short WMLS_GetFromStore(String WMLtypeIn, String queryIn, String optionsIn, String capabilitiesIn, StringHolder XMLout, StringHolder suppMsgOut) throws RemoteException;

    public String WMLS_GetVersion() throws RemoteException;

    public short WMLS_UpdateInStore(String WMLtypeIn, String XMLin, String optionsIn, String capabilitiesIn, StringHolder suppMsgOut) throws RemoteException;
}
