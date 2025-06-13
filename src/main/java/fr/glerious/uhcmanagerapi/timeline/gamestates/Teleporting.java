package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.timeline.Events;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnables;
import fr.glerious.uhcmanagerapi.utils.ConfigAPI;
import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Teleporting extends GameState {

    final List<GamePlayer> gamePlayers = new ArrayList<>(Main.getGamePlayers());

    public Teleporting() {
        super("Téléportation");
        runnables.add(new Runnables(0, 5) {
            @Override
            public boolean condition() {
                return gamePlayers.isEmpty();
            }

            @Override
            public void exit() {
                Bukkit.broadcastMessage(ConfigUHC.getInformation("end_teleportation"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        next();
                    }
                }.runTaskLater(Main.getMain(), Methods.seconds2ticks(5));
            }

            @Override
            public void action() {
                GamePlayer gamePlayer = gamePlayers.get(new Random().nextInt(gamePlayers.size()));
                gamePlayers.remove(gamePlayer);
                if (Main.getTeamManager().getActualGamePlayers().contains(gamePlayer)) {
                    teleportPlayer(gamePlayer);
                    return;
                }
                gamePlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        });
        clock();
    }

    @Override
    public void next() {
        super.next();
        Main.setGameState(new InGame());
    }

    @EventHandler
    public void OnDamage(EntityDamageEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER)) return;
        Player player = Bukkit.getPlayer(event.getEntity().getUniqueId());
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        if (player.isDead()) return;
        event.setCancelled(true);
    }

    private void teleportPlayer(GamePlayer gamePlayer) {
        double area = 49087.39;


        int rayNumber = (int) (Math.log( 0.25 * gamePlayers.size() + 1) / Math.log(2));
        double raySize = Math.sqrt(area * Math.pow(2, 1 + gamePlayers.size()) / Math.PI);

        double angle = Math.PI / Math.pow(2, rayNumber) * gamePlayers.size() - Math.pow(2, rayNumber + 1) + 4;
        double[] coordinates = getCoordinates(raySize, angle);
        gamePlayer.getPlayer().teleport(new Location(
                gamePlayer.getPlayer().getWorld(),
                coordinates[0],
                coordinates[1],
                coordinates[2]
        ));
    }

    private double[] getCoordinates(double raySize, double angle) {
        double x = raySize * Math.cos(angle);
        double z = raySize * Math.sin(angle);
        double fixedHeight = 150;
        return new double[] {x, fixedHeight, z};
    }
}
