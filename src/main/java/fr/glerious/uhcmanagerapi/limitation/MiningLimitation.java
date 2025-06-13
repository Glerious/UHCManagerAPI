package fr.glerious.uhcmanagerapi.limitation;

import fr.glerious.uhcmanagerapi.ConfigUHC;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MiningLimitation implements Listener {

    private int diamondMined = 0;

    private final int diamondLimit = ConfigUHC.getConstants("diamond_limit");

    private final Material diamondReplacement = Material.GOLD_INGOT;

    private final int numberReplacement = ConfigUHC.getConstants("number_replacement");


    @EventHandler
    public void onOreMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() == Material.DIAMOND_ORE) {
            if (diamondMined != diamondLimit) {
                diamondMined++;
                GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
                if (gamePlayer == null)  return;
                gamePlayer.sendActionBar("(§c" + diamondMined + "§7/§c" + diamondLimit + "§7)");
                return;
            }
            event.setCancelled(true);
            player.sendMessage(ConfigUHC.getExpected("diamond_limit"));
            block.setType(Material.AIR);
            player.giveExp(7);
            block.getWorld().dropItem(block.getLocation(), new ItemStack(diamondReplacement, numberReplacement));
        }
    }
}