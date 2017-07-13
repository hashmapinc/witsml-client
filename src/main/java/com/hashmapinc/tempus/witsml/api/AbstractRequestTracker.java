package com.hashmapinc.tempus.witsml.api;

public abstract class AbstractRequestTracker<T> {

    private String wellId;
    private String wellboreId;
    private String objectId;
    private String query;

    public void setWellId(String wellId){
        this.wellId = wellId;
    }

    public String getWellId(){
        return wellId;
    }

    public void setWellboreId(String wellboreId){
        this.wellboreId = wellboreId;
    }

    public String getWellboreId() {
        return wellboreId;
    }

    public void setObjectId(String objectId){
        this.objectId = objectId;
    }

    public String getObjectId(){
        return objectId;
    }

    public String getQuery(){
        return query;
    }

    public abstract void initalize();

    public abstract T ExecuteRequest();

}
