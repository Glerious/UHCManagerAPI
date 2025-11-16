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

    private final List<UHCTeam> UHCTeams = new ArrayList<>();

    private final Team spectatorTeam;

    private final String spectatorsName = "Spectators";

    private final String spectatorsPrefix = "§o";

    public TeamManager() {
        scoreboard.registerNewTeam(spectatorsName);
        this.spectatorTeam = scoreboard.getTeam(spectatorsName);
        this.spectatorTeam.setPrefix(spectatorsPrefix);
        this.spectatorTeam.setCanSeeFriendlyInvisibles(true);
        addTeam("Team1", "§6", false);
        addTeam("Team2", "§7", false);
    }

    public List<UHCTeam> getUHCteams() {
        return UHCTeams;
    }

    public String getSpectatorsName(){
            return spectatorsName;
    }

    public Integer getGameSize() {
        return ConfigUHC.getConstants("size_team")*UHCTeams.size();
    }

    public Integer getActualNumberInTeam() {
        int returned = 0;
        for (UHCTeam UHCteam : UHCTeams) returned += UHCteam.getActualSize();
        return returned;
    }

    public List<GamePlayer> getActualGamePlayerInTeam() {
        List<GamePlayer> returned = new ArrayList<>();
        for (UHCTeam UHCteam : UHCTeams)
            returned.addAll(UHCteam.getGamePlayers());
        return returned;
    }

    public Team getTeamByName(String teamName, boolean prefixed) {
        if (prefixed)
            if ((spectatorsPrefix + spectatorsName).equals(teamName)) return spectatorTeam;
        if (spectatorsName.equals(teamName)) return spectatorTeam;
        for (UHCTeam UHCteam : UHCTeams) {
            if (prefixed)
                if ((UHCteam.getPrefix() + UHCteam.getName()).equals(teamName)) return UHCteam.getTeam();
            if (UHCteam.getName().equals(teamName)) return UHCteam.getTeam();
        }
        return null;
    }

    public UHCTeam getUHCTeamByName(String teamName, boolean prefixed) {
        for (UHCTeam UHCteam : UHCTeams)
            if (getTeamByName(teamName, prefixed).getName().equals(UHCteam.getName())) return UHCteam;
        return null;
    }

    public Team getSpectatorTeam() {
        return spectatorTeam;
    }

    public void joinTeam(GamePlayer gamePlayer, String teamName) {
        Team team = getTeamByName(teamName, false);
        if (team != spectatorTeam) {
            UHCTeam UHCteam = getUHCTeamByName(teamName, false);
            if (UHCteam.getActualSize() == UHCteam.getMaximumSize()) {
                gamePlayer.getPlayer().sendMessage(ConfigUHC.getExpected("team_full"));
                joinTeam(gamePlayer, spectatorsName);
                return;
            }
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
        UHCTeam UHCteam = new UHCTeam(team);
        UHCTeams.add(UHCteam);
    }

    public void removeTeam(String teamName) {
        teamName = getTeamByName(teamName, false).getName();
        for (UHCTeam UHCteam : UHCTeams)
            if (teamName.equals(UHCteam.getName())) UHCTeams.remove(UHCteam);
        scoreboard.getTeam(teamName).unregister();
    }

    public void changeDisplayName(Player player, String newName) {
        player.setDisplayName(newName);
        player.setPlayerListName(newName);
        //changeName(player, newName);
    }

    public void changeName(Player p, String newName) {
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
