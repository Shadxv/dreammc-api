package pl.dreammc.dreammcapi.velocity.service;

import com.velocitypowered.api.proxy.config.ProxyConfig;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.shared.service.BaseService;
import pl.dreammc.dreammcapi.velocity.config.VelocityAPIConfig;

import java.util.NoSuchElementException;

public class VelocityService extends BaseService {

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
    public String readServiceGroup() throws NoSuchElementException {
        if(!System.getenv().containsKey("GROUP_NAME")) throw new NoSuchElementException();
        return System.getenv("GROUP_NAME");
    }

    @Override
    public String readServiceName() throws NoSuchElementException  {
        if(!System.getenv().containsKey("SERVICE_NAME")) throw new NoSuchElementException();
        return System.getenv("SERVICE_NAME");
    }

    @Override
    public String readServiceId() throws NoSuchElementException  {
        if(!System.getenv().containsKey("SERVER_ID")) throw new NoSuchElementException();
        return System.getenv("SERVER_ID");
    }
}
