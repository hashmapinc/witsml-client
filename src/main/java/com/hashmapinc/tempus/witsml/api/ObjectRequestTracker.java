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
import java.util.stream.Collectors;

public class ObjectRequestTracker extends AbstractRequestTracker {

    private String wellId;
    private String wellboreId;
    private WitsmlVersionTransformer transformer;
    private WitsmlClient witsmlClient;
    private String objectId;
    private String objectType;

    public void setObjectType(String objectType) {this.objectType = objectType;}
    public String getObjectType() {return this.objectType;}
    public void setObjectId(String objectId) {this.objectId = objectId;}
    public String getObjectId() {return this.objectId;}

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
    public Object ExecuteRequest() {
        String response;
        try {
            response = executeQuery();

            if (response == null) {
                return null;
            }

            if (getVersion() == WitsmlVersion.VERSION_1311) {
                try {
                    response = transformer.convertVersion(response);
                } catch (TransformerException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            switch (getObjectType().toUpperCase()) {
                case "BHARUN" :
                    ObjBhaRuns bhaRuns = WitsmlMarshal.deserialize(response, ObjBhaRuns.class);
                    return bhaRuns.getBhaRun().get(0);
                case "CEMENTJOB" :
                    ObjCementJobs cementJobs = WitsmlMarshal.deserialize(response, ObjCementJobs.class);
                    return cementJobs.getCementJob().get(0);
                case "CONVCORE" :
                    ObjConvCores convCores = WitsmlMarshal.deserialize(response, ObjConvCores.class);
                    return convCores.getConvCore().get(0);
                case "FLUIDREPORT" :
                    ObjFluidsReports fluidsReports = WitsmlMarshal.deserialize(response, ObjFluidsReports.class);
                    return fluidsReports.getFluidsReport().get(0);
                case "FORMATIONMARKER" :
                    ObjFormationMarkers formationMarkers = WitsmlMarshal.deserialize(response, ObjFormationMarkers.class);
                    return formationMarkers.getFormationMarker().get(0);
                case "MESSAGE" :
                    ObjMessages messages = WitsmlMarshal.deserialize(response, ObjMessages.class);
                    return messages.getMessage().get(0);
                case "OPSREPORT" :
                    ObjOpsReports opsReports = WitsmlMarshal.deserialize(response, ObjOpsReports.class);
                    return opsReports.getOpsReport().get(0);
                case "RIG" :
                    ObjRigs rigs = WitsmlMarshal.deserialize(response, ObjRigs.class);
                    return rigs.getRig().get(0);
                case "RISK" :
                    ObjRisks risks = WitsmlMarshal.deserialize(response, ObjRisks.class);
                    return risks.getRisk().get(0);
                case "SIDEWALLCORE" :
                    ObjSidewallCores sidewallCores = WitsmlMarshal.deserialize(response, ObjSidewallCores.class);
                    return sidewallCores.getSidewallCore().get(0);
                case "SURVEYPROGRAM" :
                    ObjSurveyPrograms surveyPrograms = WitsmlMarshal.deserialize(response, ObjSurveyPrograms.class);
                    return surveyPrograms.getSurveyProgram().get(0);
                case "TARGET" :
                    ObjTargets targets = WitsmlMarshal.deserialize(response, ObjTargets.class);
                    return targets.getTarget().get(0);
                case "TUBULAR" :
                    ObjTubulars tubulars = WitsmlMarshal.deserialize(response, ObjTubulars.class);
                    return tubulars.getTubular().get(0);
                case "WBGEOMETRY" :
                    ObjWbGeometrys wbGeometrys = WitsmlMarshal.deserialize(response, ObjWbGeometrys.class);
                    return wbGeometrys.getWbGeometry().get(0);
                default:
                    return null;
            }
        } catch (JAXBException | RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String executeQuery() throws RemoteException {
        String query = "";
        setOptionsIn("");
        switch (getObjectType().toUpperCase()) {
            case "BHARUN" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetBhaRunData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetBhaRunData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "CEMENTJOB" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetCementJobData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetCementJobData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "CONVCORE" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetConvCoreData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetConvCoreData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "FLUIDREPORT" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetFluidsReportData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetFluidsReportData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "FORMATIONMARKER" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetFormationMarkerData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetFormationMarkerData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "MESSAGE" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetMessageData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetMessageData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "OPSREPORT" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetOpsReportData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetOpsReportData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "RIG" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetRigData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetRigData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "RISK" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetRiskData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetRiskData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "SIDEWALLCORE" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetSideWallCoreData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetSideWallCoreData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "SURVEYPROGRAM" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetSurveyProgramData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetSurveyProgramData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "TARGET" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetTargetData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetTargetData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "TUBULAR" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetTubularData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetTubularData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            case "WBGEOMETRY" :
                try {
                    if (getVersion().toString().equals("1.3.1.1")) {
                        query = getQuery("/1311/GetWbGeometryData.xml");
                    } else if (getVersion().toString().equals("1.4.1.1")) {
                        query = getQuery("/1411/GetWbGeometryData.xml");
                        setOptionsIn("dataVersion=1.4.1.1");
                    }
                } catch (Exception ex) {
                    System.out.println("Error in getQuery ex : " + ex);
                }
                break;
            default:
                return null;
        }
        query = query.replace("%uidWell%", wellId);
        query = query.replace("%uidWellbore%", wellboreId);
        query = query.replace("%uid%", objectId);
        setQuery(query);
        setCapabilitesIn("");
        return witsmlClient.executeObjectQuery(getObjectType(), getQuery(), getOptionsIn(), getCapabilitiesIn());
    }

    private String getQuery(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        return reader.lines().collect(Collectors.joining(
                System.getProperty("line.separator")));
    }
}
