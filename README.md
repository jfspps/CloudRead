# CloudRead

CloudRead is a REST client for the article authoring tool, [CloudWrite](https://github.com/jfspps/CloudWrite). It provides read-only, frontend access to the CloudWrite database, and primarily saves REST queries to an XML file. Saving to JSON is also provided though all subsequent Java object instantiation is based on the XML file.

CloudRead can retrieve [saved queries](./xmlFeeds) by ID for the user to view without re-querying CloudWrite. Each article can be exported as a DOCX file. With each export, the DOCX file is saved [here](./DOCX).

## Running CloudRead (client) with CloudWrite (service)

(Docker images are currently under development)

Download copies of the JAR directories and subdirectories present for both [CloudRead](https://github.com/jfspps/CloudRead/tree/main/JAR) and [CloudWrite](https://github.com/jfspps/CloudWrite/tree/main/JAR). Run both JARs in separate terminals as outlined in their respective READMEs. Then access localhost port 5000 for CloudWrite or localhost port 8080 for CloudRead.
