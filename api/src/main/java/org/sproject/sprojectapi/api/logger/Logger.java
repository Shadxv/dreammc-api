package org.sproject.sprojectapi.api.logger;

import org.sproject.sprojectapi.shared.Registry;


public class Logger {

    public static void sendDebug(String message){
        if(Registry.logger == null) throw new RuntimeException("Logger not initialized");
        Registry.logger.sendDebug(message);
    }
    public static void sendInfo(String message){
        if(Registry.logger == null) throw new RuntimeException("Logger not initialized");
        Registry.logger.sendInfo(message);
    }
    public static void sendWarning(String message){
        if(Registry.logger == null) throw new RuntimeException("Logger not initialized");
        Registry.logger.sendWarning(message);
    }
    public static void sendError(String message){
        if(Registry.logger == null) throw new RuntimeException("Logger not initialized");
        Registry.logger.sendError(message);
    }

}
