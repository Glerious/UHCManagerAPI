package fr.glerious.uhcmanagerapi.timeline;

import fr.glerious.uhcmanagerapi.Main;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class GameState implements Listener {

    public GameState(String name) {
        this.name = name;
    }

    private final String name;

    private final Timer timer = new Timer();

    public static List<Events> events = new ArrayList<>();

    public static List<Runnables> runnables = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Timer getTimer()
    {
        return timer;
    }

    public List<Events> getEvents()
    {
        return events;
    }

    public List<Runnables> getRunnables()
    {
        return runnables;
    }

    public void next() {
        GameState actual = Main.getGameState();
        HandlerList.unregisterAll(actual);
    }

    public void clock() {
        for (Runnables runnable :
                getRunnables()) {
            runnable.get().runTaskTimer(Main.getMain(), runnable.getDelay(), runnable.getPeriod());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Events events: getEvents()) {
                    if (events.getTime() == timer.getTime()) events.action();
                }
                timer.increment();
            }
        };
    }
}
