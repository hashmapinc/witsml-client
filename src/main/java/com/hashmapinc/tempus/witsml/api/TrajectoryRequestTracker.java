package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import com.hashmapinc.tempus.WitsmlObjects.v1411.*;
import com.hashmapinc.tempus.witsml.client.WitsmlQuery;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TrajectoryRequestTracker extends AbstractRequestTracker{

    private WitsmlClient witsmlClient;
    private WitsmlVersionTransformer transformer;
    private String wellId;
    private String wellboreId;
    private String trajectoryId;
    private ZonedDateTime lastQueryTime = null;
    private double lastMeasuredDepth = -1;
    private boolean fullQuery = true;

    public void setFullQuery(boolean fullQuery) { this.fullQuery = fullQuery; }
    public void setTrajectoryId(String trajectoryId) { this.trajectoryId = trajectoryId; }
    public void setLastMeasuredDepth(double lastMeasuredDepth) {this.lastMeasuredDepth = lastMeasuredDepth;}
    public double getLastMeasuredDepth() { return lastMeasuredDepth; }

    @Override
    public void initalize(WitsmlClient witsmlClient, String wellId, String wellboreId) {
        try {
            transformer = new WitsmlVersionTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        this.witsmlClient = witsmlClient;
        this.wellId = wellId;
        this.wellboreId = wellboreId;
    }

    @Override
    public ObjTrajectorys ExecuteRequest() {
        ObjTrajectorys trajectorys = null;
        WitsmlResponse trajectoryResponse;
        String response = null;

        try {
            trajectoryResponse = executeQuery();
            response = trajectoryResponse.getXmlOut();

            if (getVersion() == WitsmlVersion.VERSION_1311) {
                try {
                    response = transformer.convertVersion(response);
                } catch (TransformerException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            trajectorys = WitsmlMarshal.deserialize(response, ObjTrajectorys.class);
            if (trajectorys == null || trajectorys.getTrajectory().isEmpty()) {
                return null;
            }
            lastMeasuredDepth = getLastMeasuredDepth(trajectorys.getTrajectory().get(0));
            setFullQuery(false);


        } catch (JAXBException | RemoteException e) {
            e.printStackTrace();
        }
        return trajectorys;
    }

    private WitsmlResponse executeQuery() throws RemoteException {
        lastQueryTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        String query = "";
        setOptionsIn("");
        try {
            if (getVersion().toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetTrajectoryData.xml");
            } else if (getVersion().toString().equals("1.4.1.1")) {
                query = getQuery("/1411/GetTrajectoryData.xml");
                setOptionsIn("dataVersion=1.4.1.1");
            }
        } catch (Exception ex) {
            System.out.println("Error in getQuery ex : " + ex);
        }
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setBulkData(true);
        witsmlQuery.setObjectType("trajectory");
        witsmlQuery.addAttributeConstraint("trajectory", "uidWell", wellId);
        witsmlQuery.addAttributeConstraint("trajectory", "uidWellbore", wellboreId);
        witsmlQuery.addAttributeConstraint("trajectory", "uid", trajectoryId);
        witsmlQuery.addElementConstraint("mdMn", getQueryStartMd());
        query = witsmlQuery.apply(query);
        setQuery(query);
        setCapabilitesIn("");
        return witsmlClient.executeObjectQuery(witsmlQuery.getObjectType(), getQuery(), getOptionsIn(), getCapabilitiesIn());
    }

    private String getQueryStartMd(){
        if (lastMeasuredDepth == -1 || fullQuery)
            return "";
        else if (lastMeasuredDepth > 1 )
            return String.valueOf(lastMeasuredDepth);
        return "";
    }

    private double getLastMeasuredDepth(ObjTrajectory trajectory) {
        List<CsTrajectoryStation> trajectoryStations = trajectory.getTrajectoryStation();
        double maxDepth = -1;
        for(CsTrajectoryStation station : trajectoryStations) {
            if (station.getMd() != null) {
                double depth = station.getMd().getValue();
                if (maxDepth < depth) {
                    maxDepth = depth;
                }
            }
        }
        return maxDepth;
    }

    private String getQuery(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
