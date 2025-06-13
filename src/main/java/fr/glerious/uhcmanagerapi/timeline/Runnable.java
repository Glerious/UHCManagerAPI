package fr.glerious.uhcmanagerapi.timeline;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class Runnable implements ConditionalAction {

    private final long delay;

    private final long period;

    public Runnable(long delayTick, long periodTick) {
        this.delay = delayTick;
        this.period = periodTick;
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



