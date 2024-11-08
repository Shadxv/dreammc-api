package pl.dreammc.dreammcapi.paper.item;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class Lore<T extends BaseItem<?>>{

    @Getter private final List<Component> loreLines;
    @Getter private final T parent;

    public Lore(T parent) {
        this.loreLines = new ArrayList<>();
        this.parent = parent;
    }

    public Lore(T parent, List<Component> loreLines) {
        this.parent = parent;
        this.loreLines = loreLines;
    }

    public Lore<T> addLine(Component component) {
        this.loreLines.add(component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public Lore<T> removeLine(int index) {
        if(index >= 0 && index < this.loreLines.size())
            this.loreLines.remove(index);
        return this;
    }

    public Lore<T> insertLine(int index, Component component) {
        if(index < 0) return this;

        if(index > this.loreLines.size())
            for(int i = this.loreLines.size(); i < index; i++)
                this.addLine(Component.empty());

        this.loreLines.add(index, component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public Lore<T> addLines(Component... components) {
        for(Component component : components) {
            this.addLine(component);
        }
        return this;
    }

    public Lore<T> insertLines(int index, Component... components) {
        if(index < 0) return this;

        for(Component component : components) {
            this.insertLine(index, component);
            index++;
        }
        return this;
    }

    public Lore<T> removeLines(int... indexes) {
        for(int i : indexes) {
            this.removeLine(i);
        }
        return this;
    }

    public Lore<T> modifyLine(int index, Component component) {
        if(index >= 0 && index < this.loreLines.size())
            this.loreLines.set(index, component);
        return this;
    }

    public Lore<T> clear() {
        this.loreLines.clear();
        return this;
    }

    public T endLore() {
        return (T) this.parent;
    }

}
