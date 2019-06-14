package com.hashmapinc.tempus.witsml.example;

import com.hashmapinc.tempus.WitsmlObjects.v1311.*;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsGeologyInterval;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsLogData;
import com.hashmapinc.tempus.WitsmlObjects.v1411.CsTrajectoryStation;
import com.hashmapinc.tempus.WitsmlObjects.v1411.*;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjBhaRuns;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjCementJobs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjConvCores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjFluidsReports;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjFormationMarkers;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjMessages;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjMudLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjOpsReports;
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
import com.hashmapinc.tempus.witsml.api.LogRequestTracker;
import com.hashmapinc.tempus.witsml.api.MudlogRequestTracker;
import com.hashmapinc.tempus.witsml.api.TrajectoryRequestTracker;
import com.hashmapinc.tempus.witsml.api.WitsmlVersion;
import com.hashmapinc.tempus.witsml.client.Client;
import com.hashmapinc.tempus.witsml.client.WitsmlQuery;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
            System.out.println(prettyPrint(c.getCapabilities()));
        }
        catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
            System.out.println("Error executing get capabilities: " + e.getMessage());
        }

        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.includeElement("statusWell");
        witsmlQuery.setObjectType("well");
        ObjWells wells = null;
        try {
            wells = c.getWellsAsObj(witsmlQuery);
        } catch (FileNotFoundException | RemoteException | JAXBException e) {
            System.out.println("Error executing get wells as obj: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wells != null) {
            System.out.println("Found " + wells.getWell().size() + " wells.");
            wells.getWell().forEach(well -> {
                if (well.getUid().equals("HM_01e30d7b-7d06-4cc4-afd6-39e06b1a68c1")) {
                    System.out.println("Well Name: " + well.getName());
                    System.out.println("Well Legal: " + well.getNameLegal());
                    System.out.println("Well Id : " + well.getUid());
                    System.out.println("Well Status : " + well.getStatusWell());
                    PrintWellbores(c, well.getUid());
                }
            });
        }
    }

    private static void PrintWellbores(Client c, String wellId){
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("wellbore", "uidWell", wellId);
        witsmlQuery.setObjectType("wellbore");
        try {
            ObjWellbores wellbores = c.getWellboresForWellAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setBulkData(false);
        witsmlQuery.setObjectType("log");
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.includeElement("indexType");
        witsmlQuery.addAttributeConstraint("log","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("log", "uidWellbore", wellboreId);
        try {
            ObjLogs logs = c.getLogsAsObj(witsmlQuery);
            System.out.println("Found " + logs.getLog().size() + " Logs");
            logs.getLog().forEach(log -> {
                if (log.getUid().equals("L-12345")) {
                    System.out.println("Log Name: " + log.getName());
                    System.out.println("Log Id: " + log.getUid());
                    System.out.println("Log Index Type: " + log.getIndexType());
                    PrintLogRequest(c, wellId, wellboreId, log.getUid());
                }
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
        timeCountMap.put(0, 3);
//        timeCountMap.put(20, 2);
//        timeCountMap.put(40, 1);
//        timeCountMap.put(3600, 2);

        LogRequestTracker tracker = new LogRequestTracker();
        tracker.setVersion(WitsmlVersion.VERSION_1411);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setBulkData(false);
        witsmlQuery.setObjectType("mudLog");
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("mudLog","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("mudLog", "uidWellbore", wellboreId);
        try {
            ObjMudLogs mudLogs = c.getMudLogsAsObj(witsmlQuery);
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
                    String lastGeologyIntervalUid = geologyIntervals.get(geologyIntervals.size() - 1).getUid();
                    if (hashCodeGeologyInvervalUid != lastGeologyIntervalUid.hashCode()) {
                        hashCodeGeologyInvervalUid = lastGeologyIntervalUid.hashCode();
                        count = (int)timeCountMap.get(0);
                    } else {
                        count--;
                    }
                    System.out.println("MUDLOG_HASHCODE : " + hashCodeGeologyInvervalUid);

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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setBulkData(false);
        witsmlQuery.setObjectType("trajectory");
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("trajectory","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("trajectory", "uidWellbore", wellboreId);
        try {
            ObjTrajectorys trajectorys = c.getTrajectorysAsObj(witsmlQuery);
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
                    String lastGeologIntervalUid = trajectoryStations.get(trajectoryStations.size() - 1).getUid();
                    if (hashCodeTrajectoryStationUid != lastGeologIntervalUid.hashCode()) {
                        hashCodeTrajectoryStationUid = lastGeologIntervalUid.hashCode();
                        count = (int)timeCountMap.get(0);
                    } else {
                        count--;
                    }
                    System.out.println("TRAJECTORY_HASHCODE : " + hashCodeTrajectoryStationUid);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("bhaRun","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("bhaRun", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("bhaRun");
        try {
            ObjBhaRuns bhaRuns = c.getBhaRunsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("cementJob","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("cementJob", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("cementJob");
        try {
            ObjCementJobs cementJobs = c.getCementJobsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("convCore","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("convCore", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("convCore");
        try {
            ObjConvCores convCores = c.getConvCoresAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("dtsInstalledSystem","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("dtsInstalledSystem", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("dtsInstalledSystem");
        try {
            ObjDtsInstalledSystems dtsInstalledSystems = c.getDtsInstalledSystemsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("fluidsReport","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("fluidsReport", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("fluidsReport");
        try {
            ObjFluidsReports fluidsReports = c.getFluidsReportsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("formationMarker","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("formationMarker", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("formationMarker");
        try {
            ObjFormationMarkers formationMarkers = c.getFormationMarkersAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("message","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("message", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("message");
        try {
            ObjMessages messages = c.getMessagesAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("opsReport","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("opsReport", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("opsReport");
        try {
            ObjOpsReports opsReports = c.getOpsReportsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("rig","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("rig", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("rig");
        try {
            ObjRigs rigs = c.getRigsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("risk","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("risk", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("risk");
        try {
            ObjRisks risks = c.getRisksAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("sidewallCore","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("sidewallCore", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("sidewallCore");
        try {
            ObjSidewallCores sidewallCores = c.getSideWallCoresAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("surveyProgram","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("surveyProgram", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("surveyProgram");
        try {
            ObjSurveyPrograms surveyPrograms = c.getSurveyProgramsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("target","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("target", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("target");
        try {
            ObjTargets targets = c.getTargetsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("tubular","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("tubular", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("tubular");
        try {
            ObjTubulars tubulars = c.getTubularsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("wbGeometry","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("wbGeometry", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("wbGeometry");
        try {
            ObjWbGeometrys wbGeometrys = c.getWbGeometrysAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("attachment","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("attachment", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("attachment");
        try {
            ObjAttachments attachments = c.getAttachmentsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("nameObject");
        witsmlQuery.addAttributeConstraint("changeLog","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("changeLog", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("changeLog");
        try {
            ObjChangeLogs changeLogs = c.getChangeLogsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("drillReport","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("drillReport", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("drillReport");
        try {
            ObjDrillReports drillReports = c.getDrillReportsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("objectGroup","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("objectGroup", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("objectGroup");
        try {
            ObjObjectGroups objObjectGroups = c.getObjectGroupsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("stimJob","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("stimJob","uidWellbore", wellboreId);
        witsmlQuery.setObjectType("stimJob");
        try {
            ObjStimJobs stimJobs = c.getStimJobsAsObj(witsmlQuery);
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
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("realtime","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("realtime", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("realtime");
        try {
            ObjRealtimes realtimes = c.getRealtimesAsObj(witsmlQuery);
            System.out.println("Found " + realtimes.getRealtime().size() + " Realtimes");
            realtimes.getRealtime().forEach(realtime -> {
                System.out.println("Realtimes Id : " + realtime.getIdSub());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintWellLogs(Client c, String wellId, String wellboreId) {
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("wellLog","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("wellLog", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("wellLog");
        try {
            ObjWellLogs wellLogs = c.getWellLogsAsObj(witsmlQuery);
            System.out.println("Found " + wellLogs.getWellLog().size() + " WellLogs");
            wellLogs.getWellLog().forEach(wellLog -> {
                System.out.println("WellLogs Id : " + wellLog.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintDtsMeasurements(Client c, String wellId, String wellboreId) {
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("dtsMeasurement","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("dtsMeasurement", "uidWellbore", wellboreId);
        witsmlQuery.setObjectType("dtsMeasurement");
        try {
            ObjDtsMeasurements dtsMeasurements = c.getDtsMeasurementsAsObj(witsmlQuery);
            System.out.println("Found " + dtsMeasurements.getDtsMeasurement().size() + " DtsMeasurements");
            dtsMeasurements.getDtsMeasurement().forEach(dtsMeasurement -> {
                System.out.println("DtsMeasurements Id : " + dtsMeasurement.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintTrajectoryStations(Client c, String wellId, String wellboreId, String trajectoryId) {
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.includeElement("uid");
        witsmlQuery.includeElement("name");
        witsmlQuery.addAttributeConstraint("trajectoryStation","uidWell", wellId);
        witsmlQuery.addAttributeConstraint("trajectoryStation", "uidWellbore", wellboreId);
        witsmlQuery.addAttributeConstraint("trajectoryStation", "uidTrajectory", trajectoryId);
        witsmlQuery.setObjectType("trajectoryStation");
        try {
            ObjTrajectoryStations trajectoryStations = c.getTrajectoryStationsAsObj(witsmlQuery);
            System.out.println("Found " + trajectoryStations.getTrajectoryStation().size() + " TrajectoryStations");
            trajectoryStations.getTrajectoryStation().forEach(trajectoryStation -> {
                System.out.println("TrajectoryStations Id : " + trajectoryStation.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String prettyPrint(String xml) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(new StringWriter());
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xml)));
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        return result.getWriter().toString();
    }
}
