package fr.glerious.uhcmanagerapi.permission;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.BetterItems;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.utils.ConfigAPI;
import fr.glerious.uhcmanagerapi.utils.Menu;
import fr.glerious.uhcmanagerapi.utils.menu.Page;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class HostMenu extends Menu implements Listener {

    public HostMenu() {
        super(ConfigAPI.getInformation("host_menu_name"));
        List<Integer> slots = Arrays.asList(0, 4, 22);
        List<BetterItems> betterItems = Arrays.asList(
                new BetterItems(Material.BARRIER, "§c§lBack"),
                new BetterItems(Material.SKULL_ITEM, "§1PlayerHead"),
                new BetterItems(Material.SLIME_BALL, "§a§lStart")
        );
        Page page = new Page(name, 3, slots, betterItems);
        addPage("0", page, true);
    }

    @EventHandler
    public void onClassItemClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(name)) return;
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        assert gamePlayer != null;
        if (event.getCursor() == null) return;
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        event.setCancelled(true);
        switch (item.getItemMeta().getDisplayName()) {
            case "§1PlayerHead": getPlayerInfo(player); break;
            case "§a§lStart": startGame(player); break;
            case "§c§lBack": player.closeInventory(); break;
        }
    }

    private void getPlayerInfo(Player player) {
        player.updateInventory();
        player.performCommand("info");
    }

    private void startGame(Player player) {
        player.performCommand("uhc start");
    }
}


