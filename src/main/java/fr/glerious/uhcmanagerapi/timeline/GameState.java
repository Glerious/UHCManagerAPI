package fr.glerious.uhcmanagerapi.timeline;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.timeline.gamestates.Waiting;
import fr.glerious.uhcmanagerapi.utils.Methods;
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

    protected List<Event> events = new ArrayList<>();

    protected List<Runnable> runnables = new ArrayList<>();

    protected final BukkitRunnable eventRunnable = new BukkitRunnable() {
        @Override
        public void run() {
            for (Event event: events)
                if (Methods.seconds2ticks(timer.getTime()).equals(event.getTime())) event.action();
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
        timer.reset();
        Main.getGamePlayers().forEach(gamePlayer -> gamePlayer.getSideBar().updateTimer());
    }

    public void clock() {
        for (Runnables runnable :
                getRunnables())
            runnable.get().runTaskTimer(Main.getMain(), runnable.getDelay(), runnable.getPeriod());
        eventRunnable.runTaskTimer(Main.getMain(), 0, 20);
    }
}
