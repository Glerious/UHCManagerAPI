package fr.glerious.uhcmanagerapi.timeline.gamestates;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.timeline.Runnable;
import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (Methods.isOneOf(action, Action.LEFT_CLICK_BLOCK, Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            player.updateInventory();
        }
    }

    @Override
    public void next() {
        super.next();
        Main.setGameState(new Teleporting());
    }
}
