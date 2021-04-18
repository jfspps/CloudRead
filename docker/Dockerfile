FROM centos 
 RUN yum install -y java-11-openjdk-devel 
 VOLUME /tmp
 ADD /cloudread-0.0.1-SNAPSHOT.jar cloudRead.jar
 RUN sh -c 'touch /cloudRead.jar'
 ENTRYPOINT ["java","-jar","/cloudRead.jar"]
