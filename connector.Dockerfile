# Use the official Confluent Kafka Connect image
FROM confluentinc/cp-kafka-connect

ENV CONNECT_PLUGIN_PATH=/usr/share/java \
    CONNECT_PLUGIN_KAFKA=/usr/share/java/kafka

# Setup SpoolDir Connector
RUN mkdir -p /tmp/data/input &&\
    mkdir /tmp/data/finished &&\
    mkdir /tmp/data/error
COPY libs/*.jar /usr/share/java/kafka

# Deploy PostgreSQL JDBC Driver
USER root
RUN cd $CONNECT_PLUGIN_KAFKA && \
    curl -sO https://jdbc.postgresql.org/download/postgresql-42.7.4.jar
