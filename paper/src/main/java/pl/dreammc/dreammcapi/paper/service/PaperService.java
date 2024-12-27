package pl.dreammc.dreammcapi.paper.service;

import org.bukkit.configuration.file.FileConfiguration;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.shared.service.BaseService;

import java.util.NoSuchElementException;

public class PaperService extends BaseService {

    private final FileConfiguration config;

    public PaperService(FileConfiguration config) {
        super();
        this.config = config;
        try {
            if(config == null) throw new NoSuchElementException();
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
        String result = this.config.getString("service.group");
        if(result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public String readServiceName() throws NoSuchElementException  {
        String result = this.config.getString("service.name");
        if(result == null) throw new NoSuchElementException();
        return result;
    }

    @Override
    public String readServiceId() throws NoSuchElementException  {
        String result = this.config.getString("service.id");
        if(result == null) throw new NoSuchElementException();
        return result;
    }
}
