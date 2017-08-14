package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import com.hashmapinc.tempus.WitsmlObjects.v1311.*;
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
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjRisk;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjRisks;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSidewallCores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSurveyPrograms;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTargets;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTrajectorys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTubulars;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWbGeometrys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWellbores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWells;
import com.hashmapinc.tempus.witsml.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class MockClient implements WitsmlClient {

    private String url;
    private Logger log;
    private WitsmlVersion version = WitsmlVersion.VERSION_1411;
    private List<String> supportedVersions;
    private Boolean connected = false;
    private String userName = "";
    private String password = "";
    private String supportedApiVersions = "1.3.1.1,1.4.1.1";

    private WitsmlVersionTransformer transform;

    public MockClient(String url){
        log = LogManager.getLogger(Client.class);
        setupClient(url);
        try {
            transform = new WitsmlVersionTransformer();
        } catch (TransformerConfigurationException e) {
            log.error("Error setting up XSLT Transformer " + e.getMessage());
        }
    }

    /**
     * Setup client
     * @param url
     */
    private void setupClient(String url) {
        this.url = url;
    }

    /**
     * Sets the URL for the WITSML STORE API Requests
     * @return URL being used to execute WITSML STORE API Requests
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL for the WITSML STORE API Requests
     * @param url URL to use for executing WITSML STORE API Requests
     */
    public void setUrl(String url){
        this.url = url;
    }

    /**
     * Sets the user name for the WITSML STORE API Requests (sent as HTTP Basic)
     * @return  userName The user name being used to execute WITSML STORE API Requests
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Sets the user name for the WITSML STORE API Requests (sent as HTTP Basic)
     * @param userName The user name to use to execute WITSML STORE API Requests
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Sets the password for the WITSML API requests (sent as HTTP Basic)
     * @param password The password to use
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Sets the version that will be used for querying
     * @param version The version string that will be used
     */
    public void setVersion(WitsmlVersion version){
        this.version = version;
    }

    /**
     * This method queries the server and returns the supported WITSML version in a comma delimited string.
     * @return A comma-delimited list of supported version from the server, will return null if an error occured.
     */
    public String getVersion(){
        return supportedApiVersions;
    }

    /**
     * Sets the version and validates the connection information.
     */
    public void connect() {
        connected = true;
    }

    /**
     * Returns the capabilites that are supported by the remote server
     * @return The XML document representing the capabilities of the server
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    public String getCapabilities() throws RemoteException {
        try {
            if (version == WitsmlVersion.VERSION_1311)
                return getReturnData("caps1311.xml");
            else if (version == WitsmlVersion.VERSION_1411)
                return getReturnData("caps1411.xml");
            else
                return "";
        } catch (IOException ex){
            throw new RemoteException(ex.getMessage());
        }
    }

    /**
     * This method gets all the wells on the server that the user has access to
     * @return String representation of all the wells the user has access to
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    public String getWells() throws FileNotFoundException, RemoteException, Exception {
        return "";
    }

    /**
     * This method gets all the wells on the server that the user has access to
     * @return Returns a POJO that represents the Obj_wells XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    public ObjWells getWellsAsObj() throws Exception {
        String wells = getWells();
        return WitsmlMarshal.deserialize(wells, ObjWells.class);
    }

    /**
     * This method gets all the wellbores on the server that the user has access to under the specified well
     * @param wellId The UID of the well that we want to get wellbores for
     * @return String representation of all the wellbores the user has access to under the specified Well
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    public String getWellboresForWell(String wellId) throws FileNotFoundException, RemoteException, Exception {
        return "";
    }

    /**
     * This method gets all the wellbores on the server that the user has access to under the specified well
     * @param wellId The UID of the well that we want to get wellbores for
     * @return Returns a POJO that represents the Obj_wellbores XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    public ObjWellbores getWellboresForWellAsObj(String wellId) throws Exception {
        String wellbores = getWellboresForWell(wellId);
        return WitsmlMarshal.deserialize(wellbores, ObjWellbores.class);
    }

    /**
     * This method gets all of the logs contained within a wellbore and returns the metadata (minus the <data></data> element
     * @param wellId The UID of thw well that contains the wellbore that contains the log
     * @param wellboreId The UID of the wellbore that contains the log
     * @return The string representation of the WITSML of the Logs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    public String getLogMetadata(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * Executes a log query in either 1.3.1.1 or 1.4.1.1
     * @param query The query to send to the server
     * @param optionsIn The options to send (only supported in 1.4.1.1)
     * @param capabilitiesIn The capabilites to send (only supported in 1.4.1.1)
     * @return a string representing the response from the server
     * @throws RemoteException thrown on any exception encountered from the server
     */
    public String executeLogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        return "";
    }

    /**
     * Executes a mudLog query in either 1.3.1.1 or 1.4.1.1
     * @param query The query to send to the server
     * @param optionsIn The options to send (only supported in 1.4.1.1)
     * @param capabilitiesIn The capabilities to send (only supported in 1.4.1.1)
     * @return a string representing the response from the server
     * @throws RemoteException thrown on any exception encountered from the server
     */
    @Override
    public String executeMudlogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        return "";
    }

    /**
     * Executes a trajectory query in either 1.3.1.1 or 1.4.1.1
     * @param query The query to send to the server
     * @param optionsIn The options to send (only supported in 1.4.1.1)
     * @param capabilitiesIn The capabilities to send (only supported in 1.4.1.1)
     * @return a string representing the response from the server
     * @throws RemoteException thrown on any exception encountered from the server
     */
    @Override
    public String executeTrajectoryQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        return "";
    }

    /**
     * This method gets all of the logs contained within a wellbore and returns the metadata (minus the <data></data> element
     * @param wellId The UID of thw well that contains the wellbore that contains the log
     * @param wellboreId The UID of the wellbore that contains the log
     * @return A POJO representing the Logs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    public ObjLogs getLogMetadataAsObj(String wellId, String wellboreId) throws Exception {
        String logs = getLogMetadata(wellId, wellboreId);
        return WitsmlMarshal.deserialize(logs, ObjLogs.class);
    }

    /**
     * This method gets all of the mudlogs contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the mudlog
     * @param wellboreId The UID of the wellbore that contains the mudlog
     * @return The string representation of the WITSML of the MudLogs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getMudLogs(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the mudlogs contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the mudlog
     * @param wellboreId The UID of the wellbore that contains the mudlog
     * @return A POJO representing the MudLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    public ObjMudLogs getMudLogsAsObj(String wellId, String wellboreId) throws Exception {
        String mudLogs = getMudLogs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(mudLogs, ObjMudLogs.class);
    }

    /**
     * This method gets all of the trajectorys contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the trajectory
     * @param wellboreId The UID of the wellbore that contains the trajectory
     * @return The string representation of the WITSML of the Trajectorys contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTrajectorys(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the trajectorys contained within a wellbore and returns the metadata (minus the <data></data> element
     * @param wellId The UID of the well that contains the wellbore that contains the trajectory
     * @param wellboreId The UID of the wellbore that contains the trajectory
     * @return A POJO representing the Trajectorys contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTrajectorys getTrajectorysAsObj(String wellId, String wellboreId) throws Exception {
        String trajectorys = getTrajectorys(wellId, wellboreId);
        return WitsmlMarshal.deserialize(trajectorys, ObjTrajectorys.class);
    }

    /**
     * This method gets all of the bhaRuns contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the bhaRuns
     * @param wellboreId The UID of the wellbore that contains the bhaRuns
     * @return The string representation of the WITSML of the BhaRuns contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getBhaRuns(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the bhaRuns contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the BhaRun
     * @param wellboreId The UID of the wellbore that contains the BhaRun
     * @return A POJO representing the BhaRuns contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjBhaRuns getBhaRunsAsObj(String wellId, String wellboreId) throws Exception {
        String bhaRuns = getBhaRuns(wellId, wellboreId);
        return WitsmlMarshal.deserialize(bhaRuns, ObjBhaRuns.class);
    }

    /**
     * This method gets all of the cementJobs contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the CementJob
     * @param wellboreId The UID of the wellbore that contains the CementJob
     * @return The string representation of the WITSML of the CementJobs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getCementJobs(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the cementJobs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the CementJob
     * @param wellboreId The UID of the wellbore that contains the CementJob
     * @return A POJO representing the CementJobs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjCementJobs getCementJobsAsObj(String wellId, String wellboreId) throws Exception {
        String cementJob = getCementJobs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(cementJob, ObjConvCores.class);
    }

    /**
     * This method gets all of the convCores contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the ConvCore
     * @param wellboreId The UID of the wellbore that contains the ConvCore
     * @return The string representation of the WITSML of the ConvCores contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getConvCores(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the convCores contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the ConvCore
     * @param wellboreId The UID of the wellbore that contains the ConvCore
     * @return A POJO representing the ConvCores contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjConvCores getConvCoresAsObj(String wellId, String wellboreId) throws Exception {
        String convCores = getConvCores(wellId, wellboreId);
        return WitsmlMarshal.deserialize(convCores, ObjConvCores.class);
    }

    /**
     * This method gets all of the DtsInstalledSystems contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the DtsInstalledSystem
     * @param wellboreId The UID of the wellbore that contains the DtsInstalledSystem
     * @return The string representation of the WITSML of the DtsInstalledSystems contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getDtsInstalledSystems(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the DtsInstalledSystems contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the DtsInstalledSystem
     * @param wellboreId The UID of the wellbore that contains the DtsInstalledSystem
     * @return A POJO representing the DtsInstalledSystems contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjDtsInstalledSystems getDtsInstalledSystemsAsObj(String wellId, String wellboreId) throws Exception {
        String dtsInstalledSystems = getDtsInstalledSystems(wellId, wellboreId);
        return WitsmlMarshal.deserialize(dtsInstalledSystems, ObjDtsInstalledSystems.class);
    }

    /**
     * This method gets all of the FluidsReports contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the FluidsReport
     * @param wellboreId The UID of the wellbore that contains the FluidsReport
     * @return The string representation of the WITSML of the FluidsReports contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getFluidsReports(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the FluidsReports contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the FluidsReport
     * @param wellboreId The UID of the wellbore that contains the FluidsReport
     * @return A POJO representing the FluidsReports contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjFluidsReports getFluidsReportsAsObj(String wellId, String wellboreId) throws Exception {
        String fluidsReports = getFluidsReports(wellId, wellboreId);
        return WitsmlMarshal.deserialize(fluidsReports, ObjFluidsReports.class);
    }

    /**
     * This method gets all of the formationMarker contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the FormationMarker
     * @param wellboreId The UID of the wellbore that contains the FormationMarker
     * @return The string representation of the WITSML of the FormationMarkers contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getFormationMarkers(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the FormationMarkers contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the FormationMarker
     * @param wellboreId The UID of the wellbore that contains the FormationMarker
     * @return A POJO representing the FormationMarkers contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjFormationMarkers getFormationMarkersAsObj(String wellId, String wellboreId) throws Exception {
        String formationMarkers = getFormationMarkers(wellId, wellboreId);
        return WitsmlMarshal.deserialize(formationMarkers, ObjFormationMarkers.class);
    }

    /**
     * This method gets all of the Messages contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the Message
     * @param wellboreId The UID of the wellbore that contains the Message
     * @return The string representation of the WITSML of the Messages contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getMessages(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the Messages contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Message
     * @param wellboreId The UID of the wellbore that contains the Message
     * @return A POJO representing the Messages contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjMessages getMessagesAsObj(String wellId, String wellboreId) throws Exception {
        String messages = getMessages(wellId, wellboreId);
        return WitsmlMarshal.deserialize(messages, ObjMessages.class);
    }

    /**
     * This method gets all of the OpsReport contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the OpsReport
     * @param wellboreId The UID of the wellbore that contains the OpsReport
     * @return The string representation of the WITSML of the OpsReports contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getOpsReports(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the OpsReport contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the OpsReport
     * @param wellboreId The UID of the wellbore that contains the OpsReport
     * @return A POJO representing the OpsReports contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjOpsReports getOpsReportsAsObj(String wellId, String wellboreId) throws Exception {
        String opsReports = getOpsReports(wellId, wellboreId);
        return WitsmlMarshal.deserialize(opsReports, ObjOpsReports.class);
    }

    /**
     * This method gets all of the Rigs contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the Rig
     * @param wellboreId The UID of the wellbore that contains the Rig
     * @return The string representation of the WITSML of the Rigs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getRigs(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the Rigs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Rig
     * @param wellboreId The UID of the wellbore that contains the Rig
     * @return A POJO representing the Rigs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjRigs getRigsAsObj(String wellId, String wellboreId) throws Exception {
        String rigs = getRigs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(rigs, ObjRigs.class);
    }

    /**
     * This method gets all of the Risks contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the Risk
     * @param wellboreId The UID of the wellbore that contains the Risk
     * @return The string representation of the WITSML of the Risks contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getRisks(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the Risks contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Risk
     * @param wellboreId The UID of the wellbore that contains the Risk
     * @return A POJO representing the Risks contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjRisks getRisksAsObj(String wellId, String wellboreId) throws Exception {
        String risks = getRisks(wellId, wellboreId);
        return WitsmlMarshal.deserialize(risks, ObjRisks.class);
    }

    /**
     * This method gets all of the SideWallCores contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the SideWallCore
     * @param wellboreId The UID of the wellbore that contains the SideWallCore
     * @return The string representation of the WITSML of the SideWallCores contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getSideWallCores(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the SideWallCores contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the SideWallCore
     * @param wellboreId The UID of the wellbore that contains the SideWallCore
     * @return A POJO representing the SideWallCores contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjSidewallCores getSideWallCoresAsObj(String wellId, String wellboreId) throws Exception {
        String sideWallCores = getSideWallCores(wellId, wellboreId);
        return WitsmlMarshal.deserialize(sideWallCores, ObjSidewallCores.class);
    }

    /**
     * This method gets all of the SurveyPrograms contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the SurveyProgram
     * @param wellboreId The UID of the wellbore that contains the SurveyProgram
     * @return The string representation of the WITSML of the SurveyPrograms contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getSurveyPrograms(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the SurveyPrograms contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the SurveyProgram
     * @param wellboreId The UID of the wellbore that contains the SurveyProgram
     * @return A POJO representing the SurveyPrograms contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjSurveyPrograms getSurveyProgramsAsObj(String wellId, String wellboreId) throws Exception {
        String sideWallCores = getSurveyPrograms(wellId, wellboreId);
        return WitsmlMarshal.deserialize(sideWallCores, ObjSurveyPrograms.class);
    }

    /**
     * This method gets all of the Targets contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the Target
     * @param wellboreId The UID of the wellbore that contains the Target
     * @return The string representation of the WITSML of the Targets contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTargets(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the Targets contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Target
     * @param wellboreId The UID of the wellbore that contains the Target
     * @return A POJO representing the Targets contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTargets getTargetsAsObj(String wellId, String wellboreId) throws Exception {
        String targets = getTargets(wellId, wellboreId);
        return WitsmlMarshal.deserialize(targets, ObjTargets.class);
    }

    /**
     * This method gets all of the Tubulars contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the Tubular
     * @param wellboreId The UID of the wellbore that contains the Tubular
     * @return The string representation of the WITSML of the Tubulars contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTubulars(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the Tubulars contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Tubular
     * @param wellboreId The UID of the wellbore that contains the Tubular
     * @return A POJO representing the Tubulars contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTubulars getTubularsAsObj(String wellId, String wellboreId) throws Exception {
        String tubulars = getTubulars(wellId, wellboreId);
        return WitsmlMarshal.deserialize(tubulars, ObjTubulars.class);
    }

    /**
     * This method gets all of the WbGeometrys contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the WbGeometry
     * @param wellboreId The UID of the wellbore that contains the WbGeometry
     * @return The string representation of the WITSML of the WbGeometrys contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getWbGeometrys(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the WbGeometrys contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the WbGeometry
     * @param wellboreId The UID of the wellbore that contains the WbGeometry
     * @return A POJO representing the WbGeometrys contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWbGeometrys getWbGeometrysAsObj(String wellId, String wellboreId) throws Exception {
        String wbGeometrys = getWbGeometrys(wellId, wellboreId);
        return WitsmlMarshal.deserialize(wbGeometrys, ObjWbGeometrys.class);
    }

    /**
     * This method gets all of the attachment contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the Attachment
     * @param wellboreId The UID of the wellbore that contains the Attachment
     * @return The string representation of the WITSML of the Attachments contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getAttachments(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the Attachments contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Attachment
     * @param wellboreId The UID of the wellbore that contains the Attachment
     * @return A POJO representing the Attachments contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjAttachments getAttachmentsAsObj(String wellId, String wellboreId) throws Exception {
        String attachments = getAttachments(wellId, wellboreId);
        return WitsmlMarshal.deserialize(attachments, ObjAttachments.class);
    }

    /**
     * This method gets all of the ChangeLogs contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the ChangeLog
     * @param wellboreId The UID of the wellbore that contains the ChangeLog
     * @return The string representation of the WITSML of the ChangeLogs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getChangeLogs(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the ChangeLogs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the ChangeLog
     * @param wellboreId The UID of the wellbore that contains the ChangeLog
     * @return A POJO representing the ChangeLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjChangeLogs getChangeLogsAsObj(String wellId, String wellboreId) throws Exception {
        String changeLogs = getChangeLogs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(changeLogs, ObjChangeLogs.class);
    }

    /**
     * This method gets all of the DrillReports contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the DrillReport
     * @param wellboreId The UID of the wellbore that contains the DrillReport
     * @return The string representation of the WITSML of the DrillReports contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getDrillReports(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the DrillReports contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the DrillReport
     * @param wellboreId The UID of the wellbore that contains the DrillReport
     * @return A POJO representing the DrillReports contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjDrillReports getDrillReportsAsObj(String wellId, String wellboreId) throws Exception {
        String drillReports = getDrillReports(wellId, wellboreId);
        return WitsmlMarshal.deserialize(drillReports, ObjDrillReports.class);
    }

    /**
     * This method gets all of the ObjectGroups contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the ObjectGroup
     * @param wellboreId The UID of the wellbore that contains the ObjectGroup
     * @return The string representation of the WITSML of the ObjectGroups contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getObjectGroups(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the ObjectGroups contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the ObjectGroup
     * @param wellboreId The UID of the wellbore that contains the ObjectGroup
     * @return A POJO representing the ObjectGroups contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjObjectGroups getObjectGroupsAsObj(String wellId, String wellboreId) throws Exception {
        String objectGroups = getObjectGroups(wellId, wellboreId);
        return WitsmlMarshal.deserialize(objectGroups, ObjObjectGroups.class);
    }

    /**
     * This method gets all of the StimJobs contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the StimJob
     * @param wellboreId The UID of the wellbore that contains the StimJob
     * @return The string representation of the WITSML of the StimJobs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getStimJobs(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the StimJobs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the StimJob
     * @param wellboreId The UID of the wellbore that contains the StimJob
     * @return A POJO representing the StimJobs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjStimJobs getStimJobsAsObj(String wellId, String wellboreId) throws Exception {
        String stimJobs = getStimJobs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(stimJobs, ObjStimJobs.class);
    }

    /**
     * This method gets all of the Realtimes contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the Realtime
     * @param wellboreId The UID of the wellbore that contains the Realtime
     * @return The string representation of the WITSML of the Realtimes contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getRealtimes(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the Realtimes contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Realtime
     * @param wellboreId The UID of the wellbore that contains the Realtime
     * @return A POJO representing the Realtimes contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjRealtimes getRealtimesAsObj(String wellId, String wellboreId) throws Exception {
        String realtimes = getRealtimes(wellId, wellboreId);
        return WitsmlMarshal.deserialize(realtimes, ObjRealtimes.class);
    }

    /**
     * This method gets all of the WellLogs contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the WellLog
     * @param wellboreId The UID of the wellbore that contains the WellLog
     * @return The string representation of the WITSML of the WellLog contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getWellLogs(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the WellLogs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the WellLog
     * @param wellboreId The UID of the wellbore that contains the WellLog
     * @return A POJO representing the WellLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWellLogs getWellLogsAsObj(String wellId, String wellboreId) throws Exception {
        String wellLogs = getWellLogs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(wellLogs, ObjWellLogs.class);
    }

    /**
     * This method gets all of the DtsMeasurements contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the DtsMeasurement
     * @param wellboreId The UID of the wellbore that contains the DtsMeasurement
     * @return The string representation of the WITSML of the DtsMeasurement contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getDtsMeasurements(String wellId, String wellboreId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the DtsMeasurements contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the DtsMeasurement
     * @param wellboreId The UID of the wellbore that contains the DtsMeasurement
     * @return A POJO representing the DtsMeasurements contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjDtsMeasurements getDtsMeasurementsAsObj(String wellId, String wellboreId) throws Exception {
        String dtsMeasurements = getDtsMeasurements(wellId, wellboreId);
        return WitsmlMarshal.deserialize(dtsMeasurements, ObjDtsMeasurements.class);
    }

    /**
     * This method gets all of the TrajectoryStations contained within a wellbore
     * @param wellId The UID of thw well that contains the wellbore that contains the TrajectoryStation
     * @param wellboreId The UID of the wellbore that contains the TrajectoryStation
     * @return The string representation of the WITSML of the TrajectoryStations contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTrajectoryStations(String wellId, String wellboreId, String trajectoryId) throws Exception {
        return "";
    }

    /**
     * This method gets all of the TrajectoryStations contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Trajectory Staion
     * @param wellboreId The UID of the wellbore that contains the Trajectory Station
     * @param trajectoryId The UID of the Trajectory that contains the Trajectory Station
     * @return A POJO representing the Trajectory Station contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTrajectoryStations getTrajectoryStationsAsObj(String wellId, String wellboreId, String trajectoryId) throws Exception {
        String trajectoryStations = getTrajectoryStations(wellId, wellboreId, trajectoryId);
        return WitsmlMarshal.deserialize(trajectoryStations, ObjTrajectoryStations.class);
    }

    private String getReturnData(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
