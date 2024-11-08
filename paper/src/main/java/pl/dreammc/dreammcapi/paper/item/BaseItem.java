package pl.dreammc.dreammcapi.paper.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;

import java.util.*;

@SuppressWarnings("unchecked")
public class BaseItem<T extends BaseItem<?>> implements Cloneable{

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    @Getter private Material material;
    @Getter private Component name;
    @Getter private int amount = 1;
    private Lore<T> lore;
    @Getter private boolean unbreakable = false;
    @Getter private final Multimap<Attribute, AttributeModifier> attributes;
    @Getter private final Set<ItemFlag> flags;
    @Getter private PotionType potionType;
    @Getter private final Map<Enchantment, EnchantData> enchantments;
    @Getter @Nullable private Map<String, NBTTag<?>> tags;
    @Nullable private PersistentDataContainer persistentDataContainer;

    protected BaseItem() {
        this.attributes = ArrayListMultimap.create();
        this.flags = new HashSet<>();
        this.enchantments = new HashMap<>();
        this.tags = new HashMap<>();
    }

    protected BaseItem(Material material) {
        this.material = material;
        this.attributes = ArrayListMultimap.create();
        this.flags = new HashSet<>();
        this.enchantments = new HashMap<>();
        this.tags = new HashMap<>();
    }

    protected BaseItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
        this.material = this.itemStack.getType();
        this.name = this.itemStack.displayName();
        this.amount = this.itemStack.getAmount();
        this.lore = new Lore<T>((T) this, this.itemMeta.lore());
        this.unbreakable = this.itemMeta.isUnbreakable();
        this.attributes = this.itemMeta.getAttributeModifiers();
        this.flags = this.itemMeta.getItemFlags();
        this.enchantments = new HashMap<>();
        for(Map.Entry<Enchantment, Integer> entry : this.itemMeta.getEnchants().entrySet()) {
            this.enchantments.put(entry.getKey(), new EnchantData(entry.getValue(), entry.getKey().getMaxLevel() < entry.getValue()));
        }
        this.persistentDataContainer = this.itemMeta.getPersistentDataContainer();
    }

    public T setName(Component name) {
        if(name == null) this.name = null;
        else this.name = name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        return (T) this;
    }

    public T setAmount(int amount) {
        this.amount = amount;
        return (T) this;
    }

    public T setMaterial(Material material) {
        this.material = material;
        return (T) this;
    }

    public Lore<T> lore() {
        if(this.lore == null) this.lore = new Lore<T>((T) this);
        return this.lore;
    }

    public T setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return (T) this;
    }

    public T addAttribute(Attribute attribute, AttributeModifier modifier) {
        this.attributes.put(attribute, modifier);
        return (T) this;
    }

    public T removeAttribute(Attribute attribute, AttributeModifier modifier) {
        this.attributes.remove(attribute, modifier);
        return (T) this;
    }

    public T removeAttribute(Attribute attribute) {
        this.attributes.removeAll(attribute);
        return (T) this;
    }

    public T clearAttributes() {
        this.attributes.clear();
        return (T) this;
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

    public T addItemFlag(ItemFlag itemFlag) {
        this.flags.add(itemFlag);
        return (T) this;
    }

    public T removeItemFlag(ItemFlag itemFlag) {
        this.flags.remove(itemFlag);
        return (T) this;
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        return this.flags.contains(itemFlag);
    }

    public T addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestrictions) {
        this.enchantments.put(enchantment, new EnchantData(level, ignoreLevelRestrictions));
        return (T) this;
    }

    public T removeEnchant(Enchantment enchantment) {
        this.enchantments.remove(enchantment);
        return (T) this;
    }

    public boolean hasEnchant(Enchantment enchantment) {
        return this.enchantments.containsKey(enchantment);
    }

    public int getEnchantLevel(Enchantment enchantment) {
        return this.enchantments.get(enchantment).getLevel();
    }

    public T setPotionType(PotionType type) {
        this.potionType = type;
        return (T) this;
    }

    @Nullable
    public ItemStack build() {
        if (this.material == null || this.material == Material.AIR) return null;
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
        Optional.ofNullable(this.potionType).ifPresent(type -> {
            if (this.itemMeta instanceof PotionMeta potionMeta)
                potionMeta.setBasePotionType(this.potionType);
        });
        this.itemMeta.removeEnchantments();
        for (Map.Entry<Enchantment, EnchantData> enchant : this.enchantments.entrySet()) {
            this.itemMeta.addEnchant(enchant.getKey(), enchant.getValue().getLevel(), enchant.getValue().isIgnoreLevelRestrictions());
        }
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    public <V> T addNBTTag(String key, PersistentDataType<?, V> type, V value) {
        if(this.persistentDataContainer != null) {
            this.persistentDataContainer.set(new NamespacedKey(PaperDreamMCAPI.getInstance(), key), type, value);
        } else this.tags.put(key, new NBTTag<V>(key, type, value));
        return (T) this;
    }

    public <V> boolean hasNBTTag(String key, PersistentDataType<?, V> type) {
        if(this.persistentDataContainer != null) {
            return this.persistentDataContainer.has(new NamespacedKey(PaperDreamMCAPI.getInstance(), key), type);
        } else return this.tags.containsKey(key) && this.tags.get(key).getType().equals(type);
    }

    @Nullable
    public <V> V getNBTTagValue(String key, PersistentDataType<?, V> type) {
        if (!this.hasNBTTag(key, type)) return null;

        if(this.persistentDataContainer != null) {
            return this.persistentDataContainer.get(new NamespacedKey(PaperDreamMCAPI.getInstance(), key), type);
        } else return (V) this.getTags().get(key).getValue();
    }

    @Override
    public BaseItem<T> clone() {
        try {
            BaseItem<T> clone = (BaseItem<T>) super.clone();
            clone.material = this.material;
            clone.name = this.name;
            clone.amount = this.amount;
            clone.lore = new Lore<>((T) clone, this.lore().getLoreLines());
            clone.unbreakable = this.unbreakable;
            clone.attributes.putAll(this.attributes);
            clone.flags.addAll(this.flags);
            clone.potionType = this.potionType;
            clone.enchantments.putAll(this.enchantments);
            if(this.persistentDataContainer != null) clone.persistentDataContainer = this.persistentDataContainer;
            else {
                for (NBTTag<?> tag : this.tags.values()) {
                    clone.tags.put(tag.getKey(), tag);
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
