package fr.glerious.uhcmanagerapi.timeline;

public abstract class Events implements ConditionalAction {

    private final int elapsed;

    public Events(int elapsedTick) {
        this.elapsed = elapsedTick;
    }

    public int getTime() {
        return elapsed;
    }

    @Override
    public boolean condition() {
        return false;
    }

    @Override
    public void exit() {

    }
}
