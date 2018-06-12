package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.v1311.*;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjOpsReports;
import com.hashmapinc.tempus.WitsmlObjects.v1411.*;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjBhaRuns;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjCementJobs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjConvCores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjFluidsReports;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjFormationMarkers;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjMessages;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjMudLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjRigs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjRisks;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSidewallCores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSurveyPrograms;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTargets;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTrajectorys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTubulars;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWbGeometrys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWellbores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWells;
import com.hashmapinc.tempus.witsml.client.WitsmlQuery;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    String getObjectQuery(String objectType) throws IOException;

    WitsmlResponse getObjectData(WitsmlQuery witsmlQuery) throws FileNotFoundException, RemoteException;

    ObjWells getWellsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjWellbores getWellboresForWellAsObj(WitsmlQuery witsmlQuery) throws Exception;

    WitsmlResponse executeObjectQuery(String objectType, String query, String optionsIn, String capabilitiesIn) throws RemoteException;

    ObjLogs getLogsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjMudLogs getMudLogsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjTrajectorys getTrajectorysAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjBhaRuns getBhaRunsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjCementJobs getCementJobsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjConvCores getConvCoresAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjDtsInstalledSystems getDtsInstalledSystemsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjFluidsReports getFluidsReportsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjFormationMarkers getFormationMarkersAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjMessages getMessagesAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjOpsReports getOpsReportsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjRigs getRigsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjRisks getRisksAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjSidewallCores getSideWallCoresAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjSurveyPrograms getSurveyProgramsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjTargets getTargetsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjTubulars getTubularsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjWbGeometrys getWbGeometrysAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjAttachments getAttachmentsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjChangeLogs getChangeLogsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjDrillReports getDrillReportsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjObjectGroups getObjectGroupsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjStimJobs getStimJobsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjRealtimes getRealtimesAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjWellLogs getWellLogsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjDtsMeasurements getDtsMeasurementsAsObj(WitsmlQuery witsmlQuery) throws Exception;

    ObjTrajectoryStations getTrajectoryStationsAsObj(WitsmlQuery witsmlQuery) throws Exception;
}
