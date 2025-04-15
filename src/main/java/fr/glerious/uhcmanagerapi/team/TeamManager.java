package fr.glerious.uhcmanagerapi.team;

import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.utils.Scores;

public class TeamManager extends Scores {

    public TeamManager() {
        addTeam("Team1", "", false);
    }

    @Override
    public void joinTeam(GamePlayer gamePlayer, String teamName) {
        super.joinTeam(gamePlayer, teamName);
        gamePlayer.getPlayer().sendMessage("§7Vous venez de rejoindre l'équipe : §c" + teamName);
    }

    @Override
    public void quitTeam(GamePlayer gamePlayer, String teamName) {
        super.quitTeam(gamePlayer, teamName);
        gamePlayer.getPlayer().sendMessage("§7Vous venez de quitter l'équipe : §c" + teamName);

    }
}
