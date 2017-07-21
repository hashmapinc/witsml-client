package com.hashmapinc.tempus.witsml.example;

import com.hashmapinc.tempus.witsml.client.Client;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWells;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjLogs;
import com.hashmapinc.tempus.WitsmlObjects.v1411.ObjWellbores;
import com.hashmapinc.tempus.witsml.api.WitsmlVersion;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.rmi.RemoteException;

public class MyWitsmlClient {

    public static void main(String args[]){
        Client c = new Client(args[0]);
        c.setUserName(args[1]);
        c.setPassword(args[2]);
        c.setVersion(WitsmlVersion.VERSION_1411);
        c.connect();
        try {
            System.out.println("Supported Caps:");
            System.out.println(prettyPrint(c.getCapabilities(), true));
        }
        catch (SAXException | ParserConfigurationException | IOException e) {
            System.out.println("Error executing get capabilities: " + e.getMessage());
        }
        ObjWells wells = null;
        try {
            wells = c.getWellsAsObj();
        } catch (FileNotFoundException | RemoteException | JAXBException e) {
            System.out.println("Error executing get wells as obj: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wells != null) {
            System.out.println("Found " + wells.getWell().size() + " wells.");
            wells.getWell().forEach(well -> {
                System.out.println("Well Name: " + well.getName());
                System.out.println("Well Legal: " + well.getNameLegal());
                PrintWellbores(c, well.getUid());
            });
        }
    }

    private static void PrintWellbores(Client c, String wellId){
        try {
            ObjWellbores wellbores = c.getWellboresForWellAsObj(wellId);
            wellbores.getWellbore().forEach(wellbore -> {
                System.out.println("Wellbore Name: " + wellbore.getName());
                System.out.println("Wellbore Id: " + wellbore.getUid());
                PrintLogs(c, wellbore.getUidWell(), wellbore.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintLogs(Client c, String wellId, String wellboreId){
        try {
            ObjLogs logs = c.getLogMetadataAsObj(wellId, wellboreId);
            logs.getLog().forEach(log -> {
                System.out.println("Log Name: " + log.getName());
                System.out.println("Log Id: " + log.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String prettyPrint(String xml, Boolean omitXmlDeclaration) throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xml)));

        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        format.setIndent(2);
        format.setOmitXMLDeclaration(omitXmlDeclaration);
        format.setLineWidth(Integer.MAX_VALUE);
        Writer outxml = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(outxml, format);
        serializer.serialize(doc);

        return outxml.toString();

    }
}
