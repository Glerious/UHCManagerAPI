package fr.glerious.uhcmanagerapi.team;

import com.mojang.authlib.GameProfile;
import fr.glerious.uhcmanagerapi.ConfigUHC;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    private final List<Team> teams = new ArrayList<>();

    private final Team spectatorTeam;

    private Integer maximumTeamSlot = ConfigUHC.getConstants("maximum_team_slot");

    private final String spectatorsName = "Spectators";

    private final String spectatorsPrefix = "§o";

    public TeamManager() {
        scoreboard.registerNewTeam(spectatorsName);
        this.spectatorTeam = scoreboard.getTeam(spectatorsName);
        this.spectatorTeam.setPrefix(spectatorsPrefix);
        this.spectatorTeam.setCanSeeFriendlyInvisibles(true);
        addTeam("Team1", "§6TEAM1 ", false);
        //addTeam("Team2", "§7TEAM2 ", false);
        //addTeam("Team3", "§1", false);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Integer getMaximumTeamSlot() {
        return maximumTeamSlot;
    }

    public void setMaximumTeamSlot(Integer maximumTeamSlot) {
        this.maximumTeamSlot = maximumTeamSlot;
    }

    public String getSpectatorsName() {
        return spectatorsName;
    }

    public Integer getGameSize() {
        return maximumTeamSlot*teams.size();
    }

    public Integer getActualSize() {
        int returned = 0;
        for (Team team : teams) returned += team.getSize();
        return returned;
    }

    public List<GamePlayer> getActualGamePlayers() {
        List<GamePlayer> returned = new ArrayList<>();
        for (Team team : teams) {
            for (String entry : team.getEntries()) {
                Player player = Bukkit.getPlayer(entry);
                returned.add(Main.getGamePlayer(player.getUniqueId()));
            }
        }
        return returned;
    }

    public Team getTeamByName(String teamName, boolean prefixed) {
        if (prefixed)
            if ((spectatorsPrefix + spectatorsName).equals(teamName)) return spectatorTeam;
        if (spectatorsName.equals(teamName)) return spectatorTeam;
        for (Team team : getTeams()) {
            if (prefixed)
                if ((team.getPrefix() + team.getName()).equals(teamName)) return team;
            if (team.getName().equals(teamName)) return team;
        }
        return null;
    }

    public Team getSpectatorTeam() {
        return spectatorTeam;
    }

    public void joinTeam(GamePlayer gamePlayer, String teamName) {
        Team team = getTeamByName(teamName, false);
        if (team == null) {
            gamePlayer.getPlayer().sendMessage(ConfigUHC.getExpected("team_not_found"));
            return;
        }
        team.addEntry(gamePlayer.getPseudo());
        gamePlayer.setTeam(team);
        String displayName = team.getPrefix() + gamePlayer.getPlayer().getName();
        changeDisplayName(gamePlayer.getPlayer(), displayName);
        gamePlayer.getSideBar().updateTeam();
        gamePlayer.getPlayer().sendMessage("§7Vous venez de rejoindre l'équipe : §c" + teamName);
    }

    public void quitTeam(GamePlayer gamePlayer) {
        Team team = getTeamByName(gamePlayer.getTeam().getName(), false);
        if (team == null) {
            ConfigUHC.getExpected("team_not_found");
            return;
        }
        team.removeEntry(gamePlayer.getPseudo());
        String teamName = team.getName();
        gamePlayer.setTeam(null);
        gamePlayer.getSideBar().updateTeam();
        gamePlayer.getPlayer().sendMessage("§7Vous venez de quitter l'équipe : §c" + teamName);
    }

    protected void addTeam(String teamName, String prefix, boolean ownInvisibleVisibility) {
        scoreboard.registerNewTeam(teamName);
        Team team = scoreboard.getTeam(teamName);
        team.setPrefix(prefix);
        team.setCanSeeFriendlyInvisibles(ownInvisibleVisibility);
        teams.add(team);
    }

    public void removeTeam(String teamName) {
        teams.remove(getTeamByName(teamName, false));
        scoreboard.getTeam(teamName).unregister();
    }

    public void changeDisplayName(Player player, String newName) {
        player.setDisplayName(newName);
        player.setPlayerListName(newName);
        changeName(player, newName);
    }

    public void changeName(Player p, String newName){
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl == p) continue;
            //REMOVES THE PLAYER
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)p).getHandle()));
            //CHANGES THE PLAYER'S GAME PROFILE
            GameProfile gp = ((CraftPlayer)p).getProfile();
            try {
                Field nameField = GameProfile.class.getDeclaredField("name");
                nameField.setAccessible(true);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

                nameField.set(gp, newName);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                throw new IllegalStateException(ex);
            }
            //ADDS THE PLAYER
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)p).getHandle()));
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(p.getEntityId()));
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));
        }
    }
}
