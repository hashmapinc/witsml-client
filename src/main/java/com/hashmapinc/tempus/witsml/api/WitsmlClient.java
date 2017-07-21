package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWellbores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWells;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

public interface WitsmlClient {
    String getUrl();

    void setUrl(String url);

    String getUserName();

    void setUserName(String userName);

    void setPassword(String password);

    void setVersion(WitsmlVersion version);

    String getVersion();

    void connect();

    String getCapabilities() throws RemoteException;

    String getWells() throws FileNotFoundException, RemoteException, Exception;

    ObjWells getWellsAsObj() throws Exception;

    String getWellboresForWell(String wellId) throws FileNotFoundException, RemoteException, Exception;

    ObjWellbores getWellboresForWellAsObj(String wellId) throws Exception;

    String getLogMetadata(String wellId, String wellboreId) throws Exception;

    String executeLogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException;

    ObjLogs getLogMetadataAsObj(String wellId, String wellboreId) throws Exception;
}
