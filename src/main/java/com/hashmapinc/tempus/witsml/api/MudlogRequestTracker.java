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
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class MudlogRequestTracker extends AbstractRequestTracker{

    private WitsmlClient witsmlClient;
    private WitsmlVersionTransformer transformer;
    private String wellId;
    private String wellboreId;
    private String mudlogId;
    private ZonedDateTime lastQueryTime = null;
    private double lastStartMd = -1;
    private boolean fullQuery = true;

    public void setFullQuery(boolean fullQuery) { this.fullQuery = fullQuery; }
    public void setMudlogId(String mudlogId) { this.mudlogId = mudlogId; }
    public void setLastStartMd(double lastStartMd) {this.lastStartMd = lastStartMd;}
    public double getLastStartMd() { return  lastStartMd; }

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
    public ObjMudLogs ExecuteRequest() {
        ObjMudLogs mudLogs = null;
        WitsmlResponse mudLogResponse;
        String response;

        try {
            mudLogResponse = executeQuery();
            response = mudLogResponse.getXmlOut();

            if (getVersion() == WitsmlVersion.VERSION_1311) {
                try {
                    response = transformer.convertVersion(response);
                } catch (TransformerException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            mudLogs = WitsmlMarshal.deserialize(response, ObjMudLogs.class);
            if (mudLogs == null || mudLogs.getMudLog().isEmpty()) {
                return null;
            }
            lastStartMd = getMinOfMdBottom(mudLogs.getMudLog().get(0));
            setFullQuery(false);

        } catch (JAXBException | RemoteException e) {
            e.printStackTrace();
        }
        return mudLogs;
    }

    private WitsmlResponse executeQuery() throws RemoteException {
        lastQueryTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        String query = "";
        setOptionsIn("");
        try {
            if (getVersion().toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetMudLogs.xml");
            } else if (getVersion().toString().equals("1.4.1.1")) {
                query = getQuery("/1411/GetMudLogs.xml");
                setOptionsIn("dataVersion=1.4.1.1");
            }
        } catch (Exception ex) {
            System.out.println("Error in getQuery ex : " + ex);
        }
        WitsmlQuery witsmlQuery = new WitsmlQuery();
        witsmlQuery.setBulkData(true);
        witsmlQuery.setObjectType("mudLog");
        witsmlQuery.addAttributeConstraint("mudLog", "uidWell", wellId);
        witsmlQuery.addAttributeConstraint("mudLog", "uidWellbore", wellboreId);
        witsmlQuery.addAttributeConstraint("mudLog", "uid", mudlogId);
        witsmlQuery.addElementConstraint("startMd", getQueryStartMd());
        query = witsmlQuery.apply(query);
        setQuery(query);
        setCapabilitesIn("");
        return witsmlClient.executeObjectQuery(witsmlQuery.getObjectType(), getQuery(), getOptionsIn(), getCapabilitiesIn());
    }

    private String getQueryStartMd(){
        if (lastStartMd == -1 || fullQuery)
            return "";
        else if (lastStartMd > 1)
            return String.valueOf(lastStartMd);
        return "";
    }

    private double getMinOfMdBottom(ObjMudLog mudLog) {
        List<CsGeologyInterval> geologyIntervals = mudLog.getGeologyInterval();
        DoubleSummaryStatistics summaryStat = geologyIntervals.stream()
                                              .map(CsGeologyInterval::getMdBottom)
                                              .mapToDouble(MeasuredDepthCoord::getValue)
                                              .summaryStatistics();

        double mdMax = -1;
        for (CsGeologyInterval geologyInterval : geologyIntervals) {
            if (geologyInterval.getMdBottom() != null) {
                double value = geologyInterval.getMdBottom().getValue();
                if (mdMax < value) {
                    mdMax = value;
                }
            }
        }
        return mdMax;
    }
    private String getQuery(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
