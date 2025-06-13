package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnables;
import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.event.Listener;

public class Restarting extends GameState implements Listener {

    public Restarting() {
        super("Redémmarage");
        runnables.add(new Runnable(Methods.seconds2ticks(10), Methods.seconds2ticks(1)) {
            private final int duration = 5;

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
                for (GamePlayer gamePlayer : Main.getGamePlayers())
                    gamePlayer.sendActionBar("§7Redémmarage dans§c " + (duration - i) + " §7secondes.");
                i++;
            }
        });
        clock();
    }

    @Override
    public void next() {
        super.next();
        Main.setGameState(new Waiting());
    }
}
