package com.FileLogger;

import java.io.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.time.Instant;
import java.util.ArrayList;

import com.Logger;
import com.LogLevel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class FileLogger implements Logger {
    private static class LogFormat {
        public LogLevel logLevel;
        public String logInformation;

        public LogFormat(LogLevel level, String info) {
            logLevel = level;
            logInformation = info;
        }

        @Override
        public String toString() { return logLevel + ": " + logInformation; }
    }

    private final Gson convertor = new Gson();
    private final String  LogFilePath = "Log/src/com/FileLogger/log.json";

    @Override
    public void testActivity(String logInformation) {
        this.logActivity(logInformation, LogLevel.TEST);
    }

    @Override
    public void debugActivity(String logInformation) {
        this.logActivity(logInformation, LogLevel.DEBUG);
    }

    @Override
    public void infoActivity(String logInformation) {
        this.logActivity(logInformation, LogLevel.INFO);
    }

    @Override
    public void warningActivity(String logInformation) {
        this.logActivity(logInformation, LogLevel.WARNING);
    }

    @Override
    public void errorActivity(String logInformation) {
        this.logActivity(logInformation, LogLevel.ERROR);
    }

    @Override
    public void criticalActivity(String logInformation) {
        this.logActivity(logInformation, LogLevel.CRITICAL);
    }

    @Override
    public List<String> getLogs(LogLevel startLevel, LogLevel endLevel) {
        String fileLogs = readLogFileData();
        List<String> logs = new ArrayList<>();
        Type logDBType = new TypeToken<Map<Instant, LogFormat>>() {}.getType();
        Map<Instant, LogFormat> logDB = convertor.fromJson(fileLogs, logDBType);

        logDB.values().stream()
                .filter(line -> (line.logLevel.compareTo(startLevel) >= 0)  && ( line.logLevel.compareTo(endLevel) < 0))
                .forEach(line -> logs.add(line.toString()));

        return logs;
    }

    private void logActivity(String logInformation, LogLevel level) {
        String fileLogs = readLogFileData();
        Map<String, LogFormat> logDB;

        Type logDBType = new TypeToken<Map<String, LogFormat>>() {}.getType();
        if (fileLogs.equals(""))
            logDB = new HashMap<>();
        else
            logDB = convertor.fromJson(fileLogs, logDBType);
        logDB.put(Instant.now().toString(), new LogFormat(level, logInformation));
        writeDataToLogFile(convertor.toJson(logDB));
    }

    private String readLogFileData() {
        String fileData = "";

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(LogFilePath))) {
            fileData = (String) in.readObject();
        } catch (IOException | ClassNotFoundException ignore) {}

        return fileData;
    }

    private void writeDataToLogFile(String dataToWrite) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(LogFilePath))) {
            out.writeObject(dataToWrite);
            out.flush();
        } catch (IOException ignore) {}
    }
}
