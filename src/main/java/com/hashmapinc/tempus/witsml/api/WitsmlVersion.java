package com.hashmapinc.tempus.witsml.api;

//public enum WitsmlVersion { VERSION_1311, VERSION_1411 }

public enum WitsmlVersion {
    VERSION_1311 ("1.3.1.1"),
    VERSION_1411 ("1.4.1.1");

    private final String version;

    private WitsmlVersion(String v) {
        version = v;
    }

    public String toString() {
        return this.version;
    }
}
