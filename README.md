<img src="https://github.com/hashmapinc/hashmap.github.io/blob/master/images/tempus/Tempus_Logo_Black_with_TagLine.png" width="950" height="245" alt="Hashmap, Inc Tempus"/>

[![License](http://img.shields.io/:license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Build Status](https://travis-ci.org/hashmapinc/witsml-client.svg?branch=master)](https://travis-ci.org/hashmapinc/witsml-client)

# Java WITSML 1.3.1.1 and 1.4.1.1 Client

The WITSML client utilizes the WITSML Objects Library SDK to allow a Java application to query a WITSML server. It has 
support for WITSML 1.3.1.1 and 1.4.1.1 Servers. This inital implementation allows for basic queries to be executed but
will feature a customizeable query builder and full featured growing object trackers for Logs, Trajectories, and MudLogs. 
The intention is that this will be used within another framework such as Apache NiFi or Apache Spark to communicate with
WITSML STORE API's. This data can then be processed by Spark, Storm, Flink or persisted in HBase or Hive. 

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [License](#license)

## Features

This library aims to provide a few key features:

* Utilizing the WITSML Object Library to generate POJO or string server responses
* Querying 1.3.1.1 or 1.4.1.1 WITSML STORE API's
* A Helper tracker library that keeps track of paginating large responses from open queries
* *FUTURE* - An interactive query builder to dynamically query for requested elements

## Requirements

* JDK 1.8 at a minimum
* Maven 3.1 or newer
* Git client (to build locally)

## Getting Started
To build the library and get started first off clone the GitHub repository 

    git clone https://github.com/hashmapinc/witsml-client.git

Change directory into the WitsmlObjectsLibrary

    cd witsml0client
    
Execute a maven clean install

    mvn clean install
    
A Build success message should appear
    
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 13.271 s
    [INFO] Finished at: 2017-06-30T15:14:58-05:00
    [INFO] Final Memory: 20M/377M
    [INFO] ------------------------------------------------------------------------
    
## Usage

#### Initalization
```java
    Client c = new Client("https://testwitsmlserver.com");
        c.setUserName("username");
        c.setPassword("secret");
        c.setVersion(WitsmlVersion.VERSION_1411);
        c.connect();
        try {
            System.out.println("Supported Caps:");
            System.out.println(prettyPrint(c.getCapabilities(), true));
        }
``` 

#### Well Requests
```java
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
```
 
#### Wellbore Requets
```java
    private static void PrintWellbores(Client c, String wellId){
        try {
            ObjWellbores wellbores = c.getWellboresForWellAsObj(wellId);
            wellbores.getWellbore().forEach(wellbore -> {
                System.out.println("Wellbore Name: " + wellbore.getName());
                System.out.println("Wellbore Id: " + wellbore.getUid());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

#### Log Requests
```java
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
```

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
