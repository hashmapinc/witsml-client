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

    String getMudLogs(String wellId, String wellboreId) throws Exception;

    String getTrajectorys(String wellId, String wellboreId) throws Exception;

    String getBhaRuns(String wellId, String wellboreId) throws Exception;

    String getCementJobs(String wellId, String wellboreId) throws Exception;

    String getConvCores(String wellId, String wellboreId) throws Exception;

    String getDtsInstalledSystems(String wellId, String wellboreId) throws Exception;

    String getFluidsReports(String wellId, String wellboreId) throws Exception;

    String getFormationMarkers(String wellId, String wellboreId) throws Exception;

    String getMessages(String wellId, String wellboreId) throws Exception;

    String getOpsReports(String wellId, String wellboreId) throws Exception;

    String getRigs(String wellId, String wellboreId) throws Exception;

    String getRisks(String wellId, String wellboreId) throws Exception;

    String getSideWallCores(String wellId, String wellboreId) throws Exception;

    String getSurveyPrograms(String wellId, String wellboreId) throws Exception;

    String getTargets(String wellId, String wellboreId) throws Exception;

    String getTubulars(String wellId, String wellboreId) throws Exception;

    String getWbGeometrys(String wellId, String wellboreId) throws Exception;

    String getAttachments(String wellId, String wellboreId) throws Exception;

    String getChangeLogs(String wellId, String wellboreId) throws Exception;

    String getObjectGroups(String wellId, String wellboreId) throws Exception;

    String getStimJobs(String wellId, String wellboreId) throws Exception;

    String getDrillReports(String wellId, String wellboreId) throws Exception;

    String getRealtimes(String wellId, String wellboreId) throws Exception;

    String getWellLogs(String wellId, String wellboreId) throws Exception;

    String getDtsMeasurements(String wellId, String wellboreId) throws Exception;

    String getTrajectoryStations(String wellId, String wellboreId, String trajectoryId) throws Exception;

    String executeLogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException;

    String executeMudlogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException;

    String executeTrajectoryQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException;

    ObjLogs getLogMetadataAsObj(String wellId, String wellboreId) throws Exception;

    ObjMudLogs getMudLogsAsObj(String wellId, String wellboreId) throws Exception;

    ObjTrajectorys getTrajectorysAsObj(String wellId, String wellboreId) throws Exception;

    ObjBhaRuns getBhaRunsAsObj(String wellId, String wellboreId) throws Exception;

    ObjCementJobs getCementJobsAsObj(String wellId, String wellboreId) throws Exception;

    ObjConvCores getConvCoresAsObj(String wellId, String wellboreId) throws Exception;

    ObjDtsInstalledSystems getDtsInstalledSystemsAsObj(String wellId, String wellboreId) throws Exception;

    ObjFluidsReports getFluidsReportsAsObj(String wellId, String wellboreId) throws Exception;

    ObjFormationMarkers getFormationMarkersAsObj(String wellId, String wellboreId) throws Exception;

    ObjMessages getMessagesAsObj(String wellId, String wellboreId) throws Exception;

    ObjOpsReports getOpsReportsAsObj(String wellId, String wellboreId) throws Exception;

    ObjRigs getRigsAsObj(String wellId, String wellboreId) throws Exception;

    ObjRisks getRisksAsObj(String wellId, String wellboreId) throws Exception;

    ObjSidewallCores getSideWallCoresAsObj(String wellId, String wellboreId) throws Exception;

    ObjSurveyPrograms getSurveyProgramsAsObj(String wellId, String wellboreId) throws Exception;

    ObjTargets getTargetsAsObj(String wellId, String wellboreId) throws Exception;

    ObjTubulars getTubularsAsObj(String wellId, String wellboreId) throws Exception;

    ObjWbGeometrys getWbGeometrysAsObj(String wellId, String wellboreId) throws Exception;

    ObjAttachments getAttachmentsAsObj(String wellId, String wellboreId) throws Exception;

    ObjChangeLogs getChangeLogsAsObj(String wellId, String wellboreId) throws Exception;

    ObjDrillReports getDrillReportsAsObj(String wellId, String wellboreId) throws Exception;

    ObjObjectGroups getObjectGroupsAsObj(String wellId, String wellboreId) throws Exception;

    ObjStimJobs getStimJobsAsObj(String wellId, String wellboreId) throws Exception;

    ObjRealtimes getRealtimesAsObj(String wellId, String wellboreId) throws Exception;

    ObjWellLogs getWellLogsAsObj(String wellId, String wellboreId) throws Exception;

    ObjDtsMeasurements getDtsMeasurementsAsObj(String wellId, String wellboreId) throws Exception;

    ObjTrajectoryStations getTrajectoryStationsAsObj(String wellId, String wellboreId, String trajectoryId) throws Exception;
}
