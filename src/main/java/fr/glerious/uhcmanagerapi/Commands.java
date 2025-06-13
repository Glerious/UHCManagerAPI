package fr.glerious.uhcmanagerapi;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.permission.Grade;
import fr.glerious.uhcmanagerapi.permission.HostMenu;
import fr.glerious.uhcmanagerapi.team.MenuTeam;
import fr.glerious.uhcmanagerapi.timeline.gamestates.InGame;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Restarting;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Waiting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        if (gamePlayer == null) return false;
        switch (command.getName().toLowerCase()) {
            case "revive": {
                int argumentRequire = 0;
                if (Main.commandChecker(gamePlayer, Grade.HOST, InGame.class, args.length, argumentRequire, args[0])) return true;
                InGame inGame = (InGame) Main.getGameState();
                Player otherPlayer = Bukkit.getPlayer(args[0]);
                if (otherPlayer == null) return false;
                GamePlayer otherGamePlayer = Main.getGamePlayer(otherPlayer.getUniqueId());
                if (otherGamePlayer == null) return false;
                otherGamePlayer.revive(new Location(otherPlayer.getWorld(), 0, 100, 0));
                //TODO: Fix respawn command
                Bukkit.broadcastMessage("§eRevive§7 de " + otherPlayer.getName());
                return true;
            }
            case "info" : {
                if (args.length == 0) {
                    gamePlayer.information();
                    return true;
                }
                int argumentRequire = 1;
                if (Main.commandChecker(gamePlayer, Grade.HOST, args.length, argumentRequire, args[0])) return true;
                Player otherPlayer = Bukkit.getPlayer(args[0]);
                GamePlayer otherGamePlayer = Main.getGamePlayer(otherPlayer.getUniqueId());
                if (otherGamePlayer == null) return false;
                otherGamePlayer.information();
                return true;
            }
            case "uhc": {
                if (args.length == 0) {
                    player.performCommand("uhc help");
                    return true;
                }
                switch (args[0].toLowerCase()) {
                    case "help": {
                        if (args.length == 1) {
                            player.sendMessage(ConfigUHC.getCommandsFeedback("help"));
                            return true;
                        }
                        int argumentRequire = 2;
                        if (Main.commandChecker(gamePlayer, Grade.HOST, args.length, argumentRequire)) return true;
                        if (args[1].equalsIgnoreCase("perm")) {
                            player.sendMessage(ConfigUHC.getConstructor("perm"));
                            return true;
                        }
                        return false;
                    }
                    case "menu": {
                        if (args.length == 1) new HostMenu().openInventory(player);
                        return true;
                    }
                    case "perm": {
                        if (args.length == 1) {
                            player.sendMessage("§7Niveau de permission : " + gamePlayer.getGrade().name());
                            return true;
                        }
                        int argumentRequire = 3;
                        if (Main.commandChecker(gamePlayer, Grade.HOST, args.length, argumentRequire, args[1])) return true;
                        Player otherPlayer = Bukkit.getPlayer(args[1]);
                        GamePlayer otherGamePlayer = Main.getGamePlayer(otherPlayer.getUniqueId());
                        if (otherGamePlayer == null) return false;
                        HashMap<String, Grade> grades = new HashMap<String, Grade>() {{
                            put("host", Grade.HOST);
                            put("player", Grade.PLAYER);
                        }};
                        for (String string :
                                grades.keySet()) {
                            if (args[2].equalsIgnoreCase(string)) {
                                Grade grade = grades.get(string);
                                otherGamePlayer.setGrade(grade);
                                gamePlayer.getPlayer().sendMessage(
                                        "Vous venez d'attribuer à " + otherGamePlayer.getPseudo() + "le grade : " + grade.name());
                                otherGamePlayer.getPlayer().sendMessage("§7Vous recevez le grade : " + grade.name());
                                return true;
                            }
                        }
                    }
                    case "team": {
                        if (args.length == 1) {
                            if (Main.commandChecker(gamePlayer, Waiting.class)) return true;
                            new MenuTeam().openInventory(player);
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("list")) {
                            Team team = gamePlayer.getTeam();
                            if (team == null) {
                                player.sendMessage(ConfigUHC.getExpected("team_require"));
                                return true;
                            }
                            player.sendMessage("§7Liste de l'équipe :§c " + team.getName());
                            team.getEntries().forEach(entry -> player.sendMessage(team.getPrefix() + " " + entry));
                        }
                        return true;
                    }
                    case "start": {
                        int argumentRequire = 1;
                        if (Main.commandChecker(gamePlayer, Grade.HOST, Waiting.class, args.length, argumentRequire)) return true;
                        for (Event events : Main.getStartEvents()) {
                            if (!events.condition()) return true;
                        }
                        if (!(Main.getTeamManager().getGameSize().equals(Main.getTeamManager().getActualSize()))) {
                            player.sendMessage(ConfigUHC.getExpected("team_require"));
                            return true;
                        }
                        for (GamePlayer otherGamePlayer : Main.getGamePlayers())
                            otherGamePlayer.getPlayer().getInventory().clear();
                        Main.getGameState().next(); //TODO remove testmode
                        return true;
                    }
                    case "stop": {
                        int argumentRequire = 1;
                        if (Main.commandChecker(gamePlayer, Grade.HOST, InGame.class, args.length, argumentRequire)) return true;
                        Main.setGameState(new Restarting());
                        return true;
                    }
                    case "time": {
                        int argumentRequire = 2;
                        if (Main.commandChecker(gamePlayer, Grade.HOST, InGame.class, args.length, argumentRequire)) return true;
                        try {
                            Integer.parseInt(args[1]);
                        }
                        catch (NumberFormatException exception) {
                            player.sendMessage(ConfigUHC.getExpected("type_require"));
                            return true;
                        }
                        Main.getGameState().getTimer().setTime(Integer.parseInt(args[1]));
                        return true;
                    }
                }
            }
            default: return false;
        }
    }
}
