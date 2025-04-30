package pl.dreammc.dreammcapi.api.language;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bson.json.JsonReader;
import pl.dreammc.dreammcapi.api.language.model.LangList;
import pl.dreammc.dreammcapi.api.language.model.LangObject;
import pl.dreammc.dreammcapi.api.language.model.LangText;
import pl.dreammc.dreammcapi.api.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguagePack {

    private final Map<String, LangObject<?, ?>> cache;
    private final File folder;

    public LanguagePack(File folder) {
        this.cache = new HashMap<>();
        this.folder = folder;
    }

    public boolean reload(String lang) {
        this.cache.clear();
        return this.load(lang);
    }

    public boolean load(String lang) {
        long startTime = System.currentTimeMillis();
        File langFile = new File(this.folder, lang + ".json");
        if (!langFile.isFile()) {
            Logger.sendError("Language pack not found!");
            return false;
        }

        try {
            JsonElement root = JsonParser.parseReader(new FileReader(langFile));
            processJsonObject(root, "");
        } catch (FileNotFoundException e) {
            Logger.sendError("Language pack not found!");
            return false;
        }

        Logger.sendInfo("Loaded language pack for " + this.folder.getName() + " plugin in " + (System.currentTimeMillis() - startTime) + "ms.");
        return true;
    }

    private void processJsonObject(JsonElement element, String path) {
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            String newPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
            if (entry.getValue().isJsonObject()) processJsonObject(entry.getValue(), newPath);
            else if (entry.getValue().isJsonArray()) {
                List<String> list = new ArrayList<>();
                element.getAsJsonArray().forEach(e -> list.add(e.getAsString()));
                this.cache.put(newPath, new LangList(list));
            } else this.cache.put(newPath, new LangText(entry.getValue().getAsString()));
        }
    }

    public LangText getText(String key) {
       LangObject<?, ?> result = this.cache.get(key);
       if (!(result instanceof LangText)) throw new RuntimeException("LangPack: " + key + " does not exist or is not a LangText");
       return (LangText) result;
    }

    public LangList getList(String key) {
        LangObject<?, ?> result = this.cache.get(key);
        if (!(result instanceof LangList)) throw new RuntimeException("LangPack: " + key + " does not exist or is not a LangList");
        return (LangList) result;
    }

}
