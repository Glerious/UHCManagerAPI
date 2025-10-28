package fr.glerious.uhcmanagerapi.utils;

import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ConfigAPI {

    public static String getToConfig(String string, JavaPlugin javaPlugin) {
        return Methods.stylized(
                javaPlugin.getConfig().getString(string));
    }

    protected static String getFromPath(String message, String path, String last, JavaPlugin javaPlugin) {
        if (message.equalsIgnoreCase("")) return getToConfig(path + "." + last, javaPlugin);
        return "§c" + message + ":§7 " + getToConfig(path + "." + last, javaPlugin);
    }

    public static String getName(JavaPlugin javaPlugin) {
        return getToConfig("name",javaPlugin);
    }

    public static String getCreator(JavaPlugin javaPlugin) {
        return getToConfig("creator", javaPlugin);
    }
}
