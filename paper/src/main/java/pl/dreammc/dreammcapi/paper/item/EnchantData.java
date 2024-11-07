package pl.dreammc.dreammcapi.paper.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnchantData {
    private final int level;
    private final boolean ignoreLevelRestrictions;
}
