package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import com.hashmapinc.tempus.WitsmlObjects.v1411.*;

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
import java.time.format.DateTimeFormatter;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class LogRequestTracker extends AbstractRequestTracker{

    private WitsmlClient witsmlClient;
    private ZonedDateTime lastQueryTime = null;
    private ZonedDateTime lastLogTime = null;
    private LogIndexType indexType = null;
    private double lastLogDepth = -1;
    private WitsmlVersionTransformer transformer;
    private String wellId;
    private String wellboreId;
    private String logId;
    private boolean fullQuery = true;

    public void setFullQuery(boolean fullQuery) { this.fullQuery = fullQuery; }
    public void setLogId(String logId) { this.logId = logId; }
    public String getLogId() { return logId; }

    public double getLastLogDepth() { return lastLogDepth; }
    public ZonedDateTime getLastLogTime() { return lastLogTime; }

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
    public ObjLogs ExecuteRequest()  {
        ObjLogs logs = null;
        String response;

        try {
            response = executeQuery();

            if (getVersion() == WitsmlVersion.VERSION_1311) {
                try {
                    response = transformer.convertVersion(response);
                } catch (TransformerException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            logs = WitsmlMarshal.deserialize(response, ObjLogs.class);
            if (logs == null || logs.getLog().isEmpty()) {
                return null;
            }

            if (indexType == null)
                indexType = logs.getLog().get(0).getIndexType();

            switch (indexType) {
                case MEASURED_DEPTH:
                case VERTICAL_DEPTH: {
                    lastLogDepth = getMinOfMaxDepth(logs.getLog().get(0));
                    break;
                }
                case DATE_TIME: {
                    lastLogTime = getMinOfMaxTime(logs.getLog().get(0));
                }
            }
            setFullQuery(false);
        } catch (JAXBException | RemoteException e) {
            e.printStackTrace();
        }
        return logs;
    }

    private String executeQuery() throws RemoteException {
        lastQueryTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        String query = "";
        setOptionsIn("");
        try {
            if (getVersion().toString().equals("1.3.1.1")) {
                query = getQuery("/1311/GetLogData.xml");
            } else if (getVersion().toString().equals("1.4.1.1")) {
                query = getQuery("/1411/GetLogData.xml");
                setOptionsIn("dataVersion=1.4.1.1");
            }
        } catch (Exception ex) {
            System.out.println("Error in getQuery ex : " + ex);
        }
        query = query.replace("%uidWell%", wellId);
        query = query.replace("%uidWellbore%", wellboreId);
        query = query.replace("%uidLog%", logId);
        query = query.replace("%startIndex%", getQueryStartDepth());
        query = query.replace("%startDateTimeIndex%", getQueryStartTime());
        setQuery(query);
        setCapabilitesIn("");
        return witsmlClient.executeLogQuery(getQuery(), getOptionsIn(), getCapabilitiesIn());
    }

    private String getQueryStartDepth(){
        if (lastLogDepth == -1 || fullQuery)
            return "";
        else if (lastLogDepth > 1)
            return String.valueOf(lastLogDepth);
        return "";
    }

    private String getQueryStartTime(){
        if (lastLogTime == null || fullQuery)
            return "";
        else
            return lastLogTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private double getMinOfMaxDepth(ObjLog log){
        List<CsLogCurveInfo> curveInfos = log.getLogCurveInfo();
        DoubleSummaryStatistics summaryStats = curveInfos.stream()
                .map(CsLogCurveInfo::getMaxIndex)
                .mapToDouble(GenericMeasure::getValue)
                .summaryStatistics();

        return summaryStats.getMin();
    }

    private ZonedDateTime getMinOfMaxTime(ObjLog log){
        List<CsLogCurveInfo> curveInfos = log.getLogCurveInfo();

        ZonedDateTime maxDate = null;
        for (CsLogCurveInfo curveInfo : curveInfos) {

            ZonedDateTime currentMaxDate = curveInfo.getMaxDateTimeIndex().toGregorianCalendar().toZonedDateTime();
            if (maxDate == null)
                maxDate = currentMaxDate;
            else if (currentMaxDate.isBefore(maxDate))
                maxDate = currentMaxDate;
        }
        return maxDate;
    }

    private String getQuery(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
