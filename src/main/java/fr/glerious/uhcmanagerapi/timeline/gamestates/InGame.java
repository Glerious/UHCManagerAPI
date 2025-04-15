package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.timeline.Events;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

public class InGame extends GameState implements Listener {

    private boolean PVPEnable;

    public InGame() {
        super("En Jeu");
        events = Main.getEvents();
        runnables = Main.getRunnables();
        runnables.add(new Runnables(0, 30) {
            @Override
            public boolean condition() {
                return Main.getTeamManager().getTeams().size() == 1;
            }

            @Override
            public void exit() {
                Team team = Main.getTeamManager().getTeams().get(0);
                Bukkit.broadcastMessage("§7Victoire de l'équipe : " + team.getPrefix() + " " + team.getName());
                Main.getTeamManager().getTeams().clear();
                Main.getGameState().next();
            }

            @Override
            public void action() {
                Main.getTeamManager().getTeams().forEach(
                        team -> {if (team.getEntries().isEmpty()) team.unregister();
                        });
                //TODO retirer les joueur à leur mort si la condition du PVP ou voir si on les rajoute quand ils sont revives
                //TODO tester si ça desenregistre bien car semble local
            }
        });
        events.add(new Events(20 * 60 * 20) {
            @Override
            public void action() {
                Main.getGamePlayers().forEach(
                        gamePlayer -> gamePlayer.getPlayer().sendMessage("Activation du pvp !")
                );
                PVPEnable = true;
            }
        });
    }

    private boolean isPVPEnable() {
        return PVPEnable;
    }

    @Override
    public void next() {
        super.next();
        Main.setGameState(new Restarting());
    }

    public boolean revive(Player player) {
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        if (gamePlayer == null) return false;
        if (PVPEnable) {
            gamePlayer.revive();
            return true;
        }
        return false;
    }

}
