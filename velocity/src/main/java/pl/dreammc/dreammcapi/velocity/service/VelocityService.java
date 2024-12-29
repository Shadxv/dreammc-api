package pl.dreammc.dreammcapi.velocity.service;

import com.velocitypowered.api.proxy.config.ProxyConfig;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.shared.service.BaseService;
import pl.dreammc.dreammcapi.velocity.config.VelocityAPIConfig;

import java.util.NoSuchElementException;

public class VelocityService extends BaseService {

//    private final VelocityAPIConfig config;
//
//    public VelocityService(VelocityAPIConfig config) {
//        this.config = config;
//    }

    public VelocityService() {
        try {
            this.serviceGroup = this.readServiceGroup();
            this.serviceName = this.readServiceName();
            this.serviceId = this.readServiceId();
            Registry.service = this;
        } catch (NoSuchElementException e) {
            if(Registry.logger == null) {
                return;
            }
            Registry.logger.sendError("Config file not found or service fields are not set");
        }
    }

    @Override
    protected String readServiceGroup() throws NoSuchElementException {
        return "dreammc";
    }

    @Override
    protected String readServiceName() throws NoSuchElementException {
        return "proxy";
    }

    @Override
    protected String readServiceId() throws NoSuchElementException {
        return "0";
    }
}
