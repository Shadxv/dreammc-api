package pl.dreammc.dreammcapi.api.language.model;

import net.kyori.adventure.text.Component;
import pl.dreammc.dreammcapi.api.util.TextUtil;

public class LangText extends LangObject<String, Component>{
    public LangText(String value) {
        super(value);
    }

    @Override
    public LangObject<String, Component>.Builder getBuilder() {
        return new Builder() {
            @Override
            public String buildAsString() {
                return this.build(this.text);
            }

            @Override
            public Component buildAsComponent() {
                return TextUtil.deserializeText(this.buildAsString());
            }
        };
    }
}
