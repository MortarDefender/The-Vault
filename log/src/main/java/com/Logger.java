package com;

import java.util.List;

public interface Logger {
    void testActivity(String logInformation);
    void infoActivity(String logInformation);
    void debugActivity(String logInformation);
    void errorActivity(String logInformation);
    void warningActivity(String logInformation);
    void criticalActivity(String logInformation);

    List<String> getLogs(LogLevel startLevel, LogLevel endLevel);
}
