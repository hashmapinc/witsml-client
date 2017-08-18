package com.hashmapinc.tempus.witsml.example;

import com.hashmapinc.tempus.WitsmlObjects.v1311.*;
import com.hashmapinc.tempus.WitsmlObjects.v1411.*;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsGeologyInterval;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsTrajectoryStation;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjRisks;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsLogData;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjOpsReports;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjFluidsReports;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjBhaRuns;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjCementJobs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjConvCores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjFormationMarkers;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjMessages;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjMudLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjRigs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSidewallCores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSurveyPrograms;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTargets;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTrajectorys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTubulars;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWbGeometrys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWellbores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWells;
import com.hashmapinc.tempus.witsml.api.LogRequestTracker;
import com.hashmapinc.tempus.witsml.api.MudlogRequestTracker;
import com.hashmapinc.tempus.witsml.api.TrajectoryRequestTracker;
import com.hashmapinc.tempus.witsml.client.Client;
import com.hashmapinc.tempus.witsml.api.WitsmlVersion;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MyWitsmlClient {

    public static void main(String args[]){
        Client c = new Client(args[0]);
        c.setUserName(args[1]);
        c.setPassword(args[2]);
        c.setVersion(WitsmlVersion.VERSION_1411);
        c.connect();
        try {
            System.out.println("Supported Caps:");
            System.out.println(prettyPrint(c.getCapabilities(), true));
        }
        catch (SAXException | ParserConfigurationException | IOException e) {
            System.out.println("Error executing get capabilities: " + e.getMessage());
        }
        ObjWells wells = null;
        try {
            wells = c.getWellsAsObj();
        } catch (FileNotFoundException | RemoteException | JAXBException e) {
            System.out.println("Error executing get wells as obj: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wells != null) {
            System.out.println("Found " + wells.getWell().size() + " wells.");
            wells.getWell().forEach(well -> {
                System.out.println("Well Name: " + well.getName());
                System.out.println("Well Legal: " + well.getNameLegal());
                System.out.println("Well Id : " + well.getUid());
                PrintWellbores(c, well.getUid());
            });
        }
    }

    private static void PrintWellbores(Client c, String wellId){
        try {
            ObjWellbores wellbores = c.getWellboresForWellAsObj(wellId);
            System.out.println("Found " + wellbores.getWellbore().size() + " WellBore");
            wellbores.getWellbore().forEach(wellbore -> {
                System.out.println("Wellbore Name: " + wellbore.getName());
                System.out.println("Wellbore Id: " + wellbore.getUid());

                /***********COMMON**********/
                PrintLogs(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintMudLogs(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintTrajectories(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintBhaRuns(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintCementJobs(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintConvCores(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintFluidsReports(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintFormationMarker(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintMessages(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintOpsReports(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintRigs(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintRisks(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintSideWallCores(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintSurveyPrograms(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintTargets(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintTubulars(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintWbGeometrys(c, wellbore.getUidWell(), wellbore.getUid());

                /************1.4.1.1***********/
//                PrintAttachments(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintChangeLogs(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintDrillReports(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintObjectGroups(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintStimJobs(c, wellbore.getUidWell(), wellbore.getUid());


                /*************1.3.1.1*************/
//                PrintDtsInstalledSystems(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintDtsMeasurements(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintRealtimes(c, wellbore.getUidWell(), wellbore.getUid());
//                PrintWellLogs(c, wellbore.getUidWell(), wellbore.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintLogs(Client c, String wellId, String wellboreId){
        try {
            ObjLogs logs = c.getLogMetadataAsObj(wellId, wellboreId);
            System.out.println("Found " + logs.getLog().size() + " Logs");
            logs.getLog().forEach(log -> {
                System.out.println("Log Name: " + log.getName());
                System.out.println("Log Id: " + log.getUid());
//                PrintLogRequest(c, wellId, wellboreId, log.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintLogRequest(Client c, String wellId, String wellboreId, String logId) {
        int count = 0;
        int sleepInterval = 0;
        Map<Integer, Integer> timeCountMap = new TreeMap<Integer, Integer>();
        /*<TIME_SECONDS, COUNT>*/
        timeCountMap.put(0, 5);
        timeCountMap.put(20, 2);
        timeCountMap.put(40, 1);
//        timeCountMap.put(3600, 2);

        LogRequestTracker tracker = new LogRequestTracker();
        tracker.setVersion(WitsmlVersion.VERSION_1311);
        tracker.setLogId(logId);
        tracker.initalize(c, wellId, wellboreId);

        ObjLogs logs = null;
        long hashCodeLogData = 0;
        List<CsLogData> logData;
        for(Map.Entry interval:timeCountMap.entrySet()) {
            sleepInterval = (int)interval.getKey();
            count = (int)interval.getValue();
            while (count != 0) {
                logs = tracker.ExecuteRequest();
                if (logs == null) {
                    break;
                }
                for (int i = 0; i < logs.getLog().size(); i++) {
                    logData = logs.getLog().get(i).getLogData();
                    for (CsLogData data : logData) {
                        if (hashCodeLogData != data.getData().hashCode()) {
                            hashCodeLogData = data.getData().hashCode();
                            count = (int)timeCountMap.get(0);
                        } else {
                            count--;
                        }
                        System.out.println("LOG_HASHCODE : " + hashCodeLogData);
                    }
                }
                System.out.println("Count : " + count + " Sleep : " + sleepInterval);
                try {
                    TimeUnit.SECONDS.sleep(sleepInterval);
                } catch (InterruptedException ex ){
                    System.out.println("Exception in Sleep : " + ex);
                }
            }
        }
    }

    private static void PrintMudLogs(Client c, String wellId, String wellboreId) {
        try {
            ObjMudLogs mudLogs = c.getMudLogsAsObj(wellId, wellboreId);
            System.out.println("Found " + mudLogs.getMudLog().size() + " MudLogs");
            mudLogs.getMudLog().forEach(mudLog -> {
                System.out.println("MudLog Name : " + mudLog.getName());
                System.out.println("MudLog Id : " + mudLog.getUid());
//                PrintMudLogRequest(c, wellId, wellboreId, mudLog.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintMudLogRequest(Client c, String wellId, String wellboreId, String mudLogId) {
        int count = 0;
        int sleepInterval = 0;
        Map<Integer, Integer> timeCountMap = new TreeMap<Integer, Integer>();
        /*<TIME_SECONDS, COUNT>*/
        timeCountMap.put(0, 5);
        timeCountMap.put(20, 2);
        timeCountMap.put(40, 1);
//        timeCountMap.put(3600, 2);

        MudlogRequestTracker tracker = new MudlogRequestTracker();
        tracker.setVersion(WitsmlVersion.VERSION_1311);
        tracker.setMudlogId(mudLogId);
        tracker.initalize(c, wellId, wellboreId);

        ObjMudLogs mudLogs = null;
        long hashCodeGeologyInvervalUid = 0;
        List<CsGeologyInterval> geologyIntervals;
        for(Map.Entry interval:timeCountMap.entrySet()) {
            sleepInterval = (int) interval.getKey();
            count = (int) interval.getValue();
            while (count != 0) {
                mudLogs = tracker.ExecuteRequest();
                if (mudLogs == null) {
                    break;
                }
                for (int i = 0; i < mudLogs.getMudLog().size(); i++) {
                    geologyIntervals = mudLogs.getMudLog().get(i).getGeologyInterval();
                    for (CsGeologyInterval geologyInterval : geologyIntervals) {
                        if (hashCodeGeologyInvervalUid != geologyInterval.getUid().hashCode()) {
                            hashCodeGeologyInvervalUid = geologyInterval.getUid().hashCode();
                            count = (int)timeCountMap.get(0);
                        } else {
                            count--;
                        }
                        System.out.println("MUDLOG_HASHCODE : " + hashCodeGeologyInvervalUid);
                    }
                }
                System.out.println("Count : " + count + " Sleep : " + sleepInterval);
                System.out.println();
                try {
                    TimeUnit.SECONDS.sleep(sleepInterval);
                } catch (InterruptedException ex ){
                    System.out.println("Exception in Sleep : " + ex);
                }
            }
        }
    }

    private static void PrintTrajectories(Client c, String wellId, String wellboreId) {
        try {
            ObjTrajectorys trajectorys = c.getTrajectorysAsObj(wellId, wellboreId);
            System.out.println("Found " + trajectorys.getTrajectory().size() + " Trajectory");
            trajectorys.getTrajectory().forEach(trajectory -> {
                System.out.println("Trajectory Name : " + trajectory.getName());
                System.out.println("Trajectory Id : " + trajectory.getUid());
//                PrintTrajectoryRequest(c, wellId, wellboreId, trajectory.getUid());

                /**********Only For 1.3.1.1 Version********/
//                PrintTrajectoryStations(c, wellId, wellboreId, trajectory.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintTrajectoryRequest(Client c, String wellId, String wellboreId, String trajectoryId) {
        int count = 0;
        int sleepInterval = 0;
        Map<Integer, Integer> timeCountMap = new TreeMap<Integer, Integer>();
        /*<TIME_SECONDS, COUNT>*/
        timeCountMap.put(0, 5);
        timeCountMap.put(20, 2);
        timeCountMap.put(40, 1);
//        timeCountMap.put(3600, 2);

        TrajectoryRequestTracker tracker = new TrajectoryRequestTracker();
        tracker.setVersion(WitsmlVersion.VERSION_1311);
        tracker.setTrajectoryId(trajectoryId);
        tracker.initalize(c, wellId, wellboreId);

        ObjTrajectorys trajectorys = null;
        long hashCodeTrajectoryStationUid = 0;
        List<CsTrajectoryStation> trajectoryStations;
        for(Map.Entry interval:timeCountMap.entrySet()) {
            sleepInterval = (int) interval.getKey();
            count = (int) interval.getValue();
            while (count != 0) {
                trajectorys = tracker.ExecuteRequest();
                if (trajectorys == null) {
                    break;
                }
                for (int i = 0; i < trajectorys.getTrajectory().size(); i++) {
                    trajectoryStations = trajectorys.getTrajectory().get(i).getTrajectoryStation();
                    for (CsTrajectoryStation trajectoryStation : trajectoryStations) {
                        if (hashCodeTrajectoryStationUid != trajectoryStation.getUid().hashCode()) {
                            hashCodeTrajectoryStationUid = trajectoryStation.getUid().hashCode();
                            count = (int)timeCountMap.get(0);
                        } else {
                            count--;
                        }
                        System.out.println("TRAJECTORY_HASHCODE : " + hashCodeTrajectoryStationUid);
                    }
                }
                System.out.println("Count : " + count + " Sleep : " + sleepInterval);
                System.out.println();
                try {
                    TimeUnit.SECONDS.sleep(sleepInterval);
                } catch (InterruptedException ex ){
                    System.out.println("Exception in Sleep : " + ex);
                }
            }
        }
    }

    private static void PrintBhaRuns(Client c, String wellId, String wellboreId) {
        try {
            ObjBhaRuns bhaRuns = c.getBhaRunsAsObj(wellId, wellboreId);
            System.out.println("Found " + bhaRuns.getBhaRun().size() + " BhaRuns");
            bhaRuns.getBhaRun().forEach(bhaRun -> {
                System.out.println("BhaRun Name : " + bhaRun.getName());
                System.out.println("BhaRun Id : " + bhaRun.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintCementJobs(Client c, String wellId, String wellboreId) {
        try {
            ObjCementJobs cementJobs = c.getCementJobsAsObj(wellId, wellboreId);
            System.out.println("Found " + cementJobs.getCementJob().size() + " CementJobs");
            cementJobs.getCementJob().forEach(cementJob -> {
                System.out.println("CementJob Name : " + cementJob.getName());
                System.out.println("CementJob Id : " + cementJob.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintConvCores(Client c, String wellId, String wellboreId) {
        try {
            ObjConvCores convCores = c.getConvCoresAsObj(wellId, wellboreId);
            System.out.println("Found " + convCores.getConvCore().size() + " ConvCores");
            convCores.getConvCore().forEach(convCore -> {
                System.out.println("ConvCore Name : " + convCore.getName());
                System.out.println("ConvCore Id : " + convCore.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintDtsInstalledSystems(Client c, String wellId, String wellboreId) {
        try {
            ObjDtsInstalledSystems dtsInstalledSystems = c.getDtsInstalledSystemsAsObj(wellId, wellboreId);
            System.out.println("Found " + dtsInstalledSystems.getDtsInstalledSystem().size() + " DtsInstalledSystems");
            dtsInstalledSystems.getDtsInstalledSystem().forEach(dtsInstalledSystem -> {
                System.out.println("DtsInstalledSystem Name : " + dtsInstalledSystem.getName());
                System.out.println("DtsInstalledSystem Id : " + dtsInstalledSystem.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintFluidsReports(Client c, String wellId, String wellboreId) {
        try {
            ObjFluidsReports fluidsReports = c.getFluidsReportsAsObj(wellId, wellboreId);
            System.out.println("Found " + fluidsReports.getFluidsReport().size() + " FluidsReports");
            fluidsReports.getFluidsReport().forEach(fluidsReport -> {
                System.out.println("FluidsReports Name : " + fluidsReport.getName());
                System.out.println("FluidsReports Id : " + fluidsReport.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintFormationMarker(Client c, String wellId, String wellboreId) {
        try {
            ObjFormationMarkers formationMarkers = c.getFormationMarkersAsObj(wellId, wellboreId);
            System.out.println("Found " + formationMarkers.getFormationMarker().size() + " FormationMarker");
            formationMarkers.getFormationMarker().forEach(formationMarker -> {
                System.out.println("FormationMarker Name : " + formationMarker.getName());
                System.out.println("FormationMarker Id : " + formationMarker.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintMessages(Client c, String wellId, String wellboreId) {
        try {
            ObjMessages messages = c.getMessagesAsObj(wellId, wellboreId);
            System.out.println("Found " + messages.getMessage().size() + " Messages");
            messages.getMessage().forEach(message -> {
                System.out.println("Message Name : " + message.getName());
                System.out.println("Message Id : " + message.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintOpsReports(Client c, String wellId, String wellboreId) {
        try {
            ObjOpsReports opsReports = c.getOpsReportsAsObj(wellId, wellboreId);
            System.out.println("Found " + opsReports.getOpsReport().size() + " OpsReports");
            opsReports.getOpsReport().forEach(opsReport -> {
                System.out.println("OpsReports Name : " + opsReport.getName());
                System.out.println("OpsReports Id : " + opsReport.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintRigs(Client c, String wellId, String wellboreId) {
        try {
            ObjRigs rigs = c.getRigsAsObj(wellId, wellboreId);
            System.out.println("Found " + rigs.getRig().size() + " Rigs");
            rigs.getRig().forEach(rig -> {
                System.out.println("Rigs Name : " + rig.getName());
                System.out.println("Rigs Id : " + rig.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintRisks(Client c, String wellId, String wellboreId) {
        try {
            ObjRisks risks = c.getRisksAsObj(wellId, wellboreId);
            System.out.println("Found " + risks.getRisk().size() + " Risks");
            risks.getRisk().forEach(risk -> {
                System.out.println("Risk Name : " + risk.getName());
                System.out.println("Risk Id : " + risk.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintSideWallCores(Client c, String wellId, String wellboreId) {
        try {
            ObjSidewallCores sidewallCores = c.getSideWallCoresAsObj(wellId, wellboreId);
            System.out.println("Found " + sidewallCores.getSidewallCore().size() + " SideWallCores");
            sidewallCores.getSidewallCore().forEach(sidewallCore -> {
                System.out.println("SideWallCore Name : " + sidewallCore.getName());
                System.out.println("SideWallCore Id : " + sidewallCore.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintSurveyPrograms(Client c, String wellId, String wellboreId) {
        try {
            ObjSurveyPrograms surveyPrograms = c.getSurveyProgramsAsObj(wellId, wellboreId);
            System.out.println("Found " + surveyPrograms.getSurveyProgram().size() + " SurveyPrograms");
            surveyPrograms.getSurveyProgram().forEach(surveyProgram -> {
                System.out.println("SurveyProgram Name : " + surveyProgram.getName());
                System.out.println("SurveyProgram Id : " + surveyProgram.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintTargets(Client c, String wellId, String wellboreId) {
        try {
            ObjTargets targets = c.getTargetsAsObj(wellId, wellboreId);
            System.out.println("Found " + targets.getTarget().size() + " Targets");
            targets.getTarget().forEach(target -> {
                System.out.println("Target Name : " + target.getName());
                System.out.println("Target Id : " + target.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintTubulars(Client c, String wellId, String wellboreId) {
        try {
            ObjTubulars tubulars = c.getTubularsAsObj(wellId, wellboreId);
            System.out.println("Found " + tubulars.getTubular().size() + " Tubulars");
            tubulars.getTubular().forEach(tubular -> {
                System.out.println("Tubulars Name : " + tubular.getName());
                System.out.println("Tubulars Id : " + tubular.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintWbGeometrys(Client c, String wellId, String wellboreId) {
        try {
            ObjWbGeometrys wbGeometrys = c.getWbGeometrysAsObj(wellId, wellboreId);
            System.out.println("Found " + wbGeometrys.getWbGeometry().size() + " WbGeometrys");
            wbGeometrys.getWbGeometry().forEach(geometry -> {
                System.out.println("WbGeometry Name : " + geometry.getName());
                System.out.println("WbGeometry Id : " + geometry.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintAttachments(Client c, String wellId, String wellboreId) {
        try {
            ObjAttachments attachments = c.getAttachmentsAsObj(wellId, wellboreId);
            System.out.println("Found " + attachments.getAttachment().size() + " Attachments");
            attachments.getAttachment().forEach(attachment -> {
                System.out.println("Attachment Name : " + attachment.getName());
                System.out.println("Attachment Id : " + attachment.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintChangeLogs(Client c, String wellId, String wellboreId) {
        try {
            ObjChangeLogs changeLogs = c.getChangeLogsAsObj(wellId, wellboreId);
            System.out.println("Found " + changeLogs.getChangeLog().size() + " ChangeLogs");
            changeLogs.getChangeLog().forEach(changeLog -> {
                System.out.println("ChangeLogs Name : " + changeLog.getNameObject());
                System.out.println("ChangeLogs Id : " + changeLog.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintDrillReports(Client c, String wellId, String wellboreId) {
        try {
            ObjDrillReports drillReports = c.getDrillReportsAsObj(wellId, wellboreId);
            System.out.println("Found " + drillReports.getDrillReport().size() + " DrillReports");
            drillReports.getDrillReport().forEach(drillReport -> {
                System.out.println("DrillReport Name : " + drillReport.getName());
                System.out.println("DrillReport Id : " + drillReport.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintObjectGroups(Client c, String wellId, String wellboreId) {
        try {
            ObjObjectGroups objObjectGroups = c.getObjectGroupsAsObj(wellId, wellboreId);
            System.out.println("Found " + objObjectGroups.getObjectGroup().size() + " ObjectGroups");
            objObjectGroups.getObjectGroup().forEach(objectGroup -> {
                System.out.println("DrillReport Name : " + objectGroup.getName());
                System.out.println("DrillReport Id : " + objectGroup.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintStimJobs(Client c, String wellId, String wellboreId) {
        try {
            ObjStimJobs stimJobs = c.getStimJobsAsObj(wellId, wellboreId);
            System.out.println("Found " + stimJobs.getStimJob().size() + " StimJobs");
            stimJobs.getStimJob().forEach(stimJob -> {
                System.out.println("StimJob Name : " + stimJob.getName());
                System.out.println("StimJob Id : " + stimJob.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintRealtimes(Client c, String wellId, String wellboreId) {
        try {
            ObjRealtimes realtimes = c.getRealtimesAsObj(wellId, wellboreId);
            System.out.println("Found " + realtimes.getRealtime().size() + " Realtimes");
            realtimes.getRealtime().forEach(realtime -> {
                System.out.println("Realtimes Id : " + realtime.getIdSub());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintWellLogs(Client c, String wellId, String wellboreId) {
        try {
            ObjWellLogs wellLogs = c.getWellLogsAsObj(wellId, wellboreId);
            System.out.println("Found " + wellLogs.getWellLog().size() + " WellLogs");
            wellLogs.getWellLog().forEach(wellLog -> {
                System.out.println("WellLogs Id : " + wellLog.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintDtsMeasurements(Client c, String wellId, String wellboreId) {
        try {
            ObjDtsMeasurements dtsMeasurements = c.getDtsMeasurementsAsObj(wellId, wellboreId);
            System.out.println("Found " + dtsMeasurements.getDtsMeasurement().size() + " DtsMeasurements");
            dtsMeasurements.getDtsMeasurement().forEach(dtsMeasurement -> {
                System.out.println("DtsMeasurements Id : " + dtsMeasurement.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintTrajectoryStations(Client c, String wellId, String wellboreId, String trajectoryId) {
        try {
            ObjTrajectoryStations trajectoryStations = c.getTrajectoryStationsAsObj(wellId, wellboreId, trajectoryId);
            System.out.println("Found " + trajectoryStations.getTrajectoryStation().size() + " TrajectoryStations");
            trajectoryStations.getTrajectoryStation().forEach(trajectoryStation -> {
                System.out.println("TrajectoryStations Id : " + trajectoryStation.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String prettyPrint(String xml, Boolean omitXmlDeclaration) throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xml)));

        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        format.setIndent(2);
        format.setOmitXMLDeclaration(omitXmlDeclaration);
        format.setLineWidth(Integer.MAX_VALUE);
        Writer outxml = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(outxml, format);
        serializer.serialize(doc);

        return outxml.toString();

    }
}
