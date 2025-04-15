package fr.glerious.uhcmanagerapi;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.permission.Grade;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Waiting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinAndQuitListener implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§a[+] " + event.getPlayer().getName());

        UUID uuid = event.getPlayer().getUniqueId();
        if (Main.hasGamePlayer(uuid)) return;
        Main.addGamePlayer(new GamePlayer(uuid));
        GamePlayer gamePlayer = Main.getGamePlayer(uuid);
        assert gamePlayer != null;
        if (gamePlayer.getPlayer().isOp()) gamePlayer.setGrade(Grade.HOST);
        Main.getTeamManager().joinTeam(gamePlayer, "player");

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        GamePlayer gamePlayer = Main.getGamePlayer(uuid);
        if (Main.getGameState().getClass() == Waiting.class) Main.getGamePlayers().remove(gamePlayer);
        event.setQuitMessage("§c[-] " + event.getPlayer().getName());
    }
}