package com.hashmapinc.tempus.witsml.api;

public interface WitsmlQuery {

    public String getObjectType();

    public void setObjectType(String objectType);

    public boolean isBulkData();

    public void setBulkData(boolean bulkData);

    public void includeElement(String element);

    public void excludeElement(String element);

    public void addElementConstraint(String element, Object value);

    public void addAttributeConstraint(String element, String attribute, Object value);

    public String apply(String xmlString);

    public String toString();
}
