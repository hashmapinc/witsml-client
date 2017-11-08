package com.hashmapinc.tempus.witsml.api;

public interface WitsmlResponse {

    public String getXmlOut();

    public void setXmlOut(String xmlOut);

    public String getSuppMsgOut();

    public void setSuppMsgOut(String suppMsgOut);

    public short getResponseCode();

    public void setResponseCode(short responseCode);
}
