package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import com.hashmapinc.tempus.WitsmlObjects.v1411.*;
import com.hashmapinc.tempus.witsml.client.Client;
import com.hashmapinc.tempus.witsml.client.WitsmlQuery;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class TestClientApi {

    MockStoreSoapBindingStub witsmlClient;
    Client client;
    WitsmlVersionTransformer transformer;

    @Before
    public void setUp() {
        witsmlClient = new MockStoreSoapBindingStub();
        client = new Client(witsmlClient, "https://test.test.com/witsml/witsml.asmx");
        try {
            transformer = new WitsmlVersionTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestGetVersion() {
        String supportedVersion = client.getVersion();

        assertNotNull(supportedVersion);
        assertEquals(supportedVersion, "1.3.1.1,1.4.1.1");
    }

    @Test
    public void TestGetCapabilities1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        String getCapabilities = null;
        try {
            getCapabilities = client.getCapabilities();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

        String capabilitites = getReturnData("1411/caps1411.xml");
        assertNotNull(getCapabilities);
        assertEquals(capabilitites, getCapabilities);
    }

    @Test
    public void TestGetCapabilities1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        String getCapabilities = null;
        try {
            getCapabilities = client.getCapabilities();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

        String capabilitites = getReturnData("1311/caps1311.xml");
        assertNotNull(getCapabilities);
        assertEquals(capabilitites, getCapabilities);
    }

    @Test
    public void TestGetWells1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjWells getWells = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("well");

        try {
            getWells = client.getWellsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String wellsXML = getReturnData("1311/well.xml");
        wellsXML = convertVersion(wellsXML , "1.3.1.1");
        ObjWells wells = null;
        try {
            wells = WitsmlMarshal.deserialize(wellsXML, ObjWells.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getWells);
        assertEquals(getWells.getWell().size(), wells.getWell().size());
        assertEquals(getWells.getWell().get(0).getUid(), wells.getWell().get(0).getUid());
    }

    @Test
    public void TestGetWells1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjWells getWells = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("well");

        try {
            getWells = client.getWellsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String wellsXML = getReturnData("1411/well.xml");
        ObjWells wells = null;
        try {
            wells = WitsmlMarshal.deserialize(wellsXML, ObjWells.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getWells);
        assertEquals(getWells.getWell().size(), wells.getWell().size());
        assertEquals(getWells.getWell().get(0).getUid(), wells.getWell().get(0).getUid());
    }

    @Test
    public void TestGetWellbore1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjWellbores getWellbores = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("wellbore");

        try {
            getWellbores = client.getWellboresForWellAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String wellsXML = getReturnData("1311/wellbore.xml");
        wellsXML = convertVersion(wellsXML , "1.3.1.1");
        ObjWellbores wellbores = null;
        try {
            wellbores = WitsmlMarshal.deserialize(wellsXML, ObjWellbores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getWellbores);
        assertEquals(getWellbores.getWellbore().size(), wellbores.getWellbore().size());
        assertEquals(getWellbores.getWellbore().get(0).getUid(), wellbores.getWellbore().get(0).getUid());
    }

    @Test
    public void TestGetWellbore1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjWellbores getWellbores = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("wellbore");

        try {
            getWellbores = client.getWellboresForWellAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String wellboresXML = getReturnData("1411/wellbore.xml");
        ObjWellbores wellbores = null;
        try {
            wellbores = WitsmlMarshal.deserialize(wellboresXML, ObjWellbores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getWellbores);
        assertEquals(getWellbores.getWellbore().size(), wellbores.getWellbore().size());
        assertEquals(getWellbores.getWellbore().get(0).getUid(), wellbores.getWellbore().get(0).getUid());
    }

    @Test
    public void TestGetLog1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjLogs getLogs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("log");

        try {
            getLogs = client.getLogsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String logsXML = getReturnData("1311/log.xml");
        logsXML = convertVersion(logsXML , "1.3.1.1");
        ObjLogs logs = null;
        try {
            logs = WitsmlMarshal.deserialize(logsXML, ObjLogs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getLogs);
        assertEquals(getLogs.getLog().size(), logs.getLog().size());
        assertEquals(getLogs.getLog().get(0).getUid(), logs.getLog().get(0).getUid());
    }

    @Test
    public void TestGetLog1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjLogs getLogs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("log");

        try {
            getLogs = client.getLogsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String logsXML = getReturnData("1411/log.xml");
        ObjLogs logs = null;
        try {
            logs = WitsmlMarshal.deserialize(logsXML, ObjLogs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getLogs);
        assertEquals(getLogs.getLog().size(), logs.getLog().size());
        assertEquals(getLogs.getLog().get(0).getUid(), logs.getLog().get(0).getUid());
    }

    @Test
    public void TestGetMudLog1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjMudLogs getMudLogs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("mudLog");

        try {
            getMudLogs = client.getMudLogsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String mudlogsXML = getReturnData("1311/mudLog.xml");
        mudlogsXML = convertVersion(mudlogsXML , "1.3.1.1");
        ObjMudLogs mudlogs = null;
        try {
            mudlogs = WitsmlMarshal.deserialize(mudlogsXML, ObjMudLogs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getMudLogs);
        assertEquals(getMudLogs.getMudLog().size(), mudlogs.getMudLog().size());
        assertEquals(getMudLogs.getMudLog().get(0).getUid(), mudlogs.getMudLog().get(0).getUid());
    }

    @Test
    public void TestGetMudLog1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjMudLogs getMudLogs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("mudLog");

        try {
            getMudLogs = client.getMudLogsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String mudlogsXML = getReturnData("1411/mudLog.xml");
        ObjMudLogs mudlogs = null;
        try {
            mudlogs = WitsmlMarshal.deserialize(mudlogsXML, ObjMudLogs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getMudLogs);
        assertEquals(getMudLogs.getMudLog().size(), mudlogs.getMudLog().size());
        assertEquals(getMudLogs.getMudLog().get(0).getUid(), mudlogs.getMudLog().get(0).getUid());
    }

    @Test
    public void TestGetTrajectory1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjTrajectorys getTrajectorys = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("trajectory");

        try {
            getTrajectorys = client.getTrajectorysAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String trajectoryXML = getReturnData("1311/trajectory.xml");
        trajectoryXML = convertVersion(trajectoryXML , "1.3.1.1");
        ObjTrajectorys trajectorys = null;
        try {
            trajectorys = WitsmlMarshal.deserialize(trajectoryXML, ObjTrajectorys.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getTrajectorys);
        assertEquals(trajectorys.getTrajectory().size(), trajectorys.getTrajectory().size());
        assertEquals(getTrajectorys.getTrajectory().get(0).getUid(), trajectorys.getTrajectory().get(0).getUid());
    }

    @Test
    public void TestGetTrajectory1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjTrajectorys getTrajectorys = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("trajectory");

        try {
            getTrajectorys = client.getTrajectorysAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String trajectoryXML = getReturnData("1411/trajectory.xml");
        ObjTrajectorys trajectorys = null;
        try {
            trajectorys = WitsmlMarshal.deserialize(trajectoryXML, ObjTrajectorys.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getTrajectorys);
        assertEquals(getTrajectorys.getTrajectory().size(), trajectorys.getTrajectory().size());
        assertEquals(getTrajectorys.getTrajectory().get(0).getUid(), trajectorys.getTrajectory().get(0).getUid());
    }

    @Test
    public void TestGetMessage1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjMessages getMessages = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("message");

        try {
            getMessages = client.getMessagesAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String messageXML = getReturnData("1311/message.xml");
        messageXML = convertVersion(messageXML , "1.3.1.1");
        ObjMessages messages = null;
        try {
            messages = WitsmlMarshal.deserialize(messageXML, ObjMessages.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getMessages);
        assertEquals(getMessages.getMessage().size(), messages.getMessage().size());
        assertEquals(getMessages.getMessage().get(0).getUid(), messages.getMessage().get(0).getUid());
    }

    @Test
    public void TestGetMessage1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjMessages getMessages = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("message");

        try {
            getMessages = client.getMessagesAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String messageXML = getReturnData("1411/message.xml");
        ObjMessages messages = null;
        try {
            messages = WitsmlMarshal.deserialize(messageXML, ObjMessages.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getMessages);
        assertEquals(getMessages.getMessage().size(), messages.getMessage().size());
        assertEquals(getMessages.getMessage().get(0).getUid(), messages.getMessage().get(0).getUid());
    }

    @Test
    public void TestBhaRuns1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjBhaRuns getBhaRun = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("bhaRun");

        try {
            getBhaRun = client.getBhaRunsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String bhaRunsXML = getReturnData("1311/bhaRun.xml");
        bhaRunsXML = convertVersion(bhaRunsXML , "1.3.1.1");
        ObjBhaRuns bhaRuns = null;
        try {
            bhaRuns = WitsmlMarshal.deserialize(bhaRunsXML, ObjBhaRuns.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getBhaRun);
        assertEquals(getBhaRun.getBhaRun().size(), bhaRuns.getBhaRun().size());
        assertEquals(getBhaRun.getBhaRun().get(0).getUid(), bhaRuns.getBhaRun().get(0).getUid());
    }

    @Test
    public void TestBhaRuns1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjBhaRuns getBhaRun = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("bhaRun");

        try {
            getBhaRun = client.getBhaRunsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String bhaRunsXML = getReturnData("1411/bhaRun.xml");
        ObjBhaRuns bhaRuns = null;
        try {
            bhaRuns = WitsmlMarshal.deserialize(bhaRunsXML, ObjBhaRuns.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getBhaRun);
        assertEquals(getBhaRun.getBhaRun().size(), bhaRuns.getBhaRun().size());
        assertEquals(getBhaRun.getBhaRun().get(0).getUid(), bhaRuns.getBhaRun().get(0).getUid());
    }

    @Test
    public void TestCementJobs1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjCementJobs getCementJobs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("cementJob");

        try {
            getCementJobs = client.getCementJobsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String cementJobXML = getReturnData("1311/cementJob.xml");
        cementJobXML = convertVersion(cementJobXML , "1.3.1.1");
        ObjCementJobs cementJobs = null;
        try {
            cementJobs = WitsmlMarshal.deserialize(cementJobXML, ObjCementJobs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getCementJobs);
        assertEquals(getCementJobs.getCementJob().size(), cementJobs.getCementJob().size());
        assertEquals(getCementJobs.getCementJob().get(0).getUid(), cementJobs.getCementJob().get(0).getUid());
    }

    @Test
    public void TestCementJob1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjCementJobs getCementJobs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("cementJob");

        try {
            getCementJobs = client.getCementJobsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String cementJobXML = getReturnData("1411/cementJob.xml");
        ObjCementJobs cementJobs = null;
        try {
            cementJobs = WitsmlMarshal.deserialize(cementJobXML, ObjCementJobs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getCementJobs);
        assertEquals(getCementJobs.getCementJob().size(), cementJobs.getCementJob().size());
        assertEquals(getCementJobs.getCementJob().get(0).getUid(), cementJobs.getCementJob().get(0).getUid());
    }

    @Test
    public void TestConvCores1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjConvCores getConvCores = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("convCore");

        try {
            getConvCores = client.getConvCoresAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String convCoreXML = getReturnData("1311/convCore.xml");
        convCoreXML = convertVersion(convCoreXML , "1.3.1.1");
        ObjConvCores convCores = null;
        try {
            convCores = WitsmlMarshal.deserialize(convCoreXML, ObjConvCores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getConvCores);
        assertEquals(getConvCores.getConvCore().size(), convCores.getConvCore().size());
        assertEquals(getConvCores.getConvCore().get(0).getUid(), convCores.getConvCore().get(0).getUid());
    }

    @Test
    public void TestConvCore1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjConvCores getConvCores = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("convCore");

        try {
            getConvCores = client.getConvCoresAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String convCoreXML = getReturnData("1411/convCore.xml");
        ObjConvCores convCores = null;
        try {
            convCores = WitsmlMarshal.deserialize(convCoreXML, ObjConvCores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getConvCores);
        assertEquals(getConvCores.getConvCore().size(), convCores.getConvCore().size());
        assertEquals(getConvCores.getConvCore().get(0).getUid(), convCores.getConvCore().get(0).getUid());
    }

    @Test
    public void TestFluidReports1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjFluidsReports getFluidsReport = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("fluidsReport");

        try {
            getFluidsReport = client.getFluidsReportsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String fluidsReportXML = getReturnData("1311/fluidsReport.xml");
        fluidsReportXML = convertVersion(fluidsReportXML , "1.3.1.1");
        ObjFluidsReports fluidsReports = null;
        try {
            fluidsReports = WitsmlMarshal.deserialize(fluidsReportXML, ObjFluidsReports.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getFluidsReport);
        assertEquals(getFluidsReport.getFluidsReport().size(), fluidsReports.getFluidsReport().size());
        assertEquals(getFluidsReport.getFluidsReport().get(0).getUid(), fluidsReports.getFluidsReport().get(0).getUid());
    }

    @Test
    public void TestFluidsReport1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjFluidsReports getFluidsReport = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("fluidsReport");

        try {
            getFluidsReport = client.getFluidsReportsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String fluidsReportXML = getReturnData("1411/fluidsReport.xml");
        ObjFluidsReports fluidsReports = null;
        try {
            fluidsReports = WitsmlMarshal.deserialize(fluidsReportXML, ObjFluidsReports.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getFluidsReport);
        assertEquals(getFluidsReport.getFluidsReport().size(), fluidsReports.getFluidsReport().size());
        assertEquals(getFluidsReport.getFluidsReport().get(0).getUid(), fluidsReports.getFluidsReport().get(0).getUid());
    }

    @Test
    public void TestFormationmarker1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjFormationMarkers getFormationMarker = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("formationMarker");

        try {
            getFormationMarker = client.getFormationMarkersAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String formationMarkerXML = getReturnData("1311/formationMarker.xml");
        formationMarkerXML = convertVersion(formationMarkerXML , "1.3.1.1");
        ObjFormationMarkers formationMarkers = null;
        try {
            formationMarkers = WitsmlMarshal.deserialize(formationMarkerXML, ObjFormationMarkers.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getFormationMarker);
        assertEquals(getFormationMarker.getFormationMarker().size(), formationMarkers.getFormationMarker().size());
        assertEquals(getFormationMarker.getFormationMarker().get(0).getUid(), formationMarkers.getFormationMarker().get(0).getUid());
    }

    @Test
    public void TestFormationMarker1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjFormationMarkers getFormationMarker = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("formationMarker");

        try {
            getFormationMarker = client.getFormationMarkersAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String formationMarkerXML = getReturnData("1411/formationMarker.xml");
        ObjFormationMarkers formationMarkers = null;
        try {
            formationMarkers = WitsmlMarshal.deserialize(formationMarkerXML, ObjFormationMarkers.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getFormationMarker);
        assertEquals(getFormationMarker.getFormationMarker().size(), formationMarkers.getFormationMarker().size());
        assertEquals(getFormationMarker.getFormationMarker().get(0).getUid(), formationMarkers.getFormationMarker().get(0).getUid());
    }

    @Test
    public void TestRigs1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjRigs getRigs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("rig");

        try {
            getRigs = client.getRigsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String rigsXML = getReturnData("1311/rig.xml");
        rigsXML = convertVersion(rigsXML , "1.3.1.1");
        ObjRigs rigs = null;
        try {
            rigs = WitsmlMarshal.deserialize(rigsXML, ObjRigs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getRigs);
        assertEquals(getRigs.getRig().size(), rigs.getRig().size());
        assertEquals(getRigs.getRig().get(0).getUid(), rigs.getRig().get(0).getUid());
    }

    @Test
    public void TestRigs1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjRigs getRigs = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("rig");

        try {
            getRigs = client.getRigsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String rigsXML = getReturnData("1411/rig.xml");
        ObjRigs rigs = null;
        try {
            rigs = WitsmlMarshal.deserialize(rigsXML, ObjRigs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getRigs);
        assertEquals(getRigs.getRig().size(), rigs.getRig().size());
        assertEquals(getRigs.getRig().get(0).getUid(), rigs.getRig().get(0).getUid());
    }

    @Test
    public void TestRisk1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjRisks getRisk = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("risk");

        try {
            getRisk = client.getRisksAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String risksXML = getReturnData("1311/risk.xml");
        risksXML = convertVersion(risksXML , "1.3.1.1");
        ObjRisks risks = null;
        try {
            risks = WitsmlMarshal.deserialize(risksXML, ObjRisks.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getRisk);
        assertEquals(getRisk.getRisk().size(), risks.getRisk().size());
        assertEquals(getRisk.getRisk().get(0).getUid(), risks.getRisk().get(0).getUid());
    }

    @Test
    public void TestRisk1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjRisks getRisk = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("risk");

        try {
            getRisk = client.getRisksAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String risksXML = getReturnData("1411/risk.xml");
        ObjRisks risks = null;
        try {
            risks = WitsmlMarshal.deserialize(risksXML, ObjRisks.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getRisk);
        assertEquals(getRisk.getRisk().size(), risks.getRisk().size());
        assertEquals(getRisk.getRisk().get(0).getUid(), risks.getRisk().get(0).getUid());
    }

    @Test
    public void TestOpsReport1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjOpsReports getOpsReport = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("opsReport");

        try {
            getOpsReport = client.getOpsReportsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String opsReportXML = getReturnData("1311/opsReport.xml");
        opsReportXML = convertVersion(opsReportXML , "1.3.1.1");
        ObjOpsReports opsReports = null;
        try {
            opsReports = WitsmlMarshal.deserialize(opsReportXML, ObjOpsReports.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getOpsReport);
        assertEquals(getOpsReport.getOpsReport().size(), opsReports.getOpsReport().size());
        assertEquals(getOpsReport.getOpsReport().get(0).getUid(), opsReports.getOpsReport().get(0).getUid());
    }

    @Test
    public void TestOpsReport1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjOpsReports getOpsReport = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("opsReport");

        try {
            getOpsReport = client.getOpsReportsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String opsReportXML = getReturnData("1411/opsReport.xml");
        ObjOpsReports opsReports = null;
        try {
            opsReports = WitsmlMarshal.deserialize(opsReportXML, ObjOpsReports.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getOpsReport);
        assertEquals(getOpsReport.getOpsReport().size(), opsReports.getOpsReport().size());
        assertEquals(getOpsReport.getOpsReport().get(0).getUid(), opsReports.getOpsReport().get(0).getUid());
    }

    @Test
    public void TestSidewallCore1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjSidewallCores getSidewallCore = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("sidewallCore");

        try {
            getSidewallCore = client.getSideWallCoresAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String sidewallCoreXML = getReturnData("1311/sidewallCore.xml");
        sidewallCoreXML = convertVersion(sidewallCoreXML , "1.3.1.1");
        ObjSidewallCores sidewallCores = null;
        try {
            sidewallCores = WitsmlMarshal.deserialize(sidewallCoreXML, ObjSidewallCores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getSidewallCore);
        assertEquals(getSidewallCore.getSidewallCore().size(), sidewallCores.getSidewallCore().size());
        assertEquals(getSidewallCore.getSidewallCore().get(0).getUid(), sidewallCores.getSidewallCore().get(0).getUid());
    }

    @Test
    public void TestSidewallCore1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjSidewallCores getSidewallCore = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("sidewallCore");

        try {
            getSidewallCore = client.getSideWallCoresAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String sidewallCoreXML = getReturnData("1411/sidewallCore.xml");
        ObjSidewallCores sidewallCores = null;
        try {
            sidewallCores = WitsmlMarshal.deserialize(sidewallCoreXML, ObjSidewallCores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getSidewallCore);
        assertEquals(getSidewallCore.getSidewallCore().size(), sidewallCores.getSidewallCore().size());
        assertEquals(getSidewallCore.getSidewallCore().get(0).getUid(), sidewallCores.getSidewallCore().get(0).getUid());
    }

    @Test
    public void TestSurveyProgram1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjSurveyPrograms getSurveyProgram = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("surveyProgram");

        try {
            getSurveyProgram = client.getSurveyProgramsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String surveyProgramXML = getReturnData("1311/surveyProgram.xml");
        surveyProgramXML = convertVersion(surveyProgramXML , "1.3.1.1");
        ObjSurveyPrograms surveyPrograms = null;
        try {
            surveyPrograms = WitsmlMarshal.deserialize(surveyProgramXML, ObjSidewallCores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getSurveyProgram);
        assertEquals(getSurveyProgram.getSurveyProgram().size(), surveyPrograms.getSurveyProgram().size());
        assertEquals(getSurveyProgram.getSurveyProgram().get(0).getUid(), surveyPrograms.getSurveyProgram().get(0).getUid());
    }

    @Test
    public void TestSurveyProgram1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjSurveyPrograms getSurveyProgram = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("surveyProgram");

        try {
            getSurveyProgram = client.getSurveyProgramsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String surveyProgramXML = getReturnData("1411/surveyProgram.xml");
        ObjSurveyPrograms surveyPrograms = null;
        try {
            surveyPrograms = WitsmlMarshal.deserialize(surveyProgramXML, ObjSidewallCores.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getSurveyProgram);
        assertEquals(getSurveyProgram.getSurveyProgram().size(), surveyPrograms.getSurveyProgram().size());
        assertEquals(getSurveyProgram.getSurveyProgram().get(0).getUid(), surveyPrograms.getSurveyProgram().get(0).getUid());
    }

    @Test
    public void TestTarget1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjTargets getTarget = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("target");

        try {
            getTarget = client.getTargetsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String targetXML = getReturnData("1311/target.xml");
        targetXML = convertVersion(targetXML , "1.3.1.1");
        ObjTargets targets = null;
        try {
            targets = WitsmlMarshal.deserialize(targetXML, ObjTargets.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getTarget);
        assertEquals(getTarget.getTarget().size(), targets.getTarget().size());
        assertEquals(getTarget.getTarget().get(0).getUid(), targets.getTarget().get(0).getUid());
    }

    @Test
    public void TestTarget1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjTargets getTarget = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("target");

        try {
            getTarget = client.getTargetsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String targetXML = getReturnData("1411/target.xml");
        ObjTargets targets = null;
        try {
            targets = WitsmlMarshal.deserialize(targetXML, ObjTargets.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getTarget);
        assertEquals(getTarget.getTarget().size(), targets.getTarget().size());
        assertEquals(getTarget.getTarget().get(0).getUid(), targets.getTarget().get(0).getUid());
    }

    @Test
    public void TestTubular1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjTubulars getTubular = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("tubular");

        try {
            getTubular = client.getTubularsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String tubularXML = getReturnData("1311/tubular.xml");
        tubularXML = convertVersion(tubularXML , "1.3.1.1");
        ObjTubulars tubular = null;
        try {
            tubular = WitsmlMarshal.deserialize(tubularXML, ObjTubulars.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getTubular);
        assertEquals(getTubular.getTubular().size(), tubular.getTubular().size());
        assertEquals(getTubular.getTubular().get(0).getUid(), tubular.getTubular().get(0).getUid());
    }

    @Test
    public void TestTubular1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjTubulars getTubular = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("tubular");

        try {
            getTubular = client.getTubularsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String tubularXML = getReturnData("1411/tubular.xml");
        ObjTubulars tubular = null;
        try {
            tubular = WitsmlMarshal.deserialize(tubularXML, ObjTubulars.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getTubular);
        assertEquals(getTubular.getTubular().size(), tubular.getTubular().size());
        assertEquals(getTubular.getTubular().get(0).getUid(), tubular.getTubular().get(0).getUid());
    }

    @Test
    public void TestWbGeometry1311() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1311);
        ObjWbGeometrys getWbGeometry = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("wbGeometry");

        try {
            getWbGeometry = client.getWbGeometrysAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String wbGeometryXML = getReturnData("1311/wbGeometry.xml");
        wbGeometryXML = convertVersion(wbGeometryXML , "1.3.1.1");
        ObjWbGeometrys wbGeometrys = null;
        try {
            wbGeometrys = WitsmlMarshal.deserialize(wbGeometryXML, ObjWbGeometrys.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getWbGeometry);
        assertEquals(getWbGeometry.getWbGeometry().size(), wbGeometrys.getWbGeometry().size());
        assertEquals(getWbGeometry.getWbGeometry().get(0).getUid(), wbGeometrys.getWbGeometry().get(0).getUid());
    }

    @Test
    public void TestWbGeometry1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjWbGeometrys getWbGeometry = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("wbGeometry");

        try {
            getWbGeometry = client.getWbGeometrysAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String wbGeometryXML = getReturnData("1411/wbGeometry.xml");
        ObjWbGeometrys wbGeometrys = null;
        try {
            wbGeometrys = WitsmlMarshal.deserialize(wbGeometryXML, ObjWbGeometrys.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getWbGeometry);
        assertEquals(getWbGeometry.getWbGeometry().size(), wbGeometrys.getWbGeometry().size());
        assertEquals(getWbGeometry.getWbGeometry().get(0).getUid(), wbGeometrys.getWbGeometry().get(0).getUid());
    }

    @Test
    public void TestAttachment1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjAttachments getAttachment = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("attachment");

        try {
            getAttachment = client.getAttachmentsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String attachmentXML = getReturnData("1411/attachment.xml");
        ObjAttachments attachments = null;
        try {
            attachments = WitsmlMarshal.deserialize(attachmentXML, ObjAttachments.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getAttachment);
        assertEquals(getAttachment.getAttachment().size(), attachments.getAttachment().size());
        assertEquals(getAttachment.getAttachment().get(0).getUid(), attachments.getAttachment().get(0).getUid());
    }

    @Test
    public void TestChangeLog1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjChangeLogs getChangeLog = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("changeLog");

        try {
            getChangeLog = client.getChangeLogsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String changeLogXML = getReturnData("1411/changeLog.xml");
        ObjChangeLogs changeLogs = null;
        try {
            changeLogs = WitsmlMarshal.deserialize(changeLogXML, ObjChangeLogs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getChangeLog);
        assertEquals(getChangeLog.getChangeLog().size(), changeLogs.getChangeLog().size());
        assertEquals(getChangeLog.getChangeLog().get(0).getUid(), changeLogs.getChangeLog().get(0).getUid());
    }

    @Test
    public void TestDrillReport1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjDrillReports getDrillReports = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("drillReport");

        try {
            getDrillReports = client.getDrillReportsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String drillReportXML = getReturnData("1411/drillReport.xml");
        ObjDrillReports drillReports = null;
        try {
            drillReports = WitsmlMarshal.deserialize(drillReportXML, ObjDrillReports.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getDrillReports);
        assertEquals(getDrillReports.getDrillReport().size(), drillReports.getDrillReport().size());
        assertEquals(getDrillReports.getDrillReport().get(0).getUid(), drillReports.getDrillReport().get(0).getUid());
    }

    @Test
    public void TestObjectGroup1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjObjectGroups getObjectGroup = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("objectGroup");

        try {
            getObjectGroup = client.getObjectGroupsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String objectGroupXML = getReturnData("1411/objectGroup.xml");
        ObjObjectGroups objectGroups = null;
        try {
            objectGroups = WitsmlMarshal.deserialize(objectGroupXML, ObjObjectGroups.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getObjectGroup);
        assertEquals(getObjectGroup.getObjectGroup().size(), objectGroups.getObjectGroup().size());
        assertEquals(getObjectGroup.getObjectGroup().get(0).getUid(), objectGroups.getObjectGroup().get(0).getUid());
    }

    @Test
    public void TestStimJob1411() throws IOException {
        client.setVersion(WitsmlVersion.VERSION_1411);
        ObjStimJobs getStimJob = null;
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setObjectType("stimJob");

        try {
            getStimJob = client.getStimJobsAsObj(witsmlQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String stimJobXML = getReturnData("1411/stimJob.xml");
        ObjStimJobs stimJobs = null;
        try {
            stimJobs = WitsmlMarshal.deserialize(stimJobXML, ObjStimJobs.class);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

        assertNotNull(getStimJob);
        assertEquals(getStimJob.getStimJob().size(), stimJobs.getStimJob().size());
        assertEquals(getStimJob.getStimJob().get(0).getUid(), stimJobs.getStimJob().get(0).getUid());
    }

    private String convertVersion(String original, String version){
        String converted = null;
        if (version.toString().equals("1.3.1.1")) {
            try {
                converted = transformer.convertVersion(original);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } else {
            return original;
        }
        if (converted == null) return null;
        if (converted.equals("")) return null;
        return converted;
    }

    private String getReturnData(String resourcePath) throws IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
