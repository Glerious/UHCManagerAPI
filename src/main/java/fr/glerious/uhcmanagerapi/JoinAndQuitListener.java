package fr.glerious.uhcmanagerapi;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.permission.Grade;
import fr.glerious.uhcmanagerapi.team.TeamManager;
import fr.glerious.uhcmanagerapi.timeline.gamestates.InGame;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Waiting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class JoinAndQuitListener implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = event.getPlayer().getUniqueId();
        event.setJoinMessage("§a[+] " + player.getName());
        GamePlayer gamePlayer = new GamePlayer(uuid);
        if (Main.hasGamePlayer(uuid)) gamePlayer = Main.getGamePlayer(uuid);
        try {
            assert gamePlayer != null : ConfigUHC.getExpected("player_require");;
        } catch (AssertionError error) {
            player.sendMessage(error.getMessage());
        }
        gamePlayer.getSideBar().showScoreboard();
        if (Main.getGameState() instanceof Waiting)
            gamePlayer.getPlayer().addPotionEffect(PotionEffectType.SATURATION.createEffect(10000, 0), true);
        if (gamePlayer.getPlayer().isOp()) gamePlayer.setGrade(Grade.HOST);
        Main.addGamePlayer(gamePlayer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        GamePlayer gamePlayer = Main.getGamePlayer(uuid);
        if (Main.getGameState() instanceof Waiting) {
            Main.getTeamManager().quitTeam(gamePlayer);
            Main.getGamePlayers().remove(gamePlayer);
        }
        event.setQuitMessage("§c[-] " + event.getPlayer().getName());
    }
}