package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnables;
import org.bukkit.event.Listener;

public class Starting extends GameState implements Listener {

    public Starting(boolean onTest) {
        super("Démmarage");
        runnables.add(new Runnables(0, onTest ? 0 : 20) {
            private final int duration = 10;

            private Integer i = 0;

            @Override
            public boolean condition() {
                return i.equals(duration);
            }

            @Override
            public void exit() {
                Main.getGameState().next();
            }

            @Override
            public void action() {
                Main.getGamePlayers().forEach(
                        gamePlayer -> gamePlayer.sendActionBar(
                                "§7Lancement dans§c " + (duration - i) + " §7secondes."
                        )
                ); i++;
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
