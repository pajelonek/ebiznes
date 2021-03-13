FROM ubuntu:18.04

ENV TZ=Europe/Warsaw

RUN apt-get update && apt-get upgrade -y

# VIM
RUN apt-get install -y vim git wget unzip

# CURL
RUN apt install curl -y

# JAVA
RUN apt install openjdk-8-jdk openjdk-8-jre -y

# SCALA
RUN wget https://downloads.lightbend.com/scala/2.12.3/scala-2.12.3.deb && \
    dpkg -i scala-2.12.3.deb

# NPM
RUN apt install nodejs -y && \
    apt install npm -y

# SBT
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install -y sbt

RUN useradd -ms /bin/bash pjelonek && \
    adduser pjelonek sudo

# Expose ports for React(3000) and Play(9000)
EXPOSE 3000 9000

USER pjelonek
WORKDIR /home/pjelonek
RUN mkdir /home/pjelonek/projekt

VOLUME /home/pjelonek/projekt