package com.example.KafkaConnector;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class PostgresSinkTask extends SinkTask {
    private Connection connection;
    private String tableName;

    @Override
    public void start(Map<String, String> props) {
        String url = props.get("connection.url");
        String user = props.get("connection.user");
        String password = props.get("connection.password");
        tableName = props.get("table.name");

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        for (SinkRecord record : records) {
            Struct value = (Struct) record.value();
            String performer = value.getString("Performer");
            String album = value.getString("Album");
            String track = value.getString("Track");

            String sql = "INSERT INTO " + tableName + " (performer, album, track) VALUES (?, ?, ?) " +
                         "ON CONFLICT (album, track) DO NOTHING";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, performer);
                stmt.setString(2, album);
                stmt.setString(3, track);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to insert record into PostgreSQL", e);
            }
        }
    }

    @Override
    public void stop() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close PostgreSQL connection", e);
        }
    }

    @Override
    public String version() {
        return "1.0";
    }
}

