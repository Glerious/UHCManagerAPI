package fr.glerious.uhcmanagerapi.utils;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class Scores {

    protected final Scoreboard scoreboard;

    protected final List<Team> teams = new ArrayList<>();

    public Scores() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
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
            ConfigAPI.getExpected("team_not_found");
            return;
        }
        team.addEntry(gamePlayer.getPseudo());
        gamePlayer.setTeam(team);
    }

    public void quitTeam(GamePlayer gamePlayer, String teamName) {
        Team team = getTeamByName(teamName);
        if (team == null) {
            ConfigAPI.getExpected("team_not_found");
            return;
        }
        team.removeEntry(gamePlayer.getPseudo());
        gamePlayer.setTeam(null);
        gamePlayer.getPlayer().sendMessage("§7Vous venez de quitter l'équipe : §c" + team.getName());
    }

    protected void addTeam(String teamName, String prefix, boolean ownInvisibleVisibility) {
        scoreboard.registerNewTeam(teamName);
        Team team = scoreboard.getTeam(teamName);
        teams.add(team);
        team.setPrefix(prefix);
        team.setCanSeeFriendlyInvisibles(ownInvisibleVisibility);
    }

    protected void removeTeam(String teamName) {
        scoreboard.getTeam(teamName).unregister();
    }
}
