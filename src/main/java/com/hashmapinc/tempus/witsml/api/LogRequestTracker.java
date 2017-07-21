package com.hashmapinc.tempus.witsml.api;

import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlMarshal;
import com.hashmapinc.tempus.WitsmlObjects.Util.WitsmlVersionTransformer;
import com.hashmapinc.tempus.WitsmlObjects.v1411.*;
import com.hashmapinc.tempus.witsml.client.Client;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class LogRequestTracker extends AbstractRequestTracker{

    private WitsmlClient witsmlClient;
    private ZonedDateTime lastQueryTime = null;
    private ZonedDateTime lastLogTime = null;
    private LogIndexType indexType = null;
    private double lastLogDepth = -1;
    private WitsmlVersionTransformer transformer;

    @Override
    public void initalize(WitsmlClient witsmlClient) {
        try {
            transformer = new WitsmlVersionTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        this.witsmlClient = witsmlClient;
    }

    @Override

    public ObjLogs ExecuteRequest()  {
        ObjLogs logs = null;

        try {
            String response = executeQuery();

            if (getVersion() == WitsmlVersion.VERSION_1311) {
                try {
                    response = transformer.convertVersion(response);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            }

            logs = WitsmlMarshal.deserialize(response, ObjLogs.class);

            if (indexType == null)
                indexType = logs.getLog().get(0).getIndexType();

            switch (indexType){
                case MEASURED_DEPTH:
                case VERTICAL_DEPTH:{
                    lastLogDepth = getMinOfMaxDepth(logs.getLog().get(0));
                    break;
                }
                case DATE_TIME:{
                    lastLogTime = getMinOfMaxTime(logs.getLog().get(0));
                }
            }
        } catch (JAXBException | RemoteException e) {
            e.printStackTrace();
        }

        return logs;
    }

    private String executeQuery() throws RemoteException {
        lastQueryTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        return witsmlClient.executeLogQuery(getQuery(), getOptionsIn(), getCapabilitiesIn());
    }

    public String getQueryStartDepth(){
        if (lastLogDepth == -1)
            return "";
        else if (lastLogDepth > 1)
            return String.valueOf(lastLogDepth);
        return "";
    }

    private String getQueryStartTime(){
        if (lastLogTime == null)
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
}
