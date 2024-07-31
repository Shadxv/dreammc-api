package org.sproject.sprojectapi.paper.item;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class Lore {

    @Getter private final List<Component> loreLines;
    @Getter private final Item parent;

    public Lore(Item parent) {
        this.loreLines = new ArrayList<>();
        this.parent = parent;
    }

    public Lore(Item parent, List<Component> loreLines) {
        this.parent = parent;
        this.loreLines = loreLines;
    }

    public Lore addLine(Component component) {
        this.loreLines.add(component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public Lore removeLine(int index) {
        if(index >= 0 && index < this.loreLines.size())
            this.loreLines.remove(index);
        return this;
    }

    public Lore insertLine(int index, Component component) {
        if(index < 0) return this;

        if(index > this.loreLines.size())
            for(int i = this.loreLines.size(); i < index; i++)
                this.addLine(Component.empty());

        this.loreLines.add(index, component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public Lore addLines(Component... components) {
        for(Component component : components) {
            this.addLine(component);
        }
        return this;
    }

    public Lore insertLines(int index, Component... components) {
        if(index < 0) return this;

        for(Component component : components) {
            this.insertLine(index, component);
            index++;
        }
        return this;
    }

    public Lore removeLines(int... indexes) {
        for(int i : indexes) {
            this.removeLine(i);
        }
        return this;
    }

    public Lore modifyLine(int index, Component component) {
        if(index >= 0 && index < this.loreLines.size())
            this.loreLines.set(index, component);
        return this;
    }

    public Lore clear() {
        this.loreLines.clear();
        return this;
    }

    public Item endLore() {
        return this.parent;
    }
}
