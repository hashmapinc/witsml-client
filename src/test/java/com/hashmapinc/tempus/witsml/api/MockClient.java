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
import com.hashmapinc.tempus.witsml.client.WitsmlQuery;
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
     * Sets up the client with the URL of the STORE API
     * @param url The URL of the STORE API
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
    @Override
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Sets the version that will be used for querying
     * @param version The version string that will be used
     */
    @Override
    public void setVersion(WitsmlVersion version){
        this.version = version;
    }

    /**
     * This method queries the server and returns the supported WITSML version in a comma delimited string.
     * @return A comma-delimited list of supported version from the server, will return null if an error occured.
     */
    @Override
    public String getVersion(){
        return supportedApiVersions;
    }

    /**
     * Sets the version and validates the connection information.
     */
    @Override
    public void connect() {
        connected = true;
    }

    /**
     * Returns the capabilites that are supported by the remote server
     * @return The XML document representing the capabilities of the server
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
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

    @Override
    public String getObjectQuery(String objectType) throws IOException {
        return "";
    }

    @Override
    public WitsmlResponse getObjectData(WitsmlQuery witsmlQuery) throws FileNotFoundException, RemoteException{
        String query = "";
        String optionsIn = "";
        try {
            query = getObjectQuery(witsmlQuery.getObjectType());
            query = witsmlQuery.apply(query);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getWells " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        return executeObjectQuery(witsmlQuery.getObjectType(), query, optionsIn, "");
    }

    /**
     * This method gets all the wells on the server that the user has access to
     * @return Returns a POJO that represents the Obj_wells XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWells getWellsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse wellsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(wellsResponse.getXmlOut(), ObjWells.class);
    }

    /**
     * This method gets all the wellbores on the server that the user has access to under the specified well
     * @param witsmlQuery
     * @return Returns a POJO that represents the Obj_wellbores XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWellbores getWellboresForWellAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse wellboresResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(wellboresResponse.getXmlOut(), ObjWellbores.class);
    }

    /**
     * Executes a object query in either 1.3.1.1 or 1.4.1.1
     * @param objectType The type of WMLtypeIn object
     * @param query The query to send to the server
     * @param optionsIn The options to send (only supported in 1.4.1.1)
     * @param capabilitiesIn The capabilities to send (only supported in 1.4.1.1)
     * @return a string representing the response from the server
     * @throws RemoteException thrown on any exception encountered from the server
     */
    @Override
    public WitsmlResponse executeObjectQuery(String objectType, String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        return new com.hashmapinc.tempus.witsml.client.WitsmlResponse("","", (short)0);
    }

    @Override
    public ObjLogs getLogsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse logsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(logsResponse.getXmlOut(), ObjLogs.class);
    }

    /**
     * This method gets all of the mudlogs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the MudLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjMudLogs getMudLogsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse mudLogsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(mudLogsResponse.getXmlOut(), ObjMudLogs.class);
    }

    /**
     * This method gets all of the trajectorys contained within a wellbore and returns the metadata (minus the <data></data> element
     * @param witsmlQuery
     * @return A POJO representing the Trajectorys contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTrajectorys getTrajectorysAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse trajectorysResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(trajectorysResponse.getXmlOut(), ObjTrajectorys.class);
    }

    /**
     * This method gets all of the bhaRuns contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the BhaRuns contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjBhaRuns getBhaRunsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse bhaRunsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(bhaRunsResponse.getXmlOut(), ObjBhaRuns.class);
    }

    /**
     * This method gets all of the cementJobs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the CementJobs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjCementJobs getCementJobsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse cementJobResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(cementJobResponse.getXmlOut(), ObjConvCores.class);
    }

    /**
     * This method gets all of the convCores contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the ConvCores contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjConvCores getConvCoresAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse convCoresResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(convCoresResponse.getXmlOut(), ObjConvCores.class);
    }

    /**
     * This method gets all of the DtsInstalledSystems contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the DtsInstalledSystems contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjDtsInstalledSystems getDtsInstalledSystemsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse dtsInstalledSystemsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(dtsInstalledSystemsResponse.getXmlOut(), ObjDtsInstalledSystems.class);
    }

    /**
     * This method gets all of the FluidsReports contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the FluidsReports contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjFluidsReports getFluidsReportsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse fluidsReportsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(fluidsReportsResponse.getXmlOut(), ObjFluidsReports.class);
    }

    /**
     * This method gets all of the FormationMarkers contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the FormationMarkers contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjFormationMarkers getFormationMarkersAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse formationMarkersResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(formationMarkersResponse.getXmlOut(), ObjFormationMarkers.class);
    }

    /**
     * This method gets all of the Messages contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Messages contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjMessages getMessagesAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse messagesResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(messagesResponse.getXmlOut(), ObjMessages.class);
    }

    /**
     * This method gets all of the OpsReport contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the OpsReports contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjOpsReports getOpsReportsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse opsReportsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(opsReportsResponse.getXmlOut(), ObjOpsReports.class);
    }

    /**
     * This method gets all of the Rigs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Rigs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjRigs getRigsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse rigsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(rigsResponse.getXmlOut(), ObjRigs.class);
    }

    /**
     * This method gets all of the Risks contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Risks contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjRisks getRisksAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse risksResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(risksResponse.getXmlOut(), ObjRisks.class);
    }

    /**
     * This method gets all of the SideWallCores contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the SideWallCores contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjSidewallCores getSideWallCoresAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse sideWallCoresResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(sideWallCoresResponse.getXmlOut(), ObjSidewallCores.class);
    }

    /**
     * This method gets all of the SurveyPrograms contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the SurveyPrograms contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjSurveyPrograms getSurveyProgramsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse sideWallCoresResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(sideWallCoresResponse.getXmlOut(), ObjSurveyPrograms.class);
    }

    /**
     * This method gets all of the Targets contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Targets contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTargets getTargetsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse targetsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(targetsResponse.getXmlOut(), ObjTargets.class);
    }

    /**
     * This method gets all of the Tubulars contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Tubulars contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTubulars getTubularsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse tubularsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(tubularsResponse.getXmlOut(), ObjTubulars.class);
    }

    /**
     * This method gets all of the WbGeometrys contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the WbGeometrys contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWbGeometrys getWbGeometrysAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse wbGeometrysResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(wbGeometrysResponse.getXmlOut(), ObjWbGeometrys.class);
    }

    /**
     * This method gets all of the Attachments contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Attachments contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjAttachments getAttachmentsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse attachmentsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(attachmentsResponse.getXmlOut(), ObjAttachments.class);
    }

    /**
     * This method gets all of the ChangeLogs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the ChangeLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjChangeLogs getChangeLogsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse changeLogsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(changeLogsResponse.getXmlOut(), ObjChangeLogs.class);
    }

    /**
     * This method gets all of the DrillReports contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the DrillReports contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjDrillReports getDrillReportsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse drillReportsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(drillReportsResponse.getXmlOut(), ObjDrillReports.class);
    }

    /**
     * This method gets all of the ObjectGroups contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the ObjectGroups contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjObjectGroups getObjectGroupsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse objectGroupsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(objectGroupsResponse.getXmlOut(), ObjObjectGroups.class);
    }

    /**
     * This method gets all of the StimJobs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the StimJobs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjStimJobs getStimJobsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse stimJobsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(stimJobsResponse.getXmlOut(), ObjStimJobs.class);
    }

    /**
     * This method gets all of the Realtimes contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Realtimes contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjRealtimes getRealtimesAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse realtimesResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(realtimesResponse.getXmlOut(), ObjRealtimes.class);
    }

    /**
     * This method gets all of the WellLogs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the WellLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWellLogs getWellLogsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse wellLogsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(wellLogsResponse.getXmlOut(), ObjWellLogs.class);
    }

    /**
     * This method gets all of the DtsMeasurements contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the DtsMeasurements contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjDtsMeasurements getDtsMeasurementsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse dtsMeasurementsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(dtsMeasurementsResponse.getXmlOut(), ObjDtsMeasurements.class);
    }

    /**
     * This method gets all of the TrajectoryStations contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Trajectory Station contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjTrajectoryStations getTrajectoryStationsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse trajectoryStationsResponse = getObjectData(witsmlQuery);
        return WitsmlMarshal.deserialize(trajectoryStationsResponse.getXmlOut(), ObjTrajectoryStations.class);
    }

    private String getReturnData(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
