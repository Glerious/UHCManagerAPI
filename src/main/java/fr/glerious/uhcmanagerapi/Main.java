package fr.glerious.uhcmanagerapi;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.limitation.StuffLimitation;
import fr.glerious.uhcmanagerapi.limitation.UHC;
import fr.glerious.javautils.Grade;
import fr.glerious.uhcmanagerapi.permission.HostMenu;
import fr.glerious.uhcmanagerapi.team.MenuTeam;
import fr.glerious.uhcmanagerapi.team.TeamManager;
import fr.glerious.uhcmanagerapi.timeline.Event;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnable;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Waiting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {

    private static Main main;

    private static Commands commands;

    private static TeamManager teamManager;

    private static GameState gameState;

    private static final List<Event> events = new ArrayList<>();

    private static final List<Event> startEvents = new ArrayList<>();

    private static final List<Runnable> runnables = new ArrayList<>();

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

    public static List<Event> getEvents()
    {
        return events;
    }

    public static List<Event> getStartEvents() {
        return startEvents;
    }

    public static List<Runnable> getRunnables()
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
        gameState = new Waiting();

        Bukkit.getScheduler().runTask(this, () -> {
            commands = new Commands();
            teamManager = new TeamManager();

            addListener(new JoinAndQuitListener());
            addListener(new MenuTeam());
            addListener(new UHC());
            addListener(new HostMenu());
            addListener(new StuffLimitation());
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
            player.kickPlayer(ConfigUHC.getInformation("kick_for_restarting"));
        }
    }

    private static String hasNotNumberArgumentRequire(int arguments, int argumentsRequire) {
        if (arguments < argumentsRequire) return  "arg_require";
        return "";
    }

    private static String hasNotGradeRequire(GamePlayer gamePlayer, Grade grade) {
        if (!gamePlayer.hasGarde(grade)) return "grade_require";
        return "";
    }

    private static String hasNotGameStateRequire(Class<? extends GameState> gameStateRequire) {
        if (gameStateRequire != Main.getGameState().getClass()) return "game_state_require";
        return "";
    }

    private static String isNotPlayerOnline(String entry) {
        if (Bukkit.getPlayer(entry) == null) return "player_require";
        return "";
    }

    public static boolean commandChecker(GamePlayer gamePlayer,
                                         Grade gradeRequire,
                                         Class<? extends GameState> gameStateRequire,
                                         int arguments,
                                         int argumentsRequire,
                                         String otherPlayer) {
        return printCommandFeedback(Arrays.asList(
                hasNotGameStateRequire(gameStateRequire),
                hasNotNumberArgumentRequire(arguments, argumentsRequire),
                hasNotGradeRequire(gamePlayer, gradeRequire),
                isNotPlayerOnline(otherPlayer)
        ), gamePlayer);
    }

    public static boolean commandChecker(GamePlayer gamePlayer,
                                         Grade gradeRequire,
                                         int arguments,
                                         int argumentsRequire,
                                         String otherPlayer) {
        return printCommandFeedback(Arrays.asList(
                hasNotNumberArgumentRequire(arguments, argumentsRequire),
                hasNotGradeRequire(gamePlayer, gradeRequire),
                isNotPlayerOnline(otherPlayer)
        ), gamePlayer);
    }

    public static boolean commandChecker(GamePlayer gamePlayer,
                                         Grade gradeRequire,
                                         int arguments,
                                         int argumentsRequire) {
        return printCommandFeedback(Arrays.asList(
                hasNotNumberArgumentRequire(arguments, argumentsRequire),
                hasNotGradeRequire(gamePlayer, gradeRequire)
        ), gamePlayer);
    }

    public static boolean commandChecker(GamePlayer gamePlayer,
                                         Grade gradeRequire,
                                         Class<? extends GameState> gameStateRequire,
                                         int arguments,
                                         int argumentsRequire) {
        return printCommandFeedback(Arrays.asList(
                hasNotGameStateRequire(gameStateRequire),
                hasNotNumberArgumentRequire(arguments, argumentsRequire),
                hasNotGradeRequire(gamePlayer, gradeRequire)
        ), gamePlayer);
    }

    public static boolean commandChecker(GamePlayer gamePlayer,
                                         Class<? extends GameState> gameStateRequire) {
        return printCommandFeedback(Collections.singletonList(
                hasNotGameStateRequire(gameStateRequire)
        ), gamePlayer);
    }

    private static boolean printCommandFeedback(List<String> exception, GamePlayer gamePlayer) {
        String expected = "";
        for (String text :
                exception) {
            if (text.isEmpty()) continue;
            expected = text;
            break;
        }
        if (expected.isEmpty()) return false;
        gamePlayer.getPlayer().sendMessage(ConfigUHC.getExpected(expected));
        return true;
    }
}
