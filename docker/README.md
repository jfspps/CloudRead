# Instructions

With Dockerfile and the JAR (typically found in /target) in the same directory, build the CentOS based image with

```bash
docker build -t cloud-read-docker .
```

Don't miss out the period above! Then run the container (--name is optional) from the console (add -d for daemon) with

```bash
docker run -p 8080:8080 --name reader cloud-read-docker
```

The container port (from you the host) is the first port number. Change x to the port number you want to use when accessing the Docker image:

```bash
docker run -p x:8080 cloud-read-docker
```

## Command line arguments

For reference, to edit the REST API url and paths use the following arguments:

```bash
java -jar cloudread-0.0.1-SNAPSHOT.jar --rest.config.url=http://RESTurl
```

```bash
java -jar cloudread-0.0.1-SNAPSHOT.jar --rest.config.fundamentals_path=/oops1/
```

```bash
java -jar cloudread-0.0.1-SNAPSHOT.jar --rest.config.research_path=/oops2/
```

## Running both CloudRead and CloudWrite with Docker Compose

Once both images are built (and assuming you have applied the same container names, cloud-read-docker and cloud-write-docker), run both containers with Docker Compose by entering (in the same directory as the docker-compose.yml):

```bash
docker-compose up
```

To execute this as a daemon, add the -d parameter (close the daemon, enter ```docker-compose down```). This should then have both web service and client running as networked containers. Go to ```http://localhost:8080``` for CloudRead and ```http://localhost:5000``` for CloudWrite.
