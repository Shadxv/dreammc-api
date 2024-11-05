package pl.dreammc.dreammcapi.velocity.logger;

import org.slf4j.Logger;
import pl.dreammc.dreammcapi.shared.logger.ILogger;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;

public class VelocityLoggerImpl implements ILogger {

    Logger logger = VelocityDreamMCAPI.getInstance().getLogger();

    @Override
    public void sendDebug(String message) {
        logger.debug(message);
    }

    @Override
    public void sendInfo(String message) {
        logger.info(message);
    }

    @Override
    public void sendWarning(String message) {
        logger.warn(message);
    }

    @Override
    public void sendError(String message) {
        logger.error(message);
    }

}
