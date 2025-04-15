package fr.glerious.uhcmanagerapi.timeline;

public interface ConditionalAction {

    boolean condition();

    void exit();

    void action();

}
