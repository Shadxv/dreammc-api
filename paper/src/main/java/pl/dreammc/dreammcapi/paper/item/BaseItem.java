package pl.dreammc.dreammcapi.paper.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class BaseItem<T extends BaseItem<?>> implements Cloneable{

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    private Material material;
    @Nullable private Component name;
    private int amount = 1;
    @Nullable private Lore<T> lore;
    private boolean unbreakable = false;
    private final Multimap<Attribute, AttributeModifier> attributes;
    private final Set<ItemFlag> flags;
    @Nullable private PotionType potionType;
    private final Map<Enchantment, EnchantData> enchantments;
    @Nullable private Map<String, NBTTag<?>> tags;
    @Nullable private PlayerProfile skullProfile;
    @Getter private final boolean isConverted;

    protected BaseItem() {
        this.isConverted = false;
        this.attributes = ArrayListMultimap.create();
        this.flags = new HashSet<>();
        this.enchantments = new HashMap<>();
        this.tags = new HashMap<>();
    }

    protected BaseItem(Material material) {
        this.isConverted = false;
        this.material = material;
        this.attributes = ArrayListMultimap.create();
        this.flags = new HashSet<>();
        this.enchantments = new HashMap<>();
        this.tags = new HashMap<>();
    }

    protected BaseItem(ItemStack itemStack) {
        this.isConverted = true;
        this.itemStack = itemStack.clone();
        this.itemMeta = this.itemStack.getItemMeta();
        this.attributes = null;
        this.flags = null;
        this.enchantments = null;
//        this.material = this.itemStack.getType();
//        this.name = this.itemStack.displayName();
//        this.amount = this.itemStack.getAmount();
//        this.lore = new Lore<T>((T) this, this.itemMeta.lore());
//        this.unbreakable = this.itemMeta.isUnbreakable();
//        this.attributes = this.itemMeta.getAttributeModifiers();
//        this.flags = this.itemMeta.getItemFlags();
//        if (this.itemMeta instanceof PotionMeta potionMeta) {
//            this.potionType = potionMeta.getBasePotionType();
//        }
//        this.enchantments = new HashMap<>();
//        for(Map.Entry<Enchantment, Integer> entry : this.itemMeta.getEnchants().entrySet()) {
//            this.enchantments.put(entry.getKey(), new EnchantData(entry.getValue(), entry.getKey().getMaxLevel() < entry.getValue()));
//        }
//        this.persistentDataContainer = this.itemMeta.getPersistentDataContainer();
//        if(this.itemMeta instanceof SkullMeta skullMeta) {
//            this.skullProfile = skullMeta.getPlayerProfile();
//        }
    }

    public Material getMaterial() {
        if(this.isConverted) return this.itemStack.getType();
        return this.material;
    }

    @Nullable
    public Component getName() {
        if(this.isConverted) return this.itemMeta.displayName();
        return this.name;
    }

    public T setName(Component name) {
        if(this.isConverted) {
            if(name == null) this.itemMeta.displayName(null);
            else this.itemMeta.displayName(name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            return (T) this;
        }
        if(name == null) this.name = null;
        else this.name = name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        return (T) this;
    }

    public int getAmount() {
        if(this.isConverted) return this.itemStack.getAmount();
        return this.amount;
    }

    public T setAmount(int amount) {
        if(this.isConverted) {
            this.itemStack.setAmount(Math.max(Math.min(amount, this.itemStack.getMaxStackSize()), 1));
            return (T) this;
        }
        this.amount = amount;
        return (T) this;
    }

    public T setMaterial(Material material) {
        if(this.isConverted) return (T) this;
        this.material = material;
        return (T) this;
    }

    public Lore<T> lore() {
        if(this.lore == null) {
            if(this.isConverted) this.lore = new Lore<T>((T) this, this.itemMeta.lore());
            else this.lore = new Lore<T>((T) this);
        }
        return this.lore;
    }

    public boolean isUnbreakable() {
        if(this.isConverted) return this.itemMeta.isUnbreakable();
        return this.unbreakable;
    }

    public T setUnbreakable(boolean unbreakable) {
        if(this.isConverted) {
            this.itemMeta.setUnbreakable(unbreakable);
            return (T) this;
        }
        this.unbreakable = unbreakable;
        return (T) this;
    }

    public T addAttribute(Attribute attribute, AttributeModifier modifier) {
        if(this.isConverted) {
            this.itemMeta.addAttributeModifier(attribute, modifier);
            return (T) this;
        }
        this.attributes.put(attribute, modifier);
        return (T) this;
    }

    public T removeAttribute(Attribute attribute, AttributeModifier modifier) {
        if(this.isConverted) {
            this.itemMeta.removeAttributeModifier(attribute, modifier);
            return (T) this;
        }
        this.attributes.remove(attribute, modifier);
        return (T) this;
    }

    public T removeAttribute(Attribute attribute) {
        if(this.isConverted) {
            this.itemMeta.removeAttributeModifier(attribute);
            return (T) this;
        }
        this.attributes.removeAll(attribute);
        return (T) this;
    }

    public T clearAttributes() {
        if(this.isConverted) {
            if(this.itemMeta.getAttributeModifiers() != null)
                this.itemMeta.getAttributeModifiers().clear();
            return (T) this;
        }
        this.attributes.clear();
        return (T) this;
    }

    public boolean hasAttributeSet(Attribute attribute, AttributeModifier modifier) {
        if(this.isConverted) {
            if(this.itemMeta.getAttributeModifiers() != null)
                return this.itemMeta.getAttributeModifiers().containsEntry(attribute, modifier);
            return false;
        }
        return this.attributes.containsEntry(attribute, modifier);
    }

    public boolean hasAttribute(Attribute attribute) {
        if(this.isConverted) {
            if(this.itemMeta.getAttributeModifiers() != null)
                return this.itemMeta.getAttributeModifiers().containsKey(attribute);
            return false;
        }
        return this.attributes.containsKey(attribute);
    }

    @Nullable
    public Collection<AttributeModifier> getAttributeModifier(Attribute attribute) {
        if(this.isConverted) {
            if(this.itemMeta.getAttributeModifiers() != null)
                return this.itemMeta.getAttributeModifiers().get(attribute);
            return null;
        }
        return this.attributes.get(attribute);
    }

    public T addItemFlag(ItemFlag itemFlag) {
        if(this.isConverted) {
            this.itemMeta.addItemFlags(itemFlag);
            return (T) this;
        }
        this.flags.add(itemFlag);
        return (T) this;
    }

    public T removeItemFlag(ItemFlag itemFlag) {
        if(this.isConverted) {
            this.itemMeta.removeItemFlags(itemFlag);
            return (T) this;
        }
        this.flags.remove(itemFlag);
        return (T) this;
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        if(this.isConverted) {
            return this.itemMeta.hasItemFlag(itemFlag);
        }
        return this.flags.contains(itemFlag);
    }

    public T addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestrictions) {
        if(this.isConverted) {
            this.itemMeta.addEnchant(enchantment, level, ignoreLevelRestrictions);
            return (T) this;
        }
        this.enchantments.put(enchantment, new EnchantData(level, ignoreLevelRestrictions));
        return (T) this;
    }

    public T removeEnchant(Enchantment enchantment) {
        if(this.isConverted) {
            this.itemMeta.removeEnchant(enchantment);
            return (T) this;
        }
        this.enchantments.remove(enchantment);
        return (T) this;
    }

    public T clearEnchants() {
        if(this.isConverted) {
            this.itemMeta.removeEnchantments();
            return (T) this;
        }
        this.enchantments.clear();
        return (T) this;
    }

    public boolean hasEnchant(Enchantment enchantment) {
        if(this.isConverted) {
            return this.itemMeta.hasEnchant(enchantment);
        }
        return this.enchantments.containsKey(enchantment);
    }

    public int getEnchantLevel(Enchantment enchantment) {
        if(this.isConverted) {
            return this.itemMeta.hasEnchant(enchantment) ? this.itemMeta.getEnchantLevel(enchantment) : 0;
        }
        return this.enchantments.containsKey(enchantment) ? this.enchantments.get(enchantment).getLevel() : 0;
    }

    public Map<Enchantment, Integer> getEnchants() {
        if (this.isConverted) {
            return this.itemMeta.getEnchants();
        }
        return this.enchantments.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().getLevel()));
    }

    @Nullable
    public PotionType getPotionType() {
        if(this.isConverted) {
            if(this.itemMeta instanceof PotionMeta potionMeta) return potionMeta.getBasePotionType();
            return null;
        }
        return this.potionType;
    }

    public T setPotionType(PotionType type) {
        if(this.isConverted) {
            if(this.itemMeta instanceof PotionMeta potionMeta)
                potionMeta.setBasePotionType(type);
            return (T) this;
        }
        this.potionType = type;
        return (T) this;
    }

    public <V> T addNBTTag(String key, PersistentDataType<?, V> type, V value) {
        if(this.isConverted) {
            this.itemMeta.getPersistentDataContainer().set(new NamespacedKey(PaperDreamMCAPI.getInstance(), key), type, value);
        } else this.tags.put(key, new NBTTag<V>(key, type, value));
        return (T) this;
    }

    public <V> boolean hasNBTTag(String key, PersistentDataType<?, V> type) {
        if(this.isConverted) {
            return this.itemMeta.getPersistentDataContainer().has(new NamespacedKey(PaperDreamMCAPI.getInstance(), key), type);
        } else return this.tags.containsKey(key) && this.tags.get(key).getType().equals(type);
    }

    @Nullable
    public <V> V getNBTTagValue(String key, PersistentDataType<?, V> type) {
        if (!this.hasNBTTag(key, type)) return null;

        if(this.isConverted) {
            return this.itemMeta.getPersistentDataContainer().get(new NamespacedKey(PaperDreamMCAPI.getInstance(), key), type);
        } else return (V) this.tags.get(key).getValue();
    }

    public T setSkullProfile(PlayerProfile profile) {
        if(this.isConverted) {
            if(this.itemMeta instanceof SkullMeta skullMeta)
                skullMeta.setPlayerProfile(profile);
            return (T) this;
        }
        this.skullProfile = profile;
        return (T) this;
    }

    public T setSkullProfile(String texture, String signature) {
        if(this.isConverted) {
            if(this.itemMeta instanceof SkullMeta skullMeta) {
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                profile.getProperties()
                        .add(new ProfileProperty("textures", texture, signature));
                skullMeta.setPlayerProfile(profile);
            }
            return (T) this;
        }
        this.skullProfile = Bukkit.createProfile(UUID.randomUUID());
        this.skullProfile.getProperties()
                .add(new ProfileProperty("textures", texture, signature));
        return (T) this;
    }

    @Nullable
    public ItemStack build() {
        if(this.isConverted) {
            Optional.ofNullable(this.lore).ifPresent(lore -> this.itemMeta.lore(lore.getLoreLines()));
            this.itemStack.setItemMeta(this.itemMeta);
            return this.itemStack;
        }
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

        Optional.ofNullable(this.tags).ifPresent(tagMap -> {
            for(NBTTag<?> tag : tagMap.values()) {
                this.itemMeta.getPersistentDataContainer().set(new NamespacedKey(PaperDreamMCAPI.getInstance(), tag.getKey()), tag.getType(), tag.getValue());
            }
        });
        Optional.ofNullable(this.skullProfile).ifPresent(profile -> {
            if (this.itemMeta instanceof SkullMeta skullMeta) {
                skullMeta.setPlayerProfile(profile);
            }
        });
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    @Override
    public BaseItem<T> clone() {
        try {
            if(this.isConverted) throw new CloneNotSupportedException();
            BaseItem<T> clone = (BaseItem<T>) super.clone();
            clone.material = this.material;
            clone.name = this.name;
            clone.amount = this.amount;
            clone.lore = new Lore<>((T) clone, new ArrayList<>(this.lore().getLoreLines()));
            clone.unbreakable = this.unbreakable;
            clone.attributes.putAll(this.attributes);
            clone.flags.addAll(this.flags);
            clone.potionType = this.potionType;
            clone.enchantments.putAll(this.enchantments);

            for (NBTTag<?> tag : this.tags.values()) {
                clone.tags.put(tag.getKey(), tag);
            }

            clone.skullProfile = this.skullProfile;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
