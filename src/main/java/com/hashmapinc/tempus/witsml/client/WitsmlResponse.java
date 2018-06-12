package com.hashmapinc.tempus.witsml.client;

public class WitsmlResponse implements com.hashmapinc.tempus.witsml.api.WitsmlResponse {
    private String xmlOut;
    private String suppMsgOut;
    private short responseCode;

    public WitsmlResponse(String xmlOut, String suppMsgOut, short responseCode) {
        this.xmlOut = xmlOut;
        this.suppMsgOut = suppMsgOut;
        this.responseCode = responseCode;
    }

    public String getXmlOut() {
        return xmlOut;
    }

    public void setXmlOut(String xmlOut) {
        this.xmlOut = xmlOut;
    }

    public String getSuppMsgOut() {
        return suppMsgOut;
    }

    public void setSuppMsgOut(String suppMsgOut) {
        this.suppMsgOut = suppMsgOut;
    }

    public short getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(short responseCode) {
        this.responseCode = responseCode;
    }
}
