package fr.glerious.uhcmanagerapi.permission;

import fr.glerious.uhcmanagerapi.utils.Methods;
import fr.glerious.uhcmanagerapi.ConfigUHC;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.utils.BetterItems;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.utils.Menu;
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
        super(ConfigUHC.getInformation("host_menu_name"));
        List<Integer> slots = Arrays.asList(0, 4, 22);
        List<BetterItems> betterItems = Arrays.asList(
                new BetterItems(Material.BARRIER, "§c§lBack"),
                new BetterItems(Material.SKULL_ITEM, "§1PlayerHead"),
                new BetterItems(Material.SLIME_BALL, "§a§lStart")
        );
        modifyBasePage(3, Methods.list2Hash(slots, betterItems));
    }

    @EventHandler
    public void onClassItemClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(name)) return;
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        assert gamePlayer != null;
        if (event.getCursor() == null) return;
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.AIR) return;
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


