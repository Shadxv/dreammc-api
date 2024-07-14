package org.sproject.sprojectapi.paper.logger;

import org.sproject.sprojectapi.paper.PaperSProjectAPI;
import org.sproject.sprojectapi.shared.logger.ILogger;

import java.util.logging.Logger;
import java.util.logging.Level;


public class PaperLoggerImpl implements ILogger {

    Logger logger = PaperSProjectAPI.getInstance().getLogger();

    @Override
    public void sendDebug(String message) {
        logger.log(Level.FINE, "[DEBUG] " + message);
    }

    @Override
    public void sendInfo(String message) {
        logger.log(Level.INFO, message);
    }

    @Override
    public void sendWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    @Override
    public void sendError(String message) {
        logger.log(Level.SEVERE, message);
    }

}
