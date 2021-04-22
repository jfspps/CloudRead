[![CircleCI](https://circleci.com/gh/jfspps/CloudRead.svg?style=svg)](https://circleci.com/gh/jfspps/CloudRead)


# CloudRead

CloudRead is a REST client for the article authoring tool, [CloudWrite](https://github.com/jfspps/CloudWrite). It provides read-only, frontend access to the CloudWrite database, and primarily consumes XML, saving properties to an XML file. Handling JSON is also provided though all subsequent Java object instantiation is based on the XML file.

CloudRead can retrieve [saved queries](./xmlFeeds) by ID for the user to view without re-querying CloudWrite. Each article can be exported as a DOCX file. With each export, the DOCX file is saved [here](./DOCX).

## Running CloudRead (client) with CloudWrite (service)

Download copies of the JAR directories and subdirectories present for both [CloudRead](https://github.com/jfspps/CloudRead/tree/main/JAR) and [CloudWrite](https://github.com/jfspps/CloudWrite/tree/main/JAR). Run both JARs in separate terminals as outlined in their respective READMEs. Then access localhost port 5000 for CloudWrite or localhost port 8080 for CloudRead.

To build a CloudRead Docker image, follow the guidelines [here](https://github.com/jfspps/CloudRead/tree/main/docker/README.md). This also contains instructions about running both CloudWrite (to build a CloudWrite image, go [here](https://github.com/jfspps/CloudWrite/tree/main/docker/README.md)) and CloudRead containers in their own subnet with Docker Compose.

## XML schema to Java classes

As will become apparent on inspection, CloudRead uses the same XML schema [XSD](https://github.com/jfspps/CloudRead/tree/main/src/main/resources/xsd) as CloudWrite and generates DTO classes during packaging. To extend CloudRead, one is advised to first build the project so that the IDE can recognise the generated classes (typically located in /target) before continuing.
