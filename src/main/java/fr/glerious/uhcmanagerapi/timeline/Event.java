package fr.glerious.uhcmanagerapi.timeline;

public abstract class Event implements ConditionalAction {

    private final int elapsed;

    public Event(int elapsedTick) {
        this.elapsed = elapsedTick;
    }

    public int getTime() {
        return elapsed;
    }
}
