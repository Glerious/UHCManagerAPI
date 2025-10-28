package fr.glerious.uhcmanagerapi;

import fr.glerious.uhcmanagerapi.utils.ConfigAPI;

public class ConfigUHC extends ConfigAPI {

    public static String getExpected(String string) {
        return getFromPath("Commande Impossible","expected", string, Main.getMain());
    }

    public static String getConstructor(String string) {
        return getFromPath("Constructeur", "constructor", string, Main.getMain());
    }

    public static String getCommandsFeedback(String string) {
        return getFromPath("", "commands_feedback", string, Main.getMain());
    }

    public static String getInformation(String string) {
        return getFromPath("", "information", string, Main.getMain());
    }

    public static int getConstants(String string) {
        return Integer.parseInt(getFromPath("", "constants", string, Main.getMain()));
    }
}
