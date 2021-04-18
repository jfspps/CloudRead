# Instructions

With Dockerfile and the JAR (typically found in /target) in the same directory, build the CentOS based image with

```bash
docker build -t cloud-read-docker .
```

Don't miss out the period above! Then run from the console (add -d for daemon) with

```bash
docker run -p 8080:8080 cloud-read-docker
```

The container port (from you the host) is the first port number. Change x to the port number you want to use when accessing the Docker image:

```bash
docker run -p x:8080 cloud-read-docker
```

The app has already be set to run from PORT 8080 within the container. If run as a daemon, one can verify that the container is running by calling

```bash
docker ps
```
