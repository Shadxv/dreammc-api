package pl.dreammc.dreammcapi.api.language.model;

import net.kyori.adventure.text.Component;
import pl.dreammc.dreammcapi.api.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class LangList extends LangObject<List<String>, List<Component>>{

    public LangList(List<String> value) {
        super(value);
    }

    @Override
    public LangObject<List<String>, List<Component>>.Builder getBuilder() {
        return new Builder() {
            @Override
            public List<String> buildAsString() {
                List<String> result = new ArrayList<>();
                for (String line : this.text) {
                    result.add(this.build(line));
                }
                return result;
            }

            @Override
            public List<Component> buildAsComponent() {
                List<Component> result = new ArrayList<>();
                for (String line : this.text) {
                    result.add(TextUtil.deserializeText(this.build(line)));
                }
                return result;
            }
        };
    }
}
