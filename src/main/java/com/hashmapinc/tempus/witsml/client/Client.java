package com.hashmapinc.tempus.witsml.client;

import com.hashmapinc.tempus.witsml.api.WitsmlClient;
import com.hashmapinc.tempus.witsml.api.WitsmlVersion;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWellbores;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWells;
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
        if (!supportedVer.contains(version)){
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
        if (version.equals("1.4.1.1"))
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
        try {
            if (version.equals("1.3.1.1")) {
                query = getQuery("/1311/GetWellDetails.xml");
            }
            else if (version.equals("1.4.1.1")) {
                query = getQuery("/1411/GetAllWells.xml");
            }
        } catch (IOException e) {
            String error = "Could not find or access the query template for getWells " + e.getMessage();
            log.error(error);
            throw new FileNotFoundException(error);
        }
        StringHolder xmlResponse = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        try {
            witsmlClient.WMLS_GetFromStore("well", query, "", "", xmlResponse, suppMsgOut);

            if (version.equals("1.3.1.1"))
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
        try {

            if (version.equals("1.3.1.1")) {
                query = getQuery("/1311/GetAllWellboresForWell.xml");
            }
            else if (version.equals("1.4.1.1")){
                query = getQuery("/1411/GetWellboreDetails.xml");
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
            witsmlClient.WMLS_GetFromStore("wellbore", query, "", "", xmlResponse, suppMsgOut);
            if (version.equals("1.3.1.1"))
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
    @Override
    public String getLogMetadata(String wellId, String wellboreId) throws Exception {
        if (!connected)
            throw new Exception("The connect method has not yet been called.");
        String query = "";
        try {
            if (version.equals("1.3.1.1")) {
                query = getQuery("/1311/GetAllLogs.xml");
            }
            else if (version.equals("1.4.1.1")){
                query = getQuery("/1411/GetLogs.xml");
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
            witsmlClient.WMLS_GetFromStore("log", query, "", "", xmlResponse, suppMsgOut);
            if (version.equals("1.3.1.1"))
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
     * Executes a log query in either 1.3.1.1 or 1.4.1.1
     * @param query The query to send to the server
     * @param optionsIn The options to send (only supported in 1.4.1.1)
     * @param capabilitiesIn The capabilites to send (only supported in 1.4.1.1)
     * @return a string representing the response from the server
     * @throws RemoteException thrown on any exception encountered from the server
     */
    @Override
    public String executeLogQuery(String query, String optionsIn, String capabilitiesIn) throws RemoteException {
        StringHolder xmlOut = new StringHolder();
        StringHolder suppMsgOut = new StringHolder();
        witsmlClient.WMLS_GetFromStore("log", query, optionsIn, capabilitiesIn,  xmlOut, suppMsgOut);
        return xmlOut.toString();
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
    @Override
    public ObjLogs getLogMetadataAsObj(String wellId, String wellboreId) throws Exception {
        String logs = getLogMetadata(wellId, wellboreId);
        return WitsmlMarshal.deserialize(logs, ObjLogs.class);
    }

    private String getQuery(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
