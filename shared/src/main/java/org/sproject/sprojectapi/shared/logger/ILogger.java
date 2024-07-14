package org.sproject.sprojectapi.shared.logger;

public interface ILogger {

    void sendDebug(String message);
    void sendInfo(String message);
    void sendWarning(String message);
    void sendError(String message);


}
