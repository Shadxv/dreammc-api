package org.sproject.sprojectapi.velocity.logger;

import org.slf4j.Logger;
import org.sproject.sprojectapi.shared.logger.ILogger;
import org.sproject.sprojectapi.velocity.VelocitySProjectAPI;

public class VelocityLoggerImpl implements ILogger {

    Logger logger = VelocitySProjectAPI.getInstance().getLogger();

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
        logger.warn(message);
    }

}
