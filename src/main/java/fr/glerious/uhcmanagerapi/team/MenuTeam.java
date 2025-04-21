package fr.glerious.uhcmanagerapi.team;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.BetterItems;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.utils.Menu;
import fr.glerious.uhcmanagerapi.utils.Methods;
import fr.glerious.uhcmanagerapi.utils.menu.Page;
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
        Page page = new Page(name, 1, slots, betterItems);
        addPage("0", page, true);
    }

    @EventHandler
    public void onClassItemClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(name)) return;
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        assert gamePlayer != null;
        if (gamePlayer.getGameTeam() != null) {
            Main.getTeamManager().quitTeam(gamePlayer, gamePlayer.getGameTeam().getName());
        }
        ItemStack item = event.getCurrentItem();
        Team team = Main.getTeamManager().getTeamByName(item.getItemMeta().getDisplayName());
        Main.getTeamManager().joinTeam(gamePlayer, team.getName());
        gamePlayer.setTeam(team);
        gamePlayer.getSideBar().updateTeam();
        event.setCancelled(true);
        player.closeInventory();
    }
}