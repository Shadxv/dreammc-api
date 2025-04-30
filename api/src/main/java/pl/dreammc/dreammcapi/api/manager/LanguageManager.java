package pl.dreammc.dreammcapi.api.manager;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.language.LanguagePack;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.shared.Registry;
import pl.dreammc.dreammcapi.shared.language.ILanguageManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageManager implements ILanguageManager {

    @Getter private final String language;
    private final Map<String, LanguagePack> languagePacks;

    public LanguageManager(String language) {
        this.language = language;
        this.languagePacks = new HashMap<>();
    }

    @Nullable
    public LanguagePack registerPack(File configFolder) {

        LanguagePack pack = new LanguagePack(configFolder);
        if (!pack.load(this.language)) return null;
        this.languagePacks.put(configFolder.getName(), pack);

        return pack;
    }

    public List<String> getPlugins() {
        return this.languagePacks.keySet().stream().toList();
    }

    @Nullable
    public LanguagePack getPack(String pluginName) {
        return this.languagePacks.get(pluginName);
    }

    public static LanguageManager getInstance() {
        return (LanguageManager) Registry.languageManager;
    }

}
