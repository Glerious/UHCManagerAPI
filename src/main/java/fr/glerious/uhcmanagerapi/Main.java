package fr.glerious.uhcmanagerapi;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.limitation.UHC;
import fr.glerious.uhcmanagerapi.permission.HostMenu;
import fr.glerious.uhcmanagerapi.team.MenuTeam;
import fr.glerious.uhcmanagerapi.team.TeamManager;
import fr.glerious.uhcmanagerapi.timeline.Events;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnables;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Waiting;
import fr.glerious.uhcmanagerapi.utils.ConfigAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main main;

    private static Commands commands;

    private static TeamManager teamManager;

    private static GameState gameState;

    private static final List<Events> events = new ArrayList<>();

    private static final List<Runnables> runnables = new ArrayList<>();

    private static final List<GamePlayer> gamePlayers = new ArrayList<>();

    public static void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
    }

    public static Main getMain()
    {
        return main;
    }

    public static TeamManager getTeamManager() {
        return teamManager;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        Main.gameState = gameState;
        Main.getMain().addListener(gameState);
        Main.getGamePlayers().forEach(gamePlayer -> {
            gamePlayer.getSideBar().updateEtat();
            gamePlayer.getSideBar().updateTimer();
        });
    }

    public static List<Events> getEvents()
    {
        return events;
    }

    public static List<Runnables> getRunnables()
    {
        return runnables;
    }

    public static List<GamePlayer> getGamePlayers()
    {
        return gamePlayers;
    }

    @Override
    public void onEnable() {
        main = this;
        saveDefaultConfig();

        getCommand("revive").setExecutor(new Commands());
        getCommand("uhc").setExecutor(new Commands());
        getCommand("info").setExecutor(new Commands());

        Bukkit.getScheduler().runTask(this, () -> {
            commands = new Commands();
            teamManager = new TeamManager();
            gameState = new Waiting();

            addListener(new JoinAndQuitListener());
            addListener(new MenuTeam());
            addListener(new UHC());
            addListener(new HostMenu());
            addListener(gameState);
        });
    }

    @Override
    public void onDisable() {
        kickAllPlayer();
    }

    public static GamePlayer getGamePlayer(UUID uuid) {
        for (GamePlayer gamePlayer: gamePlayers)
            if (gamePlayer.getUuid().equals(uuid)) return gamePlayer;
        return null;
    }

    public static boolean hasGamePlayer(UUID uuid)
    {
        return getGamePlayer(uuid) != null;
    }

    private void addListener(Listener listener) {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(listener, this);
    }

    private void addCommand(String command) {
        getCommand(command).setExecutor(commands);
    }

    public static void kickAllPlayer() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.kickPlayer(ConfigAPI.getInformation("kick_for_restarting"));
        }
    }
}
