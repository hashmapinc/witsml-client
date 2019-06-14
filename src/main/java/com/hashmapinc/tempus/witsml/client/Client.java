package com.hashmapinc.tempus.witsml.client;

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
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjRisks;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSidewallCores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjSurveyPrograms;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTargets;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTrajectorys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjTubulars;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWbGeometrys;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWellbores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWells;
import com.hashmapinc.tempus.witsml.api.ObjectType;
import com.hashmapinc.tempus.witsml.api.WitsmlClient;
import com.hashmapinc.tempus.witsml.api.WitsmlVersion;
import com.hashmapinc.tempus.witsml.message._120.*;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.impl.httpclient4.HttpTransportPropertiesImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Client implements WitsmlClient {

    private String url;
    private AbstractStoreSoapBindingStub witsmlClient;
    private Logger log;
    private WitsmlVersion version = WitsmlVersion.VERSION_1411;

    private WitsmlVersionTransformer transform;

    public Client(String url){
        log = LogManager.getLogger(Client.class);
        setupClient(url);
        try {
            transform = new WitsmlVersionTransformer();
        } catch (TransformerConfigurationException e) {
            log.error("Error setting up XSLT Transformer " + e.getMessage());
        }
    }

    /**
     * Client Constructor for Unit testing
     * @param storeSoapTestClient Object of MockStoreSoapBindingStub to mock the Store Soap
     * @param url dummy url for unit test
     *
     */
    public Client(AbstractStoreSoapBindingStub storeSoapTestClient, String url) {
        log = LogManager.getLogger(Client.class);
        this.url = url;
        witsmlClient = storeSoapTestClient;
        try {
            transform = new WitsmlVersionTransformer();
        } catch (TransformerConfigurationException e) {
            log.error("Error setting up XSLT Transformer " + e.getMessage());
        }
    }

    /**
     * Setup client
     * @param url URL for the WITSML API
     */
    private void setupClient(String url) {
        WMLSLocator locator = new WMLSLocator();
        this.url = url;
        locator.setStoreSoapPortEndpointAddress(url);
        try {
            witsmlClient = (StoreSoapBindingStub) locator.getStoreSoapPort();
        } catch (Exception e) {
            log.error("Error in setupClient in api.Client " + e.getMessage());
        }
    }

    /**
     * Sets the URL for the WITSML STORE API Requests
     * @return URL being used to execute WITSML STORE API Requests
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL for the WITSML STORE API Requests
     * @param url URL to use for executing WITSML STORE API Requests
     */
    @Override
    public void setUrl(String url){
        this.url = url;
    }

    /**
     * Sets the user name for the WITSML STORE API Requests (sent as HTTP Basic)
     * @return  userName The user name being used to execute WITSML STORE API Requests
     */
    @Override
    public String getUserName(){
        return witsmlClient.getUsername();
    }

    /**
     * Gets the password for the WITSML API requests (sent as HTTP Basic)
     * @return  password The password being used to execute WITSML STORE API Requests
     */
    @Override
    public String getPassword(){
        return witsmlClient.getPassword();
    }

    /**
     * Sets the user name for the WITSML STORE API Requests (sent as HTTP Basic)
     * @param userName The user name to use to execute WITSML STORE API Requests
     */
    @Override
    public void setUserName(String userName){
        witsmlClient.setUsername(userName);
    }

    /**
     * Sets the password for the WITSML API requests (sent as HTTP Basic)
     * @param password The password to use
     */
    @Override
    public void setPassword(String password){
        witsmlClient.setPassword(password);
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
        try {
            return witsmlClient.wMLS_GetVersion(new WMLSGetVersion()).getResult();
        } catch (RemoteException e) {
            log.error("Error in getVersion of api.Client " + e.getMessage());
            return null;
        }
    }

    /**
     * Sets the version and validates the connection information.
     */
    @Override
    public void connect() {
        if(getUserName() != null && !getUserName().isEmpty() && getPassword() != null && !getPassword().isEmpty()) {
            Options options = witsmlClient._getServiceClient().getOptions();
            HttpTransportPropertiesImpl.Authenticator auth = new HttpTransportPropertiesImpl.Authenticator();
            auth.setPreemptiveAuthentication(true);
            auth.setUsername(getUserName());
            auth.setPassword(getPassword());
            options.setProperty(HTTPConstants.AUTHENTICATE, auth);
        }

        String supportedVersions = getVersion();
        String versions[] = supportedVersions.split(",");
        List<String> supportedVer = Arrays.asList(versions);
        if (!supportedVer.contains(version.toString())){
            throw new IllegalArgumentException("Requested version (" + version + ") not supported by the server. The server only supports: " + supportedVersions);
        }
    }

    /**
     * Returns the capabilites that are supported by the remote server
     * @return The XML document representing the capabilities of the server
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getCapabilities() throws RemoteException {
        String optionsIn = "";
        if (version.toString().equals("1.4.1.1"))
            optionsIn = "dataVersion=1.4.1.1";

        WMLSGetCap wmlsGetCap = new WMLSGetCap();
        wmlsGetCap.setOptionsIn(optionsIn);
        WMLSGetCapResponse wmlsGetCapResponse = witsmlClient.wMLS_GetCap(wmlsGetCap);
        return wmlsGetCapResponse.getCapabilitiesOut();
    }

    public String getObjectQuery(String objectType) throws IOException {

        StringBuilder resourceStr = new StringBuilder();
        if (version.toString().equals("1.3.1.1")) {
            resourceStr.append("/1311/");
        } else {
            resourceStr.append("/1411/");
        }

        return getQuery(resourceStr.append(ObjectType.valueOf(objectType.toUpperCase()).toString()).toString());
    }

    private void handleBulkRequest(WitsmlQuery witsmlQuery) {
        if (!witsmlQuery.isBulkData()) {
            switch (witsmlQuery.getObjectType().toLowerCase()) {
                case "log" :
                    witsmlQuery.excludeElement("logData");
                    break;
                case "mudlog" :
                    witsmlQuery.excludeElement("geologyInterval");
                    break;
                case "trajectory" :
                    witsmlQuery.excludeElement("trajectoryStation");
                    break;
                default:
                    break;
            }
        }
    }

    public WitsmlResponse getObjectData(WitsmlQuery witsmlQuery) throws FileNotFoundException, RemoteException {
        String query = "";
        String optionsIn = "";
        try {
            query = getObjectQuery(witsmlQuery.getObjectType());
            handleBulkRequest(witsmlQuery);
            query = witsmlQuery.apply(query);
        } catch (IOException e) {
            String error = "Could not find or access the query template for Object :  " + witsmlQuery.getObjectType() + " : " + e.getMessage();
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
        String wells = convertVersion(wellsResponse.getXmlOut());
        if (wells.equals("")) return null;
        return WitsmlMarshal.deserialize(wells, ObjWells.class);
    }

    /**
     * This method gets all the wellbores on the server that the user has access to under the specified well
     * @param witsmlQuery The object of witsml query
     * @return Returns a POJO that represents the Obj_wellbores XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWellbores getWellboresForWellAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse wellboresResponse = getObjectData(witsmlQuery);
        String wellbores = convertVersion(wellboresResponse.getXmlOut());
        if (wellbores.equals("")) return null;
        return WitsmlMarshal.deserialize(wellbores, ObjWellbores.class);
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
        WMLSGetFromStore wmlsGetFromStore = new WMLSGetFromStore();
        wmlsGetFromStore.setWMLtypeIn(objectType);
        wmlsGetFromStore.setQueryIn(query);
        wmlsGetFromStore.setOptionsIn(optionsIn);
        wmlsGetFromStore.setCapabilitiesIn(capabilitiesIn);
        WMLSGetFromStoreResponse wmlsGetFromStoreResponse = witsmlClient.wMLS_GetFromStore(wmlsGetFromStore);
        return new WitsmlResponse(wmlsGetFromStoreResponse.getXMLout(), wmlsGetFromStoreResponse.getSuppMsgOut(), wmlsGetFromStoreResponse.getResult());
    }

    /**
     * This method gets all of the logs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the Logs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjLogs getLogsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse logsResponse = getObjectData(witsmlQuery);
        String logs = convertVersion(logsResponse.getXmlOut());
        if (logs == null) return null;
        return WitsmlMarshal.deserialize(logs, ObjLogs.class);
    }

    /**
     * This method gets all of the Mudlogs contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the MudLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjMudLogs getMudLogsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse mudLogsResponse = getObjectData(witsmlQuery);
        String mudLogs = convertVersion(mudLogsResponse.getXmlOut());
        if (mudLogs == null) return null;
        return WitsmlMarshal.deserialize(mudLogs, ObjMudLogs.class);
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
        String trajectorys = convertVersion(trajectorysResponse.getXmlOut());
        if (trajectorys == null) return null;
        return WitsmlMarshal.deserialize(trajectorys, ObjTrajectorys.class);
    }

    /**
     * This method gets all of the banRuns contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the banRuns contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjBhaRuns getBhaRunsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse bhaRunsResponse = getObjectData(witsmlQuery);
        String bhaRuns = convertVersion(bhaRunsResponse.getXmlOut());
        if (bhaRuns == null) return null;
        return WitsmlMarshal.deserialize(bhaRuns, ObjBhaRuns.class);
    }

    /**
     * This method gets all of the cementJob contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the cementJobs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjCementJobs getCementJobsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse cementJobsResponse = getObjectData(witsmlQuery);
        String cementJobs = convertVersion(cementJobsResponse.getXmlOut());
        if (cementJobs == null) return null;
        return WitsmlMarshal.deserialize(cementJobs, ObjCementJobs.class);
    }

    /**
     * This method gets all of the convCores contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the convCores contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjConvCores getConvCoresAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse convCoresResponse = getObjectData(witsmlQuery);
        String convCores = convertVersion(convCoresResponse.getXmlOut());
        if (convCores == null) return null;
        return WitsmlMarshal.deserialize(convCores, ObjConvCores.class);
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
        String dtsInstalledSystems = dtsInstalledSystemsResponse.getXmlOut();
        if (dtsInstalledSystems == null) return null;
        return WitsmlMarshal.deserialize(dtsInstalledSystems, ObjDtsInstalledSystems.class);
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
        String fluidsReports = convertVersion(fluidsReportsResponse.getXmlOut());
        if (fluidsReports == null) return null;
        return WitsmlMarshal.deserialize(fluidsReports, ObjFluidsReports.class);
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
        String formationMarkers = convertVersion(formationMarkersResponse.getXmlOut());
        if (formationMarkers == null) return null;
        return WitsmlMarshal.deserialize(formationMarkers, ObjFormationMarkers.class);
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
        String messages = convertVersion(messagesResponse.getXmlOut());
        if (messages == null) return null;
        return WitsmlMarshal.deserialize(messages, ObjMessages.class);
    }

    /**
     * This method gets all of the OpsReports contained within a wellbore
     * @param witsmlQuery
     * @return A POJO representing the OpsReports contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjOpsReports getOpsReportsAsObj(WitsmlQuery witsmlQuery) throws Exception {
        WitsmlResponse opsReportsResponse = getObjectData(witsmlQuery);
        String opsReports = convertVersion(opsReportsResponse.getXmlOut());
        if (opsReports == null) return null;
        return WitsmlMarshal.deserialize(opsReports, ObjOpsReports.class);
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
        String rigs = convertVersion(rigsResponse.getXmlOut());
        if (rigs == null) return null;
        return WitsmlMarshal.deserialize(rigs, ObjRigs.class);
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
        String risks = convertVersion(risksResponse.getXmlOut());
        if (risks == null) return null;
        return WitsmlMarshal.deserialize(risks, ObjRisks.class);
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
        String sideWallCores = convertVersion(sideWallCoresResponse.getXmlOut());
        if (sideWallCores == null) return null;
        return WitsmlMarshal.deserialize(sideWallCores, ObjSidewallCores.class);
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
        WitsmlResponse surveyProgramsResponse = getObjectData(witsmlQuery);
        String surveyPrograms = convertVersion(surveyProgramsResponse.getXmlOut());
        if (surveyPrograms == null) return null;
        return WitsmlMarshal.deserialize(surveyPrograms, ObjSurveyPrograms.class);
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
        String tubulars = convertVersion(tubularsResponse.getXmlOut());
        if (tubulars == null) return null;
        return WitsmlMarshal.deserialize(tubulars, ObjTubulars.class);
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
        String targets = convertVersion(targetsResponse.getXmlOut());
        if (targets == null) return null;
        return WitsmlMarshal.deserialize(targets, ObjTargets.class);
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
        String wbGeometrys = convertVersion(wbGeometrysResponse.getXmlOut());
        if (wbGeometrys == null) return null;
        return WitsmlMarshal.deserialize(wbGeometrys, ObjWbGeometrys.class);
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
        String attachments = attachmentsResponse.getXmlOut();
        if (attachments == null) return null;
        return WitsmlMarshal.deserialize(attachments, ObjAttachments.class);
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
        String changeLogs = changeLogsResponse.getXmlOut();
        if (changeLogs == null) return null;
        return WitsmlMarshal.deserialize(changeLogs, ObjChangeLogs.class);
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
        String drillReports = drillReportsResponse.getXmlOut();
        if (drillReports == null) return null;
        return WitsmlMarshal.deserialize(drillReports, ObjDrillReports.class);
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
        String objectGroups = objectGroupsResponse.getXmlOut();
        if (objectGroups == null) return null;
        return WitsmlMarshal.deserialize(objectGroups, ObjObjectGroups.class);
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
        String stimJobs = stimJobsResponse.getXmlOut();
        if (stimJobs == null) return null;
        return WitsmlMarshal.deserialize(stimJobs, ObjStimJobs.class);
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
        String realtimes = realtimesResponse.getXmlOut();
        if (realtimes == null) return null;
        return WitsmlMarshal.deserialize(realtimes, ObjRealtimes.class);
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
        String wellLogs = wellLogsResponse.getXmlOut();
        if (wellLogs == null) return null;
        return WitsmlMarshal.deserialize(wellLogs, ObjWellLogs.class);
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
        String dtsMeasurements = dtsMeasurementsResponse.getXmlOut();
        if (dtsMeasurements == null) return null;
        return WitsmlMarshal.deserialize(dtsMeasurements, ObjDtsMeasurements.class);
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
        String trajectoryStations = trajectoryStationsResponse.getXmlOut();
        if (trajectoryStations == null) return null;
        return WitsmlMarshal.deserialize(trajectoryStations, ObjTrajectoryStations.class);
    }

    private String getQuery(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }

    private String convertVersion(String original){
        String converted = null;
        if (version.toString().equals("1.3.1.1")) {
            try {
                converted = transform.convertVersion(original);
            } catch (TransformerException e) {
                log.error("error transforming the WITSML from 1.3.1.1 to 1.4.1.1: " + e.getMessage());
            }
        } else {
            return original;
        }
        if (converted == null) return null;
        if (converted.equals("")) return null;
        return converted;
    }
}
