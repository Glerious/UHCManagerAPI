package fr.glerious.uhcmanagerapi.gameplayer;

import fr.glerious.uhcmanagerapi.ConfigUHC;
import fr.glerious.uhcmanagerapi.Main;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class SideBar {

    private final GamePlayer gamePlayer;

    private final Scoreboard scoreboard;

    private final Objective objective;

    private final HashMap<Integer, String> lines = new HashMap<>();

    private final int indent = 5;

    public SideBar(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective(gamePlayer.getPseudo(), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        changeNames("§7" + ConfigUHC.getName(Main.getMain()));
        setScore("§7» §a§o§lBy @" + ConfigUHC.getCreator(Main.getMain()), 0);
        setScore("§1" + ConfigUHC.getInformation("sidebar_separator"), 1);
        updateTeam();
        updatePlayerCounter();
        setScore("§2" + ConfigUHC.getInformation("sidebar_separator"), 4);
        setScore("§3" + ConfigUHC.getInformation("sidebar_separator"), 12);
        updateTimer();
        updateEtat();
        setScore("§4" + ConfigUHC.getInformation("sidebar_separator"), 15);
    }

    public GamePlayer getGamePlayer()
    {
        return gamePlayer;
    }

    public Scoreboard getScoreboard()
    {
        return scoreboard;
    }

    public void showScoreboard()
    {
        gamePlayer.getPlayer().setScoreboard(scoreboard);
    }

    public void changeNames(String name) {
        objective.setDisplayName(name);
    }

    private void setScore(String content, int score) {
        Score line = objective.getScore(content);
        line.setScore(score);
        lines.put(score, content);
    }

    public void setMoreLine(int lineIndex, String content)
    {
        setScore(content, lineIndex + indent);
    }

    private void updateLine(String content, int score) {
        String reset = lines.get(score);
        if (reset != null) getScoreboard().resetScores(reset);
        setScore(content, score);
    }

    public void updateMoreLine(String newLine, int lineIndex)
    {
        updateLine(newLine, lineIndex + indent);
    }

    public void updateTeam() {
        int score = 2;
        String text = "§7» §6Team: §7";
        String updated = (gamePlayer.getTeam() != null) ? gamePlayer.getTeam().getName() : "Aucun";
        updateLine(text + updated, score);
    }

    public void updatePlayerCounter() {
        int numberPlayer = Main.getGamePlayers().size();
        for (GamePlayer gamePlayer : Main.getGamePlayers()) {
            if (gamePlayer.isDead()) numberPlayer--;
        }
        int score = 3;
        String text = "§7» §6Joueurs: §7";
        String updated = String.valueOf(numberPlayer + 1);
        updateLine(text + updated, score);
    }

    public void updateTimer() {
        int score = 13;
        String text = "§7» §6Timer: §7";
        String updated = Main.getGameState().getTimer().asClock();
        updateLine(text + updated, score);
    }

    public void updateEtat() {
        int score = 14;
        String text = "§7» §6Etat: §7";
        String updated = Main.getGameState().getName();
        updateLine(text + updated, score);
    }
}
