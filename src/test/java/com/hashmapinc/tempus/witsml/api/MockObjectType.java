package com.hashmapinc.tempus.witsml.api;

public enum MockObjectType {

    WELL               ("well.xml"),
    WELLBORE           ("wellbore.xml"),
    LOG                ("log.xml"),
    MUDLOG             ("mudLog.xml"),
    TRAJECTORY         ("trajectory.xml"),
    BHARUN             ("bhaRun.xml"),
    CEMENTJOB          ("cementJob.xml"),
    CONVCORE           ("convCore.xml"),
    FLUIDSREPORT       ("fluidsReport.xml"),
    FORMATIONMARKER    ("formationMarker.xml"),
    MESSAGE            ("message.xml"),
    OPSREPORT          ("opsReport.xml"),
    RIG                ("rig.xml"),
    RISK               ("risk.xml"),
    SIDEWALLCORE       ("sidewallCore.xml"),
    SURVEYPROGRAM      ("surveyProgram.xml"),
    TARGET             ("target.xml"),
    TUBULAR            ("tubular.xml"),
    WBGEOMETRY         ("wbGeometry.xml"),
    ATTACHMENT         ("attachment.xml"),
    DRILLREPORT        ("drillReport.xml"),
    CHANGELOG          ("changeLog.xml"),
    OBJECTGROUP        ("objectGroup.xml"),
    STIMJOB            ("stimJob.xml"),
    DTSINSTALLEDSYSTEM ("dtsInstalledSystem.xml"),
    REALTIME           ("realtime.xml"),
    DTSMEASUREMENT     ("dtsMeasurement.xml"),
    WELLLOG            ("wellLog.xml"),
    TRAJECTORYSTATION  ("trajectoryStation.xml");

    private final String objectType;

    private MockObjectType(String v) {
        objectType = v;
    }

    public String toString() {
        return this.objectType;
    }

}
