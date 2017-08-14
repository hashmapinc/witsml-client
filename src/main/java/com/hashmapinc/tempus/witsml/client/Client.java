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
            throw new IllegalArgumentException("Requested version not supported by the server");
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
    public String getWells() throws FileNotFoundException, RemoteException, Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {
            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetWells.xml");
            }
            else if (version.toString().equals("1.4.1.1")) {
                query = getQuery("/1411/GetWells.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }
        } catch (IOException e) {
            String error = "Could not find or access the query template for getWells " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
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
//        System.out.println("WELLS : \n" + wells);
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
    public String getWellboresForWell(String wellId) throws FileNotFoundException, RemoteException, Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        String optionsIn = "";
        try {

            if (version.toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetWellbores.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetWellbores.xml");
                optionsIn = "dataVersion=1.4.1.1";
            }

            if (query.equals("")) {
                throw new IllegalStateException("The current version " + version + " is not supported by the wellbore query");
            }

            query = query.replace("%uidWell%", wellId);
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
//        System.out.println("WELLBORE : " + wellbores);
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
                query = getQuery("/1311/GetLogs.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetLogs.xml");
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
                query = getQuery("/1311/GetMudLogs.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetMudLogs.xml");
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
                query = getQuery("/1311/GetTrajectorys.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetTrajectorys.xml");
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
                query = getQuery("/1311/GetBhaRuns.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetBhaRuns.xml");
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
                query = getQuery("/1311/GetCementJobs.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetCementJobs.xml");
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
                query = getQuery("/1311/GetConvCore.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetConvCore.xml");
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
                query = getQuery("/1311/GetFluidsReports.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetFluidsReports.xml");
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
                query = getQuery("/1311/GetFormationMarker.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetFormationMarker.xml");
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
                query = getQuery("/1311/GetMessages.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetMessages.xml");
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
                query = getQuery("/1311/GetOpsReports.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetOpsReports.xml");
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
                query = getQuery("/1311/GetRigs.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetRigs.xml");
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
                query = getQuery("/1311/GetRisks.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetRisks.xml");
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
                query = getQuery("/1311/GetSideWallCores.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetSideWallCores.xml");
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
                query = getQuery("/1311/GetSurveyPrograms.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetSurveyPrograms.xml");
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
                query = getQuery("/1311/GetTargets.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetTargets.xml");
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
                query = getQuery("/1311/GetTubulars.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetTubulars.xml");
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
                query = getQuery("/1311/GetWbGeometrys.xml");
            }
            else if (version.toString().equals("1.4.1.1")){
                query = getQuery("/1411/GetWbGeometrys.xml");
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
                query = getQuery("/1411/GetAttachments.xml");
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
                query = getQuery("/1411/GetChangeLogs.xml");
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
                query = getQuery("/1411/GetDrillReports.xml");
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
                query = getQuery("/1411/GetObjectGroups.xml");
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
                query = getQuery("/1411/GetStimJobs.xml");
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
                query = getQuery("/1311/GetDtsInstalledSystems.xml");
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
                query = getQuery("/1311/GetRealTimes.xml");
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
                query = getQuery("/1311/GetDtsMeasurements.xml");
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
                query = getQuery("/1311/GetWellLogs.xml");
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
                query = getQuery("/1311/GetTrajectoryStations.xml");
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
//        System.out.println("LOGS : " + logs);
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
//        System.out.println("MUDLOGS : " + mudLogs);
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
//        System.out.println("Trajectorys : " + trajectorys);
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
//        System.out.println("BhaRuns : " + bhaRuns);
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
//        System.out.println("CementJobs : " + cementJobs);
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
//        System.out.println("ConvCores : " + convCores);
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
//        System.out.println("DtsInstalledSystems : " + dtsInstalledSystems);
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
//        System.out.println("fluidsReport : " + fluidsReports);
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
//        System.out.println("FormationMarker : " + formationMarkers);
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
//        System.out.println("Messages : " + messages);
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
//        System.out.println("OpsReports : " + opsReports);
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
//        System.out.println("Rigs : " + rigs);
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
//        System.out.println("Risks : " + risks);
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
//        System.out.println("SideWallCores : " + sideWallCores);
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
//        System.out.println("SurveyPrograms : " + surveyPrograms);
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
//        System.out.println("SurveyPrograms : " + tubulars);
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
//        System.out.println("Targets : " + targets);
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
//        System.out.println("WbGeometrys : " + wbGeometrys);
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
//        System.out.println("Attachments : " + attachments);
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
//        System.out.println("ChangeLogs : " + changeLogs);
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
//        System.out.println("DrillReports : " + drillReports);
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
//        System.out.println("ObjectGroups : " + objectGroups);
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
//        System.out.println("StimJobs : " + stimJobs);
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
//        System.out.println("Realtimes : " + realtimes);
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
//        System.out.println("WellLogs : " + wellLogs);
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
//        System.out.println("DtsMeasurements : " + dtsMeasurements);
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
//        System.out.println("TrajectoryStations : " + trajectoryStations);
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
