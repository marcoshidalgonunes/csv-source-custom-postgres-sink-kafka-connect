package com.example.KafkaConnector;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.kafka.common.config.ConfigDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostgresSinkConnector extends SinkConnector {
    private Map<String, String> configProps;

    @Override
    public void start(Map<String, String> props) {
        this.configProps = props;
    }

    @Override
    public void stop() {
        // Cleanup logic
    }

    @Override
    public Class<? extends Task> taskClass() {
        return PostgresSinkTask.class;
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef()
                .define("connection.url", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "PostgreSQL connection URL")
                .define("connection.user", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "PostgreSQL user")
                .define("connection.password", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "PostgreSQL password")
                .define("table.name", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Table name");
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();
        for (int i = 0; i < maxTasks; i++) {
            configs.add(configProps);
        }
        return configs;
    }
}
