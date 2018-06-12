package com.hashmapinc.tempus.witsml.api;

public enum ObjectType {

    WELL               ("GetWells.xml"),
    WELLBORE           ("GetWellbores.xml"),
    LOG                ("GetLogs.xml"),
    MUDLOG             ("GetMudLogs.xml"),
    TRAJECTORY         ("GetTrajectorys.xml"),
    BHARUN             ("GetBhaRuns.xml"),
    CEMENTJOB          ("GetCementJobs.xml"),
    CONVCORE           ("GetConvCore.xml"),
    FLUIDSREPORT       ("GetFluidsReports.xml"),
    FORMATIONMARKER    ("GetFormationMarker.xml"),
    MESSAGE            ("GetMessages.xml"),
    OPSREPORT          ("GetOpsReports.xml"),
    RIG                ("GetRigs.xml"),
    RISK               ("GetRisks.xml"),
    SIDEWALLCORE       ("GetSideWallCores.xml"),
    SURVEYPROGRAM      ("GetSurveyPrograms.xml"),
    TARGET             ("GetTargets.xml"),
    TUBULAR            ("GetTubulars.xml"),
    WBGEOMETRY         ("GetWbGeometrys.xml"),
    ATTACHMENT         ("GetAttachments.xml"),
    DRILLREPORT        ("GetDrillReports.xml"),
    CHANGELOG          ("GetChangeLogs.xml"),
    OBJECTGROUP        ("GetObjectGroups.xml"),
    STIMJOB            ("GetStimJobs.xml"),
    DTSINSTALLEDSYSTEM ("GetDtsInstalledSystems.xml"),
    REALTIME           ("GetRealTimes.xml"),
    DTSMEASUREMENT     ("GetDtsMeasurements.xml"),
    WELLLOG            ("GetWellLogs.xml"),
    TRAJECTORYSTATION  ("GetTrajectoryStations.xml");

    private final String objectType;

    private ObjectType(String v) {
        objectType = v;
    }

    public String toString() {
        return this.objectType;
    }

}
