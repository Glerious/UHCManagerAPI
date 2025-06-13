package fr.glerious.uhcmanagerapi.team;

import fr.glerious.javautils.Methods;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.javautils.BetterItems;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.javautils.Menu;
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
        for (Team team : Main.getTeamManager().getTeams())
            betterItems.add(new BetterItems(Material.WATER_LILY, team.getPrefix() + team.getName(), true));
        modifyBasePage(1, Methods.list2Hash(slots, betterItems));
    }

    @EventHandler
    public void onClassItemClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(name)) return;
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        assert gamePlayer != null;
        ItemStack item = event.getCurrentItem();
        if (item.getType().equals(Material.AIR)) return;
        Team team = Main.getTeamManager().getTeamByName(item.getItemMeta().getDisplayName(), true);
        if (gamePlayer.getTeam().equals(team))
            team = Main.getTeamManager().getSpectatorTeam();
        Main.getTeamManager().quitTeam(gamePlayer);
        Main.getTeamManager().joinTeam(gamePlayer, team.getName());
        event.setCancelled(true);
        player.closeInventory();
    }
}