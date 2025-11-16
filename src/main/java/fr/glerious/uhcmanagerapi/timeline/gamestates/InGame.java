package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.team.UHCTeam;
import fr.glerious.uhcmanagerapi.timeline.Event;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnable;
import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class InGame extends GameState implements Listener {

    private boolean PVPEnable;

    private final Integer PVPTime = Methods.seconds2ticks(60);

    public InGame() {
        super("En Jeu");
        events = Main.getEvents();
        runnables = Main.getRunnables();

        runnables.add(new Runnable(0, Methods.seconds2ticks(1.5)) {
            @Override
            public boolean condition() {
                return Main.getTeamManager().getUHCteams().size() == 1;
            }

            @Override
            public void exit() {
                UHCTeam UHCteam = Main.getTeamManager().getUHCteams().get(0);
                Bukkit.broadcastMessage("§7Victoire de l'équipe : " + UHCteam.getPrefix() + " " + UHCteam.getName());
                next();
            }

            @Override
            public void action() {
                for (UHCTeam UHCteam : Main.getTeamManager().getUHCteams())
                    if (UHCteam.getActualSize() == 0) Main.getTeamManager().removeTeam(UHCteam.getName());
                //TODO retirer les joueur à leur mort si la condition du PVP ou voir si on les rajoute quand ils sont revives
                //TODO tester si ça desenregistre bien car semble local
            }
        });
        events.add(new Event(PVPTime) {
            @Override
            public boolean condition() {
                return false;
            }

            @Override
            public void exit() {

            }

            @Override
            public void action() {
                for (GamePlayer gamePlayer : Main.getGamePlayers())
                    gamePlayer.getPlayer().sendMessage("§7[§6UHC§7] - §cActivation du pvp !");
                PVPEnable = true;
            }
        });
        clock();
    }

    private boolean isPVPEnable() {
        return PVPEnable;
    }

    public Integer getPVPTime() {
        return PVPTime;
    }

    @Override
    public void next() {
        super.next();
        Main.setGameState(new Restarting());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = Main.getGamePlayer(event.getEntity().getUniqueId());
        if (gamePlayer == null) return;
        if (isPVPEnable()) {
            event.setDeathMessage(
                    "§7---------------------\n" +
                    player.getName() + "§7 est mort.\n" +
                    "§7---------------------"
            );
            gamePlayer.spectate();
            return;
        }
        event.setDeathMessage(
                "§7Revive du joueur :§6 " + player.getName()
        );
        gamePlayer.revive(player.getLocation());
        event.setKeepInventory(true);
    }
}
