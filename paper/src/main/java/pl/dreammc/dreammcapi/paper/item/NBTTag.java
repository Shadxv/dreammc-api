package pl.dreammc.dreammcapi.paper.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.persistence.PersistentDataType;

@AllArgsConstructor
@Getter
public class NBTTag<T> {
    private final String key;
    private final PersistentDataType type;
    private final T value;
}
