package fr.glerious.uhcmanagerapi.utils;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.permission.Grade;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigAPI {

    public static String getToConfig(String string) {
        return Methods.stylized(
                Main.getMain().getConfig().getString(string));
    }

    public static String getExpected(String string) {
        return "§cCommande Impossible :§7 " + getToConfig("expected." + string);
    }

    public static String getConstructor(String string) {
        return "§cConstructeur :§7 " + getToConfig("constructor." + string);
    }

    public static String getCommandFeedback(String string)
    {
        return getToConfig("commands_feedback." + string);
    }

    public static String getInformation(String string)
    {
        return getToConfig("information." + string);
    }

    public static String getPrefix(String string)
    {
        return getToConfig("prefix." + string);
    }

    public static String getLimitation(String string)
    {
        return getToConfig("limitation." + string);
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
        if (Bukkit.getPlayer(entry) == null) return getExpected("player_require");
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

    public static boolean printCommandFeedback(List<String> exception, GamePlayer gamePlayer) {
        String expected = "";
        for (String text :
                exception) {
            if (text.isEmpty()) continue;
            expected = text;
            break;
        }
        if (expected.isEmpty()) return false;
        gamePlayer.getPlayer().sendMessage(getExpected(expected));
        return true;
    }
}
