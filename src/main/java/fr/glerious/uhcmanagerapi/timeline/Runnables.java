package fr.glerious.uhcmanagerapi.timeline;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class Runnables implements ConditionalAction {

    private final long delay;

    private final long period;

    public Runnables(long delay, long period) {
        this.delay = delay;
        this.period = period;
    }

    public long getPeriod()
    {
        return period;
    }

    public long getDelay()
    {
        return delay;
    }

    public BukkitRunnable get() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (condition()) {
                    exit();
                    cancel();
                    return;
                }
                action();
            }
        };
    }
}



