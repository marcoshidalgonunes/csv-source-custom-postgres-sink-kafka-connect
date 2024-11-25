# Use the official Confluent Kafka Connect image
FROM confluentinc/cp-kafka-connect

ENV CONNECT_PLUGIN_PATH=/usr/share/java \
    CONNECT_PLUGIN_KAFKA=/usr/share/java/kafka

# Setup SpoolDir Connector
RUN mkdir -p /home/appuser/data/input &&\
    mkdir /home/appuser/data/finished &&\
    mkdir /home/appuser/data/error
COPY libs/*.jar /usr/share/java/kafka

USER root

# Copy files to be processed
COPY data/*.csv /home/appuser/data/input
RUN chown appuser:appuser /home/appuser/data/input/*.csv

# Deploy PostgreSQL JDBC Driver
RUN cd $CONNECT_PLUGIN_KAFKA && \
    curl -sO https://jdbc.postgresql.org/download/postgresql-42.7.4.jar
