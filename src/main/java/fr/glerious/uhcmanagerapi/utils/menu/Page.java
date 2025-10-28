package fr.glerious.uhcmanagerapi.utils.menu;

import fr.glerious.uhcmanagerapi.utils.BetterItems;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class Page {

    public Page(UUID uuid, String name, int lineNumber, HashMap<Integer, BetterItems> slots) {
        this.uuid = uuid;
        this.betterItems = slots;
        this.lineNumber = lineNumber;
        this.inventory = Bukkit.createInventory(null, lineNumber * 9, name);
    }

    public final UUID uuid;

    private final int lineNumber;

    private final Inventory inventory;

    private final HashMap<Integer, BetterItems> betterItems;

    public Inventory getInventory() {
        return inventory;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void plot() {
        for (Integer slot : betterItems.keySet())
            inventory.setItem(slot, betterItems.get(slot).getItemStack());
    }
}
