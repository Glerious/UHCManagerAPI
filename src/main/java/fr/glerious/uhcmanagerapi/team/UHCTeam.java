package fr.glerious.uhcmanagerapi.team;

import fr.glerious.uhcmanagerapi.ConfigUHC;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class UHCTeam {

    private final Team team;

    private final int size = ConfigUHC.getConstants("size_team");

    UHCTeam(Team team) {
        this.team = team;
    }

    public int getActualSize() {
        return team.getSize();
    }

    public int getMaximumSize() {
        return size;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return team.getName();
    }

    public String getPrefix() {
        return team.getPrefix();
    }

    public List<GamePlayer> getGamePlayers() {
        List<GamePlayer> returned = new ArrayList<>();
        for (String entry : team.getEntries()) {
            Player player = Bukkit.getPlayer(entry);
            GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
            returned.add(gamePlayer);
        }
        return returned;
    }

    public List<String> getEntriesDisplayed() {
        List<String> returned = new ArrayList<>();
        for (String entry : team.getEntries()) {
            String entryDisplay = " - " + getPrefix() + entry;
            returned.add(entryDisplay);
        }
        return returned;
    }




}
