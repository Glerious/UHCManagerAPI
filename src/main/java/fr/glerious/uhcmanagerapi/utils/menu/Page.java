package fr.glerious.uhcmanagerapi.utils.menu;

import fr.glerious.uhcmanagerapi.gameplayer.BetterItems;
import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;

public class Page {

    public Page(String name, int size, List<Integer> slots, List<BetterItems> items) {
        this.betterItems = Methods.MergeList(slots, items);
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, name);
    }

    private final int size;

    private final Inventory inventory;

    private final HashMap<Integer, BetterItems> betterItems;

    public Inventory getInventory() {
        return inventory;
    }

    public int getSize() {
        return size;
    }

    public void plot() {
        for (Integer slot : betterItems.keySet())
            inventory.setItem(slot, betterItems.get(slot).getItemStack());
    }


}
