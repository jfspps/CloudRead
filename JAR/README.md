## Packaged CloudRead

Create the directories /DOCX and /xmlFeeds if they do not already exist in the same directory as the JAR file.

Run the jar from the command line:

```bash
java -jar -Djava.security.egd=file:/dev/./urandom cloudread-0.0.1-SNAPSHOT.jar
```

From a browser, go to [localhost:8080](http://localhost:8080)

To edit the REST API url and paths, use the following arguments:

```bash
java -jar cloudread-0.0.1-SNAPSHOT.jar --rest.config.url=http://RESTurl
```

```bash
java -jar cloudread-0.0.1-SNAPSHOT.jar --rest.config.fundamentals_path=/oops1/
```

```bash
java -jar cloudread-0.0.1-SNAPSHOT.jar --rest.config.research_path=/oops2/
```

This then assumes that the path to retrieve e.g. research articles is `http://RESTurl/oops2/`.