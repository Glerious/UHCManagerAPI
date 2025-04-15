package fr.glerious.uhcmanagerapi.permission;

import fr.glerious.uhcmanagerapi.utils.Scores;

public class GradeManager extends Scores {

    public GradeManager() {
        addTeam("Players", "", false);
        addTeam("Spectators", "§7§o", true);
    }
}
