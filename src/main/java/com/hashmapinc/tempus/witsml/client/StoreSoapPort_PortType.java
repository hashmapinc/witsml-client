package com.hashmapinc.tempus.witsml.client;

import com.hashmapinc.tempus.witsml.message._120.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StoreSoapPort_PortType extends Remote {

    WMLSGetFromStoreResponse wMLS_GetFromStore(WMLSGetFromStore wMLS_GetFromStore) throws RemoteException;

    WMLSGetBaseMsgResponse wMLS_GetBaseMsg(WMLSGetBaseMsg wMLS_GetBaseMsg) throws RemoteException;

    WMLSGetVersionResponse wMLS_GetVersion(WMLSGetVersion wMLS_GetVersion) throws RemoteException;

    WMLSDeleteFromStoreResponse wMLS_DeleteFromStore(WMLSDeleteFromStore wMLS_DeleteFromStore) throws RemoteException;

    WMLSGetCapResponse wMLS_GetCap(WMLSGetCap wMLS_GetCap) throws RemoteException;

    WMLSAddToStoreResponse wMLS_AddToStore(WMLSAddToStore wMLS_AddToStore) throws RemoteException;

    WMLSUpdateInStoreResponse wMLS_UpdateInStore(WMLSUpdateInStore wMLS_UpdateInStore) throws RemoteException;
}
