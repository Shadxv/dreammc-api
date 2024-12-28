package pl.dreammc.dreammcapi.shared.service;

import lombok.Getter;

import java.util.NoSuchElementException;

public abstract class BaseService {

    @Getter protected String serviceGroup;
    @Getter protected String serviceName;
    @Getter protected String serviceId;

    protected abstract String readServiceGroup() throws NoSuchElementException;
    protected abstract String readServiceName() throws NoSuchElementException;
    protected abstract String readServiceId() throws NoSuchElementException;

}
