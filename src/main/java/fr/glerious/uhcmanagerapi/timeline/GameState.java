package fr.glerious.uhcmanagerapi.timeline;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Waiting;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class GameState implements Listener {

    public GameState(String name) {
        this.name = name;
    }

    protected final String name;

    protected final Timer timer = new Timer();

    protected List<Events> events = new ArrayList<>();

    protected List<Runnables> runnables = new ArrayList<>();

    protected BukkitRunnable eventRunnable = new BukkitRunnable() {
        @Override
        public void run() {
            for (Events events: getEvents())
                if (events.getTime() == timer.getTime()) events.action();
            timer.increment();
            Main.getGamePlayers().forEach(gamePlayer -> gamePlayer.getSideBar().updateTimer());
        }
    };

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

    public BukkitRunnable getEventRunnable() {
        return eventRunnable;
    }

    public void next() {
        HandlerList.unregisterAll(this);
        if (!(this instanceof Waiting)) eventRunnable.cancel();
    }

    public void clock() {
        for (Runnables runnable :
                getRunnables())
            runnable.get().runTaskTimer(Main.getMain(), runnable.getDelay(), runnable.getPeriod());
        eventRunnable.runTaskTimer(Main.getMain(), 0, 20);
    }
}
