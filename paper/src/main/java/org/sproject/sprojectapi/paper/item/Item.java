package org.sproject.sprojectapi.paper.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Item {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    @Getter private Material material;
    @Getter private Component name;
    @Getter private int amount = 1;
    private Lore lore;
    @Getter private boolean unbreakable = false;
    @Getter private Multimap<Attribute, AttributeModifier> attributes;
    @Getter private Set<ItemFlag> flags;

    public Item() {
        this.attributes = ArrayListMultimap.create();
        this.flags = new HashSet<>();
    }

    public Item(Material material) {
        this.material = material;
        this.attributes = ArrayListMultimap.create();
        this.flags = new HashSet<>();
    }

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
        this.material = this.itemStack.getType();
        this.name = this.itemStack.displayName();
        this.amount = this.itemStack.getAmount();
        this.lore = new Lore(this, this.itemMeta.lore());
        this.unbreakable = this.itemMeta.isUnbreakable();
        this.attributes = this.itemMeta.getAttributeModifiers();
        this.flags = this.itemMeta.getItemFlags();
    }

    public Item setName(Component name) {
        this.name = name;
        return this;
    }

    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Item setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Lore lore() {
        if(this.lore == null) this.lore = new Lore(this);
        return this.lore;
    }

    public Item setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public Item addAttribute(Attribute attribute, AttributeModifier modifier) {
        this.attributes.put(attribute, modifier);
        return this;
    }

    public Item removeAttribute(Attribute attribute, AttributeModifier modifier) {
        this.attributes.remove(attribute, modifier);
        return this;
    }

    public Item removeAttribute(Attribute attribute) {
        this.attributes.removeAll(attribute);
        return this;
    }

    public Item clearAttributes() {
        this.attributes.clear();
        return this;
    }

    public boolean hasAttributeSet(Attribute attribute, AttributeModifier modifier) {
        return this.attributes.containsEntry(attribute, modifier);
    }

    public boolean hasAttribute(Attribute attribute) {
        return this.attributes.containsKey(attribute);
    }

    @Nullable
    public Collection<AttributeModifier> getAttributeModifier(Attribute attribute) {
        return this.attributes.get(attribute);
    }

    public Item addItemFlag(ItemFlag itemFlag) {
        this.flags.add(itemFlag);
        return this;
    }

    public Item removeItemFlag(ItemFlag itemFlag) {
        this.flags.remove(itemFlag);
        return this;
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        return this.flags.contains(itemFlag);
    }

    @Nullable
    public ItemStack build() {
        if(this.material == null || this.material == Material.AIR) return null;
        this.itemStack = new ItemStack(this.material);
        this.itemStack.setAmount(Math.max(Math.min(this.amount, this.material.getMaxStackSize()), 1));
        this.itemMeta = this.itemStack.getItemMeta();

        Optional.ofNullable(this.name).ifPresent(this.itemMeta::displayName);
        Optional.ofNullable(this.lore).ifPresent(lore -> this.itemMeta.lore(lore.getLoreLines()));
        this.itemMeta.setUnbreakable(this.unbreakable);
        this.itemMeta.setAttributeModifiers(this.attributes);
        this.flags.forEach(flag -> {
            this.itemMeta.addItemFlags(flag);
        });
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
