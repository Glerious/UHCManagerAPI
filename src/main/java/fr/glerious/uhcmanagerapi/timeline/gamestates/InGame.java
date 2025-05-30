package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.timeline.Events;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnables;
import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team;

public class InGame extends GameState implements Listener {

    private boolean PVPEnable;

    private final Integer PVPTime = 20 * 60;

    public InGame() {
        super("En Jeu");
        events = Main.getEvents();
        runnables = Main.getRunnables();
        runnables.add(new Runnables(0, Methods.seconds2ticks(1.5)) {
            @Override
            public boolean condition() {
                return Main.getTeamManager().getTeams().size() == 1;
            }

            @Override
            public void exit() {
                Team team = Main.getTeamManager().getTeams().get(0);
                Bukkit.broadcastMessage("§7Victoire de l'équipe : " + team.getPrefix() + " " + team.getName());
                next();
            }

            @Override
            public void action() {
                for (Team team : Main.getTeamManager().getTeams())
                    if (team.getEntries().isEmpty()) Main.getTeamManager().removeTeam(team.getName());
                //TODO retirer les joueur à leur mort si la condition du PVP ou voir si on les rajoute quand ils sont revives
                //TODO tester si ça desenregistre bien car semble local
            }
        });
        events.add(new Events(Methods.seconds2ticks(PVPTime)) {
            @Override
            public void action() {
                for (GamePlayer gamePlayer : Main.getGamePlayers())
                    gamePlayer.getPlayer().sendMessage("Activation du pvp !");
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
        GamePlayer gamePlayer = Main.getGamePlayer(event.getEntity().getUniqueId());
        if (gamePlayer == null) return;
        if (isPVPEnable()) {
            gamePlayer.spectate();
            Main.getTeamManager().quitTeam(gamePlayer);
            return;
        }
        gamePlayer.revive(event.getEntity().getLocation());
        event.setKeepInventory(true);
    }

}
