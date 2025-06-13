package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnables;
import fr.glerious.uhcmanagerapi.utils.Methods;

public class Starting extends GameState {

    public Starting(boolean onTest) {
        super("Démmarage");
        runnables.add(new Runnable(0, onTest ? 0 : Methods.seconds2ticks(1)) {
            private final Integer duration = 10;

            @Override
            public boolean condition() {
                return timer.getTime().equals(duration);
            }

            @Override
            public void exit() {
                Main.getGameState().next();
            }

            @Override
            public void action() {
                Main.getGamePlayers().forEach(gamePlayer -> gamePlayer.sendActionBar(
                                "§7Lancement dans§c " + (duration - getTimer().getTime()) + " §7secondes."));
            }
        });
        clock();
    }

    @Override
    public void next() {
        super.next();
        Main.setGameState(new Teleporting());
    }
}
