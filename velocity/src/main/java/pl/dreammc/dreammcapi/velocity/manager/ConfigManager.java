package pl.dreammc.dreammcapi.velocity.manager;

import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.velocity.config.PluginConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ConfigManager {
    private final Map<Path, PluginConfiguration<?>> cachedConfigs;
    private final Yaml yaml;

    public ConfigManager() {
        this.cachedConfigs = new HashMap<>();
        this.yaml = new Yaml();
    }

    @Nullable
    public <T extends PluginConfiguration<?>> T getOrLoadConfig(Path path, Class<T> configClass) {
        if(this.cachedConfigs.containsKey(path)) {
            PluginConfiguration<?> config = this.cachedConfigs.get(path);
            if(configClass.isAssignableFrom(configClass)) {
                return (T) config;
            }
        }

        try {
            Path configPath = path.resolve("config.yml");
            if (!Files.exists(path)) {
                if(!path.toFile().mkdirs() && !configPath.toFile().createNewFile())
                    return null;
                T newConfig = (T) configClass.getConstructor().newInstance().getDefault();
                newConfig.saveConfig(configPath);
                this.cachedConfigs.put(path, newConfig);
                return newConfig;
            } else {
                try (InputStream inputStream = Files.newInputStream(configPath)) {
                    T newConfig = this.yaml.loadAs(inputStream, configClass);
                    this.cachedConfigs.put(path, newConfig);
                    return newConfig;
                }
            }
        } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            Logger.sendError(e.getMessage());
        }

        return null;
    }

}
