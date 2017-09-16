package com.hashmapinc.tempus.witsml.client;

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
import com.hashmapinc.tempus.witsml.api.WitsmlClient;
import com.hashmapinc.tempus.witsml.api.WitsmlVersion;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.StringHolder;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Client implements WitsmlClient {

    private String url;
    private StoreSoapBindingStub witsmlClient;
    private Logger log;
    private WitsmlVersion version = WitsmlVersion.VERSION_1411;
    private List<String> supportedVersions;
    private Boolean connected = false;

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
     * Setup client
     * @param url
     */
    private void setupClient(String url) {
        WMLSLocator locator = new WMLSLocator();
        this.url = url;
        locator.setStoreSoapPortEndpointAddress(url);
        try {
            witsmlClient = (StoreSoapBindingStub) locator.getStoreSoapPort();
        } catch (ServiceException e) {
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
            return witsmlClient.WMLS_GetVersion();
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
        String supportedVersions = getVersion();
        String versions[] = supportedVersions.split(",");
        List<String> supportedVer = Arrays.asList(versions);
        if (!supportedVer.contains(version.toString())){
            throw new IllegalArgumentException("Requested version (" + version + ") not supported by the server. The server only supports: " + supportedVersions);
        }
        connected = true;
    }

    /**
     * Returns the capabilites that are supported by the remote server
     * @return The XML document representing the capabilities of the server
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getCapabilities() throws RemoteException {
        StringHolder capabilities = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        String optionsIn = "";
        if (version.toString().equals("1.4.1.1"))
            optionsIn = "dataVersion=1.4.1.1";
        witsmlClient.WMLS_GetCap(optionsIn, capabilities, suppMsgOut);
        return capabilities.value;
    }

    /**
     * This method gets all the wells on the server that the user has access to
     * @return String representation of all the wells the user has access to
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getWells(String wellUid, String status) throws FileNotFoundException, RemoteException, Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetWellsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")) {
                query = getQuery("/1411/GetWellsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
        } catch (IOException e) {
            String error = "Could not find or access the query template for getWells " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        query = query.replace("%uidWell%", wellUid);
        query = query.replace("%wellStatus%", status);

        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("well", query, optionsIn, "", xmlResponse, suppMsgOut);

            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open wells query " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String getWells() throws FileNotFoundException, RemoteException, Exception{
        return getWells("","");
    }

    /**
     * This method gets all the wells on the server that the user has access to
     * @return Returns a POJO that represents the Obj_wells XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
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
    @Override
    public String getWellboresForWell(String wellId, String wellboreUid) throws FileNotFoundException, RemoteException, Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {

            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetWellboresMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetWellboresMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }

            if (query.equals("")) {
                throw new IllegalStateException("The current version " + version + " is not supported by the wellbore query");
            }

            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreUid);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getWellboresForWell " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("wellbore", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open wellbores query " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String getWellboresForWell(String wellId) throws Exception {
        return getWellboresForWell(wellId, "");
    }

    /**
     * This method gets all the wellbores on the server that the user has access to under the specified well
     * @param wellId The UID of the well that we want to get wellbores for
     * @return Returns a POJO that represents the Obj_wellbores XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjWellbores getWellboresForWellAsObj(String wellId) throws Exception {
        String wellbores = getWellboresForWell(wellId);
        return WitsmlMarshal.deserialize(wellbores, ObjWellbores.class);
    }

    /**
     * This method gets all of the logs contained within a wellbore and returns the metadata (minus the <data></data> element
     * @param wellId The UID of the well that contains the wellbore that contains the log
     * @param wellboreId The UID of the wellbore that contains the log
     * @return The string representation of the WITSML of the Logs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getLogMetadata(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn="";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetLogsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetLogsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getLogMetadata " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("log", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open logs query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the mudlogs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the mudlog
     * @param wellboreId The UID of the wellbore that contains the mudlog
     * @return The string representation of the WITSML of the MudLogs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getMudLogs(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetMudLogsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetMudLogsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for GetMudLogs " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("mudLog", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open mudLog query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the trajectorys contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the trajectory
     * @param wellboreId The UID of the wellbore that contains the trajectory
     * @return The string representation of the WITSML of the Trajectorys contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTrajectorys(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn="";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetTrajectorysMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetTrajectorysMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getTrajectorys " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("trajectory", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open trajectory query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the banRuns contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the banRun
     * @param wellboreId The UID of the wellbore that contains the banRun
     * @return The string representation of the WITSML of the banRuns contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getBhaRuns(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetBhaRunsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetBhaRunsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getBhaRuns " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("bhaRun", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open bhaRuns query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the cementJobs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the cementJob
     * @param wellboreId The UID of the wellbore that contains the cementJob
     * @return The string representation of the WITSML of the cementJobs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getCementJobs(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetCementJobsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetCementJobsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getCementJobs " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("cementJob", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open cementJob query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the ConvCores contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the ConvCore
     * @param wellboreId The UID of the wellbore that contains the ConvCore
     * @return The string representation of the WITSML of the ConvCores contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getConvCores(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetConvCoreMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetConvCoreMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getConvCores " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("convCore", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open ConvCore query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the FluidsReports contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the FluidsReport
     * @param wellboreId The UID of the wellbore that contains the FluidsReport
     * @return The string representation of the WITSML of the FluidsReports contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getFluidsReports(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetFluidsReportsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetFluidsReportsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getFluidsReports " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("fluidsReport", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open fluidReports query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the FormationMarkers contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the FormationMarker
     * @param wellboreId The UID of the wellbore that contains the FormationMarker
     * @return The string representation of the WITSML of the FormationMarkers contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getFormationMarkers(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetFormationMarkerMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetFormationMarkerMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getFormationMarkers " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("formationMarker", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open formationMarker query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the Messages contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Message
     * @param wellboreId The UID of the wellbore that contains the Message
     * @return The string representation of the WITSML of the Messages contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getMessages(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetMessageData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetMessageData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getMessages " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("message", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open message query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the OpsReports contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the OpsReport
     * @param wellboreId The UID of the wellbore that contains the OpsReport
     * @return The string representation of the WITSML of the OpsReports contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getOpsReports(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetOpsReportsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetOpsReportsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getOpsReports " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("opsReport", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open opsReport query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the Rigs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Rig
     * @param wellboreId The UID of the wellbore that contains the Rig
     * @return The string representation of the WITSML of the Rigs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getRigs(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetRigsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetRigsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getRigs " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("rig", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open Rigs query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the Risks contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Risk
     * @param wellboreId The UID of the wellbore that contains the Risk
     * @return The string representation of the WITSML of the Risks contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getRisks(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetRisksMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetRisksMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getRisks " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("risk", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open Risks query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the SideWallCores contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the SideWallCore
     * @param wellboreId The UID of the wellbore that contains the SideWallCore
     * @return The string representation of the WITSML of the SideWallCores contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getSideWallCores(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetSideWallCoresMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetSideWallCoresMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getSideWallCores " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("sidewallCore", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open SideWallCores query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the SurveyPrograms contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the SurveyProgram
     * @param wellboreId The UID of the wellbore that contains the SurveyProgram
     * @return The string representation of the WITSML of the SurveyPrograms contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getSurveyPrograms(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetSurveyProgramsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetSurveyProgramsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getSurveyPrograms " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("surveyProgram", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open SurveyPrograms query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the Targets contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Target
     * @param wellboreId The UID of the wellbore that contains the Target
     * @return The string representation of the WITSML of the Targets contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTargets(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetTargetsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetTargetsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getTargets " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("target", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open Targets query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the Tubulars contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Tubular
     * @param wellboreId The UID of the wellbore that contains the Tubular
     * @return The string representation of the WITSML of the Tubulars contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTubulars(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetTubularsMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetTubularsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getTubulars " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("tubular", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open Tubulars query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the WbGeometrys contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the WbGeometry
     * @param wellboreId The UID of the wellbore that contains the WbGeometry
     * @return The string representation of the WITSML of the WbGeometrys contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getWbGeometrys(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetWbGeometrysMetaData.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetWbGeometrysMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getWbGeometrys " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("wbGeometry", query, optionsIn, "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open WbGeometrys query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the Attachments contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Attachment
     * @param wellboreId The UID of the wellbore that contains the Attachment
     * @return The string representation of the WITSML of the Attachments contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getAttachments(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetAttachmentsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getAttachments " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("attachment", query, optionsIn, "", xmlResponse, suppMsgOut);
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open Attachments query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the ChangeLogs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the ChangeLog
     * @param wellboreId The UID of the wellbore that contains the ChangeLog
     * @return The string representation of the WITSML of the ChangeLogs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getChangeLogs(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetChangeLogsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getChangeLogs " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("changeLog", query, optionsIn, "", xmlResponse, suppMsgOut);
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open ChangeLogs query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the DrillReports contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the DrillReport
     * @param wellboreId The UID of the wellbore that contains the DrillReport
     * @return The string representation of the WITSML of the DrillReports contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getDrillReports(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetDrillReportsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getDrillReports " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("drillReport", query, optionsIn, "", xmlResponse, suppMsgOut);
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open DrillReports query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the ObjectGroups contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the ObjectGroup
     * @param wellboreId The UID of the wellbore that contains the ObjectGroup
     * @return The string representation of the WITSML of the ObjectGroups contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getObjectGroups(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetObjectGroupsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getObjectGroups " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("objectGroup", query, optionsIn, "", xmlResponse, suppMsgOut);
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open ObjectGroups query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the StimJobs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the StimJob
     * @param wellboreId The UID of the wellbore that contains the StimJob
     * @return The string representation of the WITSML of the StimJobs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getStimJobs(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetStimJobsMetaData.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getStimJobs " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("stimJob", query, optionsIn, "", xmlResponse, suppMsgOut);
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open StimJobs query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the dtsInstalledSystems contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the dtsInstalledSystem
     * @param wellboreId The UID of the wellbore that contains the dtsInstalledSystem
     * @return The string representation of the WITSML of the dtsInstalledSystems contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getDtsInstalledSystems(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetDtsInstalledSystemsMetaData.xml");
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getDtsInstalledSystems " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("dtsInstalledSystem", query, "", "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open dtsInstalledSystem query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the Realtimes contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the Realtime
     * @param wellboreId The UID of the wellbore that contains the Realtime
     * @return The string representation of the WITSML of the Realtimes contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getRealtimes(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetRealTimesMetaData.xml");
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getRealtimes " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("realtime", query, "", "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open Realtimes query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the DtsMeasurements contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the DtsMeasurement
     * @param wellboreId The UID of the wellbore that contains the DtsMeasurements
     * @return The string representation of the WITSML of the Realtimes contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getDtsMeasurements(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetDtsMeasurementsMetaData.xml");
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getDtsMeasurements " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("dtsMeasurement", query, "", "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open DtsMeasurements query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the WellLogs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the WellLog
     * @param wellboreId The UID of the wellbore that contains the WellLog
     * @return The string representation of the WITSML of the WellLog contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getWellLogs(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetWellLogsMetaData.xml");
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getWellLogs " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("wellLog", query, "", "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open WellLogs query " + e.getMessage());
            throw e;
        }
    }

    /**
     * This method gets all of the TrajectoryStations contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the trajectoryStation
     * @param wellboreId The UID of the wellbore that contains the trajectoryStation
     * @param trajectoryId The UID of the trajectory that contains the trajectoryStation
     * @return The string representation of the WITSML of the TrajectoryStations contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     */
    @Override
    public String getTrajectoryStations(String wellId, String wellboreId, String trajectoryId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetTrajectoryStationsMetaData.xml");
            }
            query = query.replace("%uidWell%", wellId);
            query = query.replace("%uidWellbore%", wellboreId);
            query = query.replace("%uidTrajectory%", trajectoryId);
        } catch (IOException e) {
            String error = "Could not find or access the query template for getTrajectoryStations " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("trajectoryStation", query, "", "", xmlResponse, suppMsgOut);
            if (version.toString().equals("1.3.1.1"))
                try {
                    return transform.convertVersion(xmlResponse.value);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            return xmlResponse.value;
        } catch (RemoteException e) {
            log.error("Error while executing open TrajectoryStations query " + e.getMessage());
            throw e;
        }
    }

    /**
     * Executes a log query in either 1.3.1.1 or 1.4.1.1
     * @param query The query to send to the server
     * @param optionsIn The options to send (only supported in 1.4.1.1)
     * @param capabilitiesIn The capabilities to send (only supported in 1.4.1.1)
     * @return a string representing the response from the server
     * @throws RemoteException thrown on any exception encountered from the server
     */
    @Override
    public String executeLogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        StringHolder xmlOut = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        witsmlClient.WMLS_GetFromStore("log", query, optionsIn, capabilitiesIn,  xmlOut, suppMsgOut);
        return xmlOut.value;
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
        StringHolder xmlOut = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        witsmlClient.WMLS_GetFromStore("mudLog", query, optionsIn, capabilitiesIn,  xmlOut, suppMsgOut);
        return xmlOut.value;
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
        StringHolder xmlOut = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        witsmlClient.WMLS_GetFromStore("trajectory", query, optionsIn, capabilitiesIn,  xmlOut, suppMsgOut);
        return xmlOut.value;
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
    public String executeObjectQuery(String objectType, String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        StringHolder xmlOut = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        witsmlClient.WMLS_GetFromStore(objectType.toLowerCase(), query, optionsIn, capabilitiesIn,  xmlOut, suppMsgOut);
        return xmlOut.value;
    }

    /**
     * This method gets all of the logs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the log
     * @param wellboreId The UID of the wellbore that contains the log
     * @return A POJO representing the Logs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjLogs getLogMetadataAsObj(String wellId, String wellboreId) throws Exception {
        String logs = getLogMetadata(wellId, wellboreId);
        return WitsmlMarshal.deserialize(logs, ObjLogs.class);
    }

    /**
     * This method gets all of the Mudlogs contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the mudlog
     * @param wellboreId The UID of the wellbore that contains the mudlog
     * @return A POJO representing the MudLogs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjMudLogs getMudLogsAsObj(String wellId, String wellboreId) throws Exception {
        String mudLogs = getMudLogs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(mudLogs, ObjMudLogs.class);
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
     * This method gets all of the banRuns contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the banRun
     * @param wellboreId The UID of the wellbore that contains the banRun
     * @return A POJO representing the banRuns contained withing the wellbore
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
     * This method gets all of the cementJob contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the cementJob
     * @param wellboreId The UID of the wellbore that contains the cementJob
     * @return A POJO representing the cementJobs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjCementJobs getCementJobsAsObj(String wellId, String wellboreId) throws Exception {
        String cementJobs = getCementJobs(wellId, wellboreId);
        return WitsmlMarshal.deserialize(cementJobs, ObjCementJobs.class);
    }

    /**
     * This method gets all of the convCores contained within a wellbore
     * @param wellId The UID of the well that contains the wellbore that contains the convCore
     * @param wellboreId The UID of the wellbore that contains the convCore
     * @return A POJO representing the convCores contained withing the wellbore
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
     * This method gets all of the OpsReports contained within a wellbore
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
     * @param wellId The UID of the well that contains the wellbore that contains the SurveyPrograms
     * @param wellboreId The UID of the wellbore that contains the SurveyProgram
     * @return A POJO representing the SurveyPrograms contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     */
    @Override
    public ObjSurveyPrograms getSurveyProgramsAsObj(String wellId, String wellboreId) throws Exception {
        String surveyPrograms = getSurveyPrograms(wellId, wellboreId);
        return WitsmlMarshal.deserialize(surveyPrograms, ObjSurveyPrograms.class);
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

    private String getQuery(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
