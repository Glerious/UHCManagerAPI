package fr.glerious.uhcmanagerapi.team;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.utils.ConfigAPI;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();;

    private final List<Team> teams = new ArrayList<>();

    public TeamManager() {
        addTeam("Team 1", "", false);
        //TODO : importer les teams depuis un fichier xml.
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeamByName(String teamName) {
        for (Team team : getTeams())
            if (team.getName().equals(teamName)) return team;
        return null;
    }

    public void joinTeam(GamePlayer gamePlayer, String teamName) {
        Team team = getTeamByName(teamName);
        if (team == null) {
            gamePlayer.getPlayer().sendMessage(ConfigAPI.getExpected("team_not_found"));
            return;
        }
        team.addEntry(gamePlayer.getPseudo());
        gamePlayer.setTeam(team);
        gamePlayer.getPlayer().sendMessage("§7Vous venez de rejoindre l'équipe : §c" + teamName);
    }

    public void quitTeam(GamePlayer gamePlayer) {
        Team team = getTeamByName(gamePlayer.getTeam().getName());
        if (team == null) {
            ConfigAPI.getExpected("team_not_found");
            return;
        }
        team.removeEntry(gamePlayer.getPseudo());
        gamePlayer.setTeam(null);
        gamePlayer.getPlayer().sendMessage("§7Vous venez de quitter l'équipe : §c" + gamePlayer.getTeam().getName());
    }

    protected void addTeam(String teamName, String prefix, boolean ownInvisibleVisibility) {
        scoreboard.registerNewTeam(teamName);
        Team team = scoreboard.getTeam(teamName);
        teams.add(team);
        team.setPrefix(prefix);
        team.setCanSeeFriendlyInvisibles(ownInvisibleVisibility);
    }

    protected void removeTeam(String teamName) {
        teams.remove(getTeamByName(teamName));
        scoreboard.getTeam(teamName).unregister();
    }

}
