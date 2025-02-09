package pl.dreammc.dreammcapi.paper.service;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.shared.service.BaseService;

import java.util.NoSuchElementException;

public class PaperService extends BaseService {

    @Getter @Setter @Nullable private String addressIP;
    @Getter @Setter @Nullable private Integer port;
    @Getter @Nullable private String proxyServiceName;

    public PaperService(FileConfiguration config) {
        super();
        try {
            this.serviceGroup = this.readServiceGroup();
            this.serviceName = this.readServiceName();
            this.serviceId = this.readServiceId();
            this.proxyServiceName = config.getString("proxy-service");
            Registry.service = this;
        } catch (NoSuchElementException e) {
            if(Registry.logger == null) {
                return;
            }Registry.logger.sendError("Service fields are not set");
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
