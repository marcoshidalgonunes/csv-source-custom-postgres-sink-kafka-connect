# Use the official Confluent Kafka Connect image
FROM confluentinc/cp-kafka-connect

# Install SpoolDir Connector
RUN mkdir -p /home/appuser/data/input &&\
    mkdir /home/appuser/data/finished &&\
    mkdir /home/appuser/data/error
COPY libs/*.jar /usr/share/java/kafka

# Copy files to be processed
COPY data/*.csv /home/appuser/data/input
USER root
RUN chown appuser:appuser /home/appuser/data/input/*.csv
