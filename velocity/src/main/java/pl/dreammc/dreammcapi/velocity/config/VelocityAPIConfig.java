package pl.dreammc.dreammcapi.velocity.config;

import lombok.Getter;

import java.nio.file.Path;

public class VelocityAPIConfig extends PluginConfiguration<VelocityAPIConfig> {

    @Getter private String serviceGroup;
    @Getter private String serviceName;
    @Getter private String serviceId;

    public VelocityAPIConfig() {}

    public VelocityAPIConfig(String serviceGroup, String serviceName, String serviceId) {
        this.serviceGroup = serviceGroup;
        this.serviceName = serviceName;
        this.serviceId = serviceId;
    }

    @Override
    public VelocityAPIConfig getDefault() {
        this.serviceGroup = "dreammc";
        this.serviceName = "unset";
        this.serviceId = "unset";
        return this;
    }
}
