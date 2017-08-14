package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLogs;
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

//public class MockClient implements WitsmlClient {

  /*  private String url;
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

    *//**
     * Setup client
     * @param url
     *//*
    private void setupClient(String url) {
        this.url = url;
    }

    *//**
     * Sets the URL for the WITSML STORE API Requests
     * @return URL being used to execute WITSML STORE API Requests
     *//*
    public String getUrl() {
        return url;
    }

    *//**
     * Sets the URL for the WITSML STORE API Requests
     * @param url URL to use for executing WITSML STORE API Requests
     *//*
    public void setUrl(String url){
        this.url = url;
    }

    *//**
     * Sets the user name for the WITSML STORE API Requests (sent as HTTP Basic)
     * @return  userName The user name being used to execute WITSML STORE API Requests
     *//*
    public String getUserName(){
        return userName;
    }

    *//**
     * Sets the user name for the WITSML STORE API Requests (sent as HTTP Basic)
     * @param userName The user name to use to execute WITSML STORE API Requests
     *//*
    public void setUserName(String userName){
        this.userName = userName;
    }

    *//**
     * Sets the password for the WITSML API requests (sent as HTTP Basic)
     * @param password The password to use
     *//*
    public void setPassword(String password){
        this.password = password;
    }

    *//**
     * Sets the version that will be used for querying
     * @param version The version string that will be used
     *//*
    public void setVersion(WitsmlVersion version){
        this.version = version;
    }

    *//**
     * This method queries the server and returns the supported WITSML version in a comma delimited string.
     * @return A comma-delimited list of supported version from the server, will return null if an error occured.
     *//*
    public String getVersion(){
        return supportedApiVersions;
    }

    *//**
     * Sets the version and validates the connection information.
     *//*
    public void connect() {
        connected = true;
    }

    *//**
     * Returns the capabilites that are supported by the remote server
     * @return The XML document representing the capabilities of the server
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     *//*
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

    *//**
     * This method gets all the wells on the server that the user has access to
     * @return String representation of all the wells the user has access to
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     *//*
    public String getWells() throws FileNotFoundException, RemoteException, Exception {
        return "";
    }

    *//**
     * This method gets all the wells on the server that the user has access to
     * @return Returns a POJO that represents the Obj_wells XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     *//*
    public ObjWells getWellsAsObj() throws Exception {
        String wells = getWells();
        return WitsmlMarshal.deserialize(wells, ObjWells.class);
    }

    *//**
     * This method gets all the wellbores on the server that the user has access to under the specified well
     * @param wellId The UID of the well that we want to get wellbores for
     * @return String representation of all the wellbores the user has access to under the specified Well
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     *//*
    public String getWellboresForWell(String wellId) throws FileNotFoundException, RemoteException, Exception {
        return "";
    }

    *//**
     * This method gets all the wellbores on the server that the user has access to under the specified well
     * @param wellId The UID of the well that we want to get wellbores for
     * @return Returns a POJO that represents the Obj_wellbores XSD
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     *//*
    public ObjWellbores getWellboresForWellAsObj(String wellId) throws Exception {
        String wellbores = getWellboresForWell(wellId);
        return WitsmlMarshal.deserialize(wellbores, ObjWellbores.class);
    }

    *//**
     * This method gets all of the logs contained within a wellbore and returns the metadata (minus the <data></data> element
     * @param wellId The UID of thw well that contains the wellbore that contains the log
     * @param wellboreId The UID of the wellbore that contains the log
     * @return The string representation of the WITSML of the Logs contained within the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     *//*
    public String getLogMetadata(String wellId, String wellboreId) throws Exception {
        return "";
    }

    *//**
     * Executes a log query in either 1.3.1.1 or 1.4.1.1
     * @param query The query to send to the server
     * @param optionsIn The options to send (only supported in 1.4.1.1)
     * @param capabilitiesIn The capabilites to send (only supported in 1.4.1.1)
     * @return a string representing the response from the server
     * @throws RemoteException thrown on any exception encountered from the server
     *//*
    public String executeLogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        return "";
    }

    *//**
     * This method gets all of the logs contained within a wellbore and returns the metadata (minus the <data></data> element
     * @param wellId The UID of thw well that contains the wellbore that contains the log
     * @param wellboreId The UID of the wellbore that contains the log
     * @return A POJO representing the Logs contained withing the wellbore
     * @throws FileNotFoundException Thrown if there is a problem accessing the query template
     * @throws RemoteException Thrown if there is an exception on the remote WITSML STORE API
     * @throws JAXBException Thrown if there is an error parsing the return.
     *//*
    public ObjLogs getLogMetadataAsObj(String wellId, String wellboreId) throws Exception {
        String logs = getLogMetadata(wellId, wellboreId);
        return WitsmlMarshal.deserialize(logs, ObjLogs.class);
    }

    private String getReturnData(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }*/
}
