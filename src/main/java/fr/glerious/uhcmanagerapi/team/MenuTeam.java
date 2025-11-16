package fr.glerious.uhcmanagerapi.team;

import fr.glerious.uhcmanagerapi.ConfigUHC;
import fr.glerious.uhcmanagerapi.utils.Methods;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.utils.BetterItems;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.utils.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class MenuTeam extends Menu implements Listener {

    public MenuTeam() {
        super("§l§e - Choix des Equipes");
        List<Integer> slots = Methods.rangedList(0, 8);
        List<BetterItems> betterItems = new ArrayList<>();
        for (UHCTeam UHCteam : Main.getTeamManager().getUHCteams()) {
            betterItems.add(new BetterItems(Material.WATER_LILY,
                            UHCteam.getPrefix() + UHCteam.getName() + " (" + UHCteam.getActualSize() + "/" + UHCteam.getMaximumSize() + ")",
                            UHCteam.getEntriesDisplayed(),
                            true));
        }
        modifyBasePage(1, Methods.list2Hash(slots, betterItems));
    }

    @EventHandler
    public void onClassItemClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(name)) return;
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        ItemStack item = event.getCurrentItem();
        if (gamePlayer == null) {
            player.sendMessage(ConfigUHC.getExpected("player_require"));
            return;
        }
        if (item == null) {
            player.sendMessage(ConfigUHC.getExpected("item_require"));
            return;
        }
        if (item.getType().equals(Material.AIR)) return;
        String itemName = item.getItemMeta().getDisplayName();
        String teamNameSearched = itemName.split(" ")[0];
        Team team = Main.getTeamManager().getTeamByName(teamNameSearched, true);
        if (gamePlayer.getTeam().equals(team))
            team = Main.getTeamManager().getSpectatorTeam();
        Main.getTeamManager().quitTeam(gamePlayer);
        Main.getTeamManager().joinTeam(gamePlayer, team.getName());
        event.setCancelled(true);
        player.closeInventory();
    }
}