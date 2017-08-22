package com.hashmapinc.tempus

import com.hashmapinc.tempus.WitsmlObjects.v1311.{ObjDtsInstalledSystem, ObjDtsMeasurement, ObjWellLog}
import com.hashmapinc.tempus.witsml.client.Client
import com.hashmapinc.tempus.witsml.api.WitsmlVersion
import com.hashmapinc.tempus.WitsmlObjects.v1411._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object WitsmlExample extends App{
  val witsmlClient = new Client("");                         /**SERVER URL***/
  witsmlClient.setUserName("");                              /**USER NAME***/
  witsmlClient.setPassword("");                              /**PASSWORD***/
  witsmlClient.setVersion(WitsmlVersion.VERSION_1311)        /**Witsml Version**/
  witsmlClient.connect();                                    /**Connect To Witsml Server**/

  Try(witsmlClient.getCapabilities) match {
    case Success(c) => println(s"Supported Caps : $c")
    case Failure(e) => println(s"Error executing get capabilities: ${e.getStackTrace}")
  }

  Try(witsmlClient.getWellsAsObj()) match {
    case Success(w) =>
      Option(w).foreach{ wells =>
        println("Found " + wells.getWell.size() + " wells")
        wells.getWell.asScala.foreach { well =>
          println("Well Id : " + well.getUid)
          println("Well Name : " + well.getName)
          printWellbores(witsmlClient, well.getUid)
        }
      }
    case Failure(e) => print(s"Error executing get wells as obj: ${e.getMessage}")
  }

  def printWellbores(witsmlClient : Client, wellId : String) {
    Try(witsmlClient.getWellboresForWellAsObj(wellId)) match {
      case Success(wellbores) => {
        println("Found " + wellbores.getWellbore.size() + " WellBores")
        wellbores.getWellbore.asScala.foreach {wellBore =>
          println("Wellbore Id : " + wellBore.getUid)
          println("Wellbore Name : " + wellBore.getName)

          /****COMMON FOR 1.3 & 1.4****/
          printLogs(witsmlClient,wellId, wellBore.getUid)
//          printMudLogs(witsmlClient, wellId, wellBore.getUid)
//          printTrajectorys(witsmlClient, wellId, wellBore.getUid)
//          printBhaRuns(witsmlClient, wellId, wellBore.getUid)
//          printCementJobs(witsmlClient, wellId, wellBore.getUid)
//          printConvCores(witsmlClient, wellId, wellBore.getUid)
//          printFluidsReports(witsmlClient, wellId, wellBore.getUid)
//          printFormationMarkers(witsmlClient, wellId, wellBore.getUid)
//          printMessages(witsmlClient, wellId, wellBore.getUid)
//          printOpsReports(witsmlClient, wellId, wellBore.getUid)
//          printRigs(witsmlClient, wellId, wellBore.getUid)
//          printRisks(witsmlClient, wellId, wellBore.getUid)
//          printSideWallCores(witsmlClient, wellId, wellBore.getUid)
//          printSurveyPrograms(witsmlClient, wellId, wellBore.getUid)
//          printTargets(witsmlClient, wellId, wellBore.getUid)
//          printTubulars(witsmlClient, wellId, wellBore.getUid)
//          printWbGeometrys(witsmlClient, wellId, wellBore.getUid)

          /******1.4.1.1******/
//          printAttachments(witsmlClient, wellId, wellBore.getUid)
//          printChangeLogs(witsmlClient, wellId, wellBore.getUid)
//          printDrillReports(witsmlClient, wellId, wellBore.getUid)
//          printObjectGroups(witsmlClient, wellId, wellBore.getUid)
//          printStimJobs(witsmlClient, wellId, wellBore.getUid)

          /******1.3.1.1******/
//          printDtsInstalledSystems(witsmlClient, wellId, wellBore.getUid)
//          printDtsMeasurements(witsmlClient, wellId, wellBore.getUid)
//          printRealtimes(witsmlClient, wellId, wellBore.getUid)
//          printWellLogs(witsmlClient, wellId, wellBore.getUid)

        }
      }
      case Failure(e) => println(s"Error in getting Wellnores : ${e.printStackTrace()}")
    }
  }

  def printLogs(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getLogMetadataAsObj(wellId, wellboreId)) match {
      case Success(logs) => {
        println("Found " + logs.getLog.size() + " Logs")
        logs.getLog.asScala.foreach {log =>
          println("Log Id : " + log.getUid)
          println("Log Name : " + log.getName)
        }
      }
      case Failure (e) => println(s"Error in getting Logs :  ${e.printStackTrace()}")
    }
  }

  def printMudLogs(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getMudLogsAsObj(wellId, wellboreId)) match {
      case Success(mudLogs) => {
        println("Found " + mudLogs.getMudLog.size() + " MudLogs")
        mudLogs.getMudLog.asScala.foreach {mudLog =>
          println("MudLog Id : " + mudLog.getUid)
          println("MudLog Name : " + mudLog.getName)
        }
      }
      case Failure (e) => println(s"Error in getting MudLogs :  ${e.printStackTrace()}")
    }
  }

  def printTrajectorys(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getTrajectorysAsObj(wellId, wellboreId)) match {
      case Success(trajectorys) => {
        println("Found " + trajectorys.getTrajectory.size() + " Trajectorys")
        trajectorys.getTrajectory.asScala.foreach {trajectory =>
          println("Trajectory Id : " + trajectory.getUid)
          println("Trajectory Name : " + trajectory.getName)
        }
      }
      case Failure (e) => println(s"Error in getting Trajectorys :  ${e.printStackTrace()}")
    }
  }

  def printBhaRuns(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getBhaRunsAsObj(wellId, wellboreId)) match {
      case Success(bhaRuns) => {
        println("Found " + bhaRuns.getBhaRun.size() + " BhaRuns")
        bhaRuns.getBhaRun.asScala.foreach {bhaRun =>
          println("BhaRun Id : " + bhaRun.getUid)
          println("BhaRun Name : " + bhaRun.getName)
        }
      }
      case Failure (e) => println(s"Error in getting BhaRuns :  ${e.printStackTrace()}")
    }
  }

  def printCementJobs(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getCementJobsAsObj(wellId, wellboreId)) match {
      case Success(cementJobs) => {
        println("Found " + cementJobs.getCementJob.size() + " CementJobs")
        cementJobs.getCementJob.asScala.foreach { cementJob =>
          println("CementJob Id : " + cementJob.getUid)
          println("CementJob Name : " + cementJob.getName)
        }
      }
      case Failure(e) => println(s"Error in getting CementJobs :  ${e.printStackTrace()}")
    }
  }

  def printConvCores(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getConvCoresAsObj(wellId, wellboreId)) match {
      case Success(convCores) => {
        println("Found " + convCores.getConvCore.size() + " ConvCores")
        convCores.getConvCore.asScala.foreach { convCore =>
          println("ConvCore Id : " + convCore.getUid)
          println("ConvCore Name : " + convCore.getName)
        }
      }
      case Failure(e) => println(s"Error in getting ConvCores :  ${e.printStackTrace()}")
    }
  }

  def printFluidsReports(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getFluidsReportsAsObj(wellId, wellboreId)) match {
      case Success(fluidsReports) => {
        println("Found " + fluidsReports.getFluidsReport.size() + " FluidsReports")
        fluidsReports.getFluidsReport.asScala.foreach { fluidsReport =>
          println("FluidsReport Id : " + fluidsReport.getUid)
          println("FluidsReport Name : " + fluidsReport.getName)
        }
      }
      case Failure(e) => println(s"Error in getting FluidsReports :  ${e.printStackTrace()}")
    }
  }

  def printFormationMarkers(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getFormationMarkersAsObj(wellId, wellboreId)) match {
      case Success(formationMarkers) => {
        println("Found " + formationMarkers.getFormationMarker.size() + " FormationMarkers")
        formationMarkers.getFormationMarker.asScala.foreach { formationMarker =>
          println("FormationMarker Id : " + formationMarker.getUid)
          println("FormationMarker Name : " + formationMarker.getName)
        }
      }
      case Failure(e) => println(s"Error in getting FormationMarkers :  ${e.printStackTrace()}")
    }
  }

  def printMessages(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getMessagesAsObj(wellId, wellboreId)) match {
      case Success(messages) => {
        println("Found " + messages.getMessage.size() + " Messages")
        messages.getMessage.asScala.foreach { message =>
          println("Message Id : " + message.getUid)
          println("Messages Name : " + message.getName)
        }
      }
      case Failure(e) => println(s"Error in getting Messages :  ${e.printStackTrace()}")
    }
  }

  def printOpsReports(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getOpsReportsAsObj(wellId, wellboreId)) match {
      case Success(opsReports) => {
        println("Found " + opsReports.getOpsReport.size() + " OpsReports")
        opsReports.getOpsReport.asScala.foreach { opsReport =>
          println("OpsReport Id : " + opsReport.getUid)
          println("OpsReport Name : " + opsReport.getName)
        }
      }
      case Failure(e) => println(s"Error in getting OpsReports :  ${e.printStackTrace()}")
    }
  }

  def printRigs(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getRigsAsObj(wellId, wellboreId)) match {
      case Success(rigs) => {
        println("Found " + rigs.getRig.size() + " Rigs")
        rigs.getRig.asScala.foreach { rig =>
          println("OpsReport Id : " + rig.getUid)
          println("OpsReport Name : " + rig.getName)
        }
      }
      case Failure(e) => println(s"Error in getting Rigs :  ${e.printStackTrace()}")
    }
  }

  def printRisks(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getRisksAsObj(wellId, wellboreId)) match {
      case Success(risks) => {
        println("Found " + risks.getRisk.size() + " Risks")
        risks.getRisk.asScala.foreach { risk =>
          println("Risk Id : " + risk.getUid)
          println("Risk Name : " + risk.getName)
        }
      }
      case Failure(e) => println(s"Error in getting Risks :  ${e.printStackTrace()}")
    }
  }

  def printSideWallCores(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getSideWallCoresAsObj(wellId, wellboreId)) match {
      case Success(sidewallCores) => {
        println("Found " + sidewallCores.getSidewallCore.size() + " SideWallCores")
        sidewallCores.getSidewallCore.asScala.foreach { sidewallCore =>
          println("SideWallCore Id : " + sidewallCore.getUid)
          println("SideWallCore Name : " + sidewallCore.getName)
        }
      }
      case Failure(e) => println(s"Error in getting SideWallCores :  ${e.printStackTrace()}")
    }
  }

  def printSurveyPrograms(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getSurveyProgramsAsObj(wellId, wellboreId)) match {
      case Success(surveyPrograms) => {
        println("Found " + surveyPrograms.getSurveyProgram.size() + " SurveyPrograms")
        surveyPrograms.getSurveyProgram.asScala.foreach { surveyProgram =>
          println("SideWallCore Id : " + surveyProgram.getUid)
          println("SideWallCore Name : " + surveyProgram.getName)
        }
      }
      case Failure(e) => println(s"Error in getting SurveyPrograms :  ${e.printStackTrace()}")
    }
  }

  def printTargets(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getTargetsAsObj(wellId, wellboreId)) match {
      case Success(targets) => {
        println("Found " + targets.getTarget.size() + " Targets")
        targets.getTarget.asScala.foreach { target =>
          println("Target Id : " + target.getUid)
          println("Target Name : " + target.getName)
        }
      }
      case Failure(e) => println(s"Error in getting Targets :  ${e.printStackTrace()}")
    }
  }

  def printTubulars(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getTubularsAsObj(wellId, wellboreId)) match {
      case Success(tubulars) => {
        println("Found " + tubulars.getTubular.size() + " Tubulars")
        tubulars.getTubular.asScala.foreach { tubular =>
          println("Tubular Id : " + tubular.getUid)
          println("Tubular Name : " + tubular.getName)
        }
      }
      case Failure(e) => println(s"Error in getting Tubulars :  ${e.printStackTrace()}")
    }
  }

  def printWbGeometrys(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getWbGeometrysAsObj(wellId, wellboreId)) match {
      case Success(wbGeometrys) => {
        println("Found " + wbGeometrys.getWbGeometry.size() + " WbGeometrys")
        wbGeometrys.getWbGeometry.asScala.foreach { wbGeometry =>
          println("WbGeometry Id : " + wbGeometry.getUid)
          println("WbGeometry Name : " + wbGeometry.getName)
        }
      }
      case Failure(e) => println(s"Error in getting WbGeometry :  ${e.printStackTrace()}")
    }
  }

  def printAttachments(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getAttachmentsAsObj(wellId, wellboreId)) match {
      case Success(attachments) => {
        println("Found " + attachments.getAttachment.size() + " Attachments")
        attachments.getAttachment.asScala.foreach { attachment: ObjAttachment =>
          println("Attachment Id : " + attachment.getUid)
          println("Attachment Name : " + attachment.getName)
        }
      }
      case Failure(e) => println(s"Error in getting Attachments :  ${e.printStackTrace()}")
    }
  }

  def printChangeLogs(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getChangeLogsAsObj(wellId, wellboreId)) match {
      case Success(changeLogs) => {
        println("Found " + changeLogs.getChangeLog.size() + " ChangeLogs")
        changeLogs.getChangeLog.asScala.foreach { changeLog: ObjChangeLog =>
          println("ChangeLog Id : " + changeLog.getUid)
          println("ChangeLog Name : " + changeLog.getNameObject)
        }
      }
      case Failure(e) => println(s"Error in getting ChangeLogs :  ${e.printStackTrace()}")
    }
  }

  def printDrillReports(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getDrillReportsAsObj(wellId, wellboreId)) match {
      case Success(drillReports) => {
        println("Found " + drillReports.getDrillReport.size() + " DrillReports")
        drillReports.getDrillReport.asScala.foreach { drillReport: ObjDrillReport =>
          println("DrillReports Id : " + drillReport.getUid)
          println("DrillReports Name : " + drillReport.getName)
        }
      }
      case Failure(e) => println(s"Error in getting DrillReports :  ${e.printStackTrace()}")
    }
  }

  def printObjectGroups(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getObjectGroupsAsObj(wellId, wellboreId)) match {
      case Success(objectGroups) => {
        println("Found " + objectGroups.getObjectGroup.size() + " ObjectGroups")
        objectGroups.getObjectGroup.asScala.foreach { objectGroup: ObjObjectGroup =>
          println("ObjectGroups Id : " + objectGroup.getUid)
          println("ObjectGroups Name : " + objectGroup.getName)
        }
      }
      case Failure(e) => println(s"Error in getting ObjectGroups :  ${e.printStackTrace()}")
    }
  }

  def printStimJobs(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getStimJobsAsObj(wellId, wellboreId)) match {
      case Success(stimJobs) => {
        println("Found " + stimJobs.getStimJob.size() + " StimJobs")
        stimJobs.getStimJob.asScala.foreach { stimJob: ObjStimJob =>
          println("StimJob Id : " + stimJob.getUid)
          println("StimJob Name : " + stimJob.getName)
        }
      }
      case Failure(e) => println(s"Error in getting StimJobs :  ${e.printStackTrace()}")
    }
  }

  def printDtsInstalledSystems(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getDtsInstalledSystemsAsObj(wellId, wellboreId)) match {
      case Success(dtsInstalledSystems) => {
        println("Found " + dtsInstalledSystems.getDtsInstalledSystem.size() + " DtsInstalledSystems")
        dtsInstalledSystems.getDtsInstalledSystem.asScala.foreach { dtsInstalledSystem: ObjDtsInstalledSystem =>
          println("DtsInstalledSystem Id : " + dtsInstalledSystem.getUid)
          println("DtsInstalledSystem Name : " + dtsInstalledSystem.getName)
        }
      }
      case Failure(e) => println(s"Error in getting DtsInstalledSystems :  ${e.printStackTrace()}")
    }
  }

  def printDtsMeasurements(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getDtsMeasurementsAsObj(wellId, wellboreId)) match {
      case Success(dtsMeasurements) => {
        println("Found " + dtsMeasurements.getDtsMeasurement.size() + " DtsMeasurements")
        dtsMeasurements.getDtsMeasurement.asScala.foreach { dtsMeasurement: ObjDtsMeasurement =>
          println("DtsMeasurement Id : " + dtsMeasurement.getUid)
          println("DtsMeasurement Name : " + dtsMeasurement.getName)
        }
      }
      case Failure(e) => println(s"Error in getting DtsMeasurements :  ${e.printStackTrace()}")
    }
  }

  def printWellLogs(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getWellLogsAsObj(wellId, wellboreId)) match {
      case Success(wellLogs) => {
        println("Found " + wellLogs.getWellLog.size() + " WellLogs")
        wellLogs.getWellLog.asScala.foreach { wellLog: ObjWellLog=>
          println("WellLog Id : " + wellLog.getUid)
          println("WellLog Name : " + wellLog.getName)
        }
      }
      case Failure(e) => println(s"Error in getting WellLogs :  ${e.printStackTrace()}")
    }
  }

  def printRealtimes(witsmlClient : Client, wellId : String, wellboreId : String) {
    Try(witsmlClient.getRealtimesAsObj(wellId, wellboreId)) match {
      case Success(realtimes) => {
        println("Found " + realtimes.getRealtime.size() + " Realtimes")
        realtimes.getRealtime.asScala.foreach { realtime =>
            println("Realtime Id : " + realtime.getIdSub)
        }
      }
      case Failure(e) => println(s"Error in getting Realtimes :  ${e.printStackTrace()}")
    }
  }
}