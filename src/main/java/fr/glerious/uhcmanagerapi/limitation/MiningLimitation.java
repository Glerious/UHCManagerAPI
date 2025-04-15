package fr.glerious.uhcmanagerapi.limitation;

import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.utils.ConfigAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MiningLimitation implements Listener {

    private int diamondMined = 0;

    private int diamondLimit = getMiningLimitation("diamond_limit");

    private Material diamondReplacement = Material.GOLD_INGOT;

    private int numberReplacement = getMiningLimitation("number_replacement");

    private int cutCleanReplacement = getMiningLimitation("cut_clean_replacement");

    public int getDiamondMined() {
        return diamondMined;
    }

    public void setDiamondMined(int diamondMined) {
        this.diamondMined = diamondMined;
    }

    public int getDiamondLimit() {
        return diamondLimit;
    }

    public void setDiamondLimit(int diamondLimit) {
        this.diamondLimit = diamondLimit;
    }

    public Material getDiamondReplacement() {
        return diamondReplacement;
    }

    public void setDiamondReplacement(Material diamondReplacement) {
        this.diamondReplacement = diamondReplacement;
    }

    public int getNumberReplacement() {
        return numberReplacement;
    }

    public void setNumberReplacement(int numberReplacement) {
        this.numberReplacement = numberReplacement;
    }

    public int getCutCleanReplacement() {
        return cutCleanReplacement;
    }

    public void setCutCleanReplacement(int cutCleanReplacement) {
        this.cutCleanReplacement = cutCleanReplacement;
    }

    public MiningLimitation() {

    }

    public int getMiningLimitation(String string) {
        String value = ConfigAPI.getLimitation("mining." + string);
        return Integer.parseInt(value);
    }

    @EventHandler
    public void onOreMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        if (gamePlayer == null)  return;
        Block block = event.getBlock();
        Material material = event.getBlock().getType();
        int experience = 0;
        switch (material) {
            case DIAMOND_ORE:
                if (diamondMined != diamondLimit) {
                    diamondMined++;
                    gamePlayer.sendActionBar("(§c" + diamondMined + "§7/§c" + diamondLimit + "§7)");
                    return;
                }
                experience = 7;
                material = diamondReplacement;
                break;
            case GOLD_ORE:
                material = Material.GOLD_INGOT;
                experience = 2;
                break;
            case IRON_ORE:
                material = Material.IRON_INGOT;
                experience = 1;
                break;
            default:
                return;
        }
        event.setCancelled(true);
        block.setType(Material.AIR);
        player.giveExp(experience);
        block.getWorld().dropItem(block.getLocation(), new ItemStack(material, numberReplacement));
    }
}