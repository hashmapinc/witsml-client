package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.witsml.client.Client;

import java.rmi.RemoteException;

public abstract class AbstractRequestTracker<T> {

    private String wellId;
    private String wellboreId;
    private String objectId;
    private String query;
    private String capabilitesIn;
    private String optionsIn;
    private WitsmlVersion version;

    public String getWellId() {
        return wellId;
    }

    public void setWellId(String wellId) {
        this.wellId = wellId;
    }

    public String getWellboreId() {
        return wellboreId;
    }

    public void setWellboreId(String wellboreId) {
        this.wellboreId = wellboreId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCapabilitiesIn(){
        return capabilitesIn;
    }

    public void setCapabilitesIn(String capabilitesIn){
        this.capabilitesIn = capabilitesIn;
    }

    public String getOptionsIn(){
        return optionsIn;
    }

    public void setOptionsIn(String optionsIn){
        this.optionsIn = optionsIn;
    }

    public WitsmlVersion getVersion(){
        return version;
    }

    public void setVersion(WitsmlVersion version){
        this.version = version;
    }

    public abstract void initalize(WitsmlClient witsmlClient, String wellId, String wellboreId);

    public abstract T ExecuteRequest() throws RemoteException;

}
