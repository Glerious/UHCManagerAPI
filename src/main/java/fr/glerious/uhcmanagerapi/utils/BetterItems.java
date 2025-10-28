package fr.glerious.uhcmanagerapi.utils;

import fr.glerious.uhcmanagerapi.utils.Grade;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class BetterItems {
    private final ItemStack itemStack;

    private final ItemMeta itemMeta;

    private fr.glerious.uhcmanagerapi.utils.Grade grade = fr.glerious.uhcmanagerapi.utils.Grade.PLAYER;

    public BetterItems(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public BetterItems(Material material, String name) {
        this(material);
        setName(name);
    }

    public BetterItems(Material material, String name, fr.glerious.uhcmanagerapi.utils.Grade grade) {
        this(material);
        setGrade(grade);
        setName(name);
    }

    public BetterItems(Material material, String name, List<String> lore) {
        this(material, name);
        setLore(lore);
    }

    public BetterItems(Material material, String name, List<String> lore, boolean shiny) {
        this(material, name, lore);
        if (shiny) displayShiny();
    }

    public BetterItems(Material material, String name, List<String> lore, short data) {
        this(material, name, lore);
        setData(data);
    }

    public BetterItems(Material material, String name, List<String> lore, boolean shiny, short data) {
        this(material, name, lore, shiny);
        setData(data);
    }

    public BetterItems(Material material, String name, HashMap<Enchantment, Integer> enchantments) {
        this(material, name);
        addEnchants(enchantments);
    }

    public BetterItems(Material material, String name, Enchantment enchantment, int level) {
        this(material, name);
        addEnchant(enchantment, level);
    }

    public BetterItems(Material material, String name, HashMap<Enchantment, Integer> enchantments, List<String> lore) {
        this(material, name, enchantments);
        setLore(lore);
    }

    public BetterItems(Material material, String name, Enchantment enchantment, int level, List<String> lore) {
        this(material, name, enchantment, level);
        setLore(lore);
    }

    public BetterItems(Material material, String name, HashMap<Enchantment, Integer> enchantments, boolean hide) {
        this(material, name, enchantments);
        if (hide) hideEnchantment();
    }

    public BetterItems(Material material, String name, Enchantment enchantment, int level, boolean hide) {
        this(material, name, enchantment, level);
        if (hide) hideEnchantment();
    }


    public BetterItems(Material material, String name, HashMap<Enchantment, Integer> enchantments, boolean hide, List<String> lore) {
        this(material, name, enchantments, hide);
        setLore(lore);
    }

    public BetterItems(Material material, String name, Enchantment enchantment, int level, boolean hide, List<String> lore) {
        this(material, name, enchantment, level, hide);
        setLore(lore);
    }

    public BetterItems(Material material, String name, boolean shiny) {
        this(material, name);
        if (shiny) displayShiny();
    }

    public BetterItems(Material material, String name, boolean shiny, short data) {
        this(material, name, shiny);
        setData(data);
    }

    public BetterItems(Material material, String name, short data) {
        this(material, name);
        setData(data);
    }

    public BetterItems(Material material, HashMap<Enchantment, Integer>  enchantments) {
        this(material);
        addEnchants(enchantments);
    }

    public BetterItems(Material material, HashMap<Enchantment, Integer>  enchantments, boolean hide) {
        this(material, enchantments);
        if (hide) hideEnchantment();
    }


    public BetterItems(Material material, Enchantment enchantment, int level) {
        this(material);
        addEnchant(enchantment, level);
    }

    public BetterItems(Material material, Enchantment enchantment, int level, boolean hide) {
        this(material, enchantment, level);
        if (hide) hideEnchantment();
    }

    public BetterItems(Material material, boolean shiny) {
        this(material);
        if (shiny) displayShiny();
    }

    public BetterItems(Material material, boolean shiny, short data) {
        this(material, shiny);
        setData(data);
    }


    public BetterItems(Material material, short data) {
        this(material);
        setData(data);
    }

    private void setName(String name)
    {
        itemMeta.setDisplayName(name);
    }

    private void hideName()
    {
        setName("");
    }

    private void setGrade(fr.glerious.uhcmanagerapi.utils.Grade grade) {
        this.grade = grade;
    }

    private void setLore(List<String> lores)
    {
        itemMeta.setLore(lores);
    }

    private void addEnchants(HashMap<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment :
                enchantments.keySet())
            itemMeta.addEnchant(enchantment, enchantments.get(enchantment), true);
    }

    private void addEnchant(Enchantment enchantment, Integer integer) {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        enchantments.put(enchantment, integer);
        addEnchants(enchantments);
    }

    private void displayShiny() {
        addEnchant(Enchantment.LURE, 1);
        hideEnchantment();
    }

    private void hideEnchantment()
    {
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    private void setData(short data)
    {
        itemStack.setDurability(data);
    }

    public ItemStack getItemStack() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public fr.glerious.uhcmanagerapi.utils.Grade getGrade() {
        return grade;
    }

    public boolean hasGrade(fr.glerious.uhcmanagerapi.utils.Grade grade) {
        return this.grade.equals(grade);
    }

    public ItemStack usable(fr.glerious.uhcmanagerapi.utils.Grade grade) {
        if (this.grade.equals(fr.glerious.uhcmanagerapi.utils.Grade.HOST) && grade.equals(Grade.PLAYER)) return null;
        return getItemStack();
    }
}
