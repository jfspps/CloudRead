# CloudRead

CloudRead is a REST client for the article authoring tool, [CloudWrite](https://github.com/jfspps/CloudWrite). It provides read-only, frontend access to the CloudWrite database, and primarily saves REST queries to an XML file. Saving to JSON is also provided though all subsequent Java object instantiation is based on the XML file.

CloudRead can retrieve saved queries by ID for the user to view without re-querying CloudWrite. Each article can be exported as a DOCX file. With each export, the DOCX file is saved [here](/src/main/resources/DOCX/).

## Docker file

A docker image can be built by following the instructions provided [here](./docker/README.md) using the [Dockerfile](./docker/Dockerfile) provided.