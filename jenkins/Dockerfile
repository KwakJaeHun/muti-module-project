# jenkins docker file
FROM jenkins/jenkins:jdk17

USER root
RUN apt-get update &&\
    apt-get upgrade -y &&\
    apt-get install -y openssh-client