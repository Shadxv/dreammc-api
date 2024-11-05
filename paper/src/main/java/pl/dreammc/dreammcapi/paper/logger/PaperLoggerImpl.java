package pl.dreammc.dreammcapi.paper.logger;

import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.shared.logger.ILogger;

import java.util.logging.Logger;
import java.util.logging.Level;


public class PaperLoggerImpl implements ILogger {

    Logger logger = PaperDreamMCAPI.getInstance().getLogger();

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
