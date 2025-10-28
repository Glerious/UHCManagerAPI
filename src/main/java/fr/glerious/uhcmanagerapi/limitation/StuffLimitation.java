package fr.glerious.uhcmanagerapi.limitation;


import fr.glerious.uhcmanagerapi.utils.Methods;
import fr.glerious.uhcmanagerapi.ConfigUHC;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.timeline.gamestates.InGame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class StuffLimitation implements Listener {

    private int diamondPieceLimit = ConfigUHC.getConstants("diamond_piece_limit");

    private boolean hasDiamondSword = true;

    private boolean hasBow = true;

    private static final List<Material> DIAMOND_ARMOR = Arrays.asList(
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET
    );

    private static final List<Material> IRON_ARMOR = Arrays.asList(
            Material.IRON_BOOTS,
            Material.IRON_LEGGINGS,
            Material.IRON_CHESTPLATE,
            Material.IRON_HELMET
    );

    public int getDiamondPieceLimit() {
        return diamondPieceLimit;
    }

    public boolean hasDiamondSword() {
        return hasDiamondSword;
    }

    public boolean hasBow() {
        return hasBow;
    }

    public void setDiamondPieceLimit(int diamondPieceLimit) {
        this.diamondPieceLimit = diamondPieceLimit;
    }

    public void setHasDiamondSword(boolean hasDiamondSword) {
        this.hasDiamondSword = hasDiamondSword;
    }

    public void setHasBow(boolean hasBow) {
        this.hasBow = hasBow;
    }

    public static List<Material> getDiamondArmor() {
        return DIAMOND_ARMOR;
    }

    public static List<Material> getIronArmor() {
        return IRON_ARMOR;
    }


    @EventHandler
    public void onArmorUpdate(InventoryClickEvent event) {
        if (!(Main.getGameState() instanceof InGame)) return;
        Player player = (Player) event.getWhoClicked();
        int diamondPiece = 0;
        if (!DIAMOND_ARMOR.contains(event.getCurrentItem().getType())) return;
        for (Integer slot: Methods.rangedList(36, 39)) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item == null) continue;
            Bukkit.broadcastMessage(item.toString());
            if (DIAMOND_ARMOR.contains(item.getType())) {
                diamondPiece++;
            }
        }
        if (diamondPiece > diamondPieceLimit) {
            event.setCancelled(true);
            player.sendMessage(ConfigUHC.getExpected("diamond_piece_limit"));
        }

    }
        /*
        Check type of player action :
        ShiftClick :
        - In Armor Inventory > pass
        - In MainBar to Inventory (and reverse) > pass
        - In Inventory (or MainBar) to Armor Inventory > have to be checked
        Normal Click :
        - In Armor Inventory > have to be checked
        - Other > pass
         */
    /*
        ItemStack itemStack;
        if (event.isShiftClick()) {
            itemStack = event.getCurrentItem();
            if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                return;
            }
        } else if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
            itemStack = event.getCursor();
        } else {
            return;
        }

     */

        /*
        Check type of itemStack :
        Diamond Equipement :
        - Set piece Limit
        - Set enchant Limit
        Iron Equipment :
        - Set piece Limit to an unreachable number (prevent case of diamond piece limit is 0)
        - Set enchant Limit
        Rest :
        - return
        So, If the item in an iron or a diamond piece, he will be checked
         */
    /*

        int pieceLimit;
        int enchantLimit;
        if (isDiamondArmorPiece(itemStack.getType())) {
            pieceLimit = diamondPieceLimit;
            enchantLimit = protectionDiamondLimit;
        } else if (isIronArmorPiece(itemStack.getType())) {
            pieceLimit = equipment.length;
            enchantLimit = protectionIronLimit;
        } else {
            return;
        }

     */

        /*
        Do the limitation with this condition :
        - The limit of diamond pieces is exceeds > block
        - Equipment of diamond want to be replaced > pass
        - The piece has an illegal enchantment > block
         */
    /*
        if (pieceLimit == getCurrentPiece(player)) {
            int pieceChecked;
            switch (itemStack.getType()) {
                case DIAMOND_LEGGINGS:
                    pieceChecked = 1;
                    break;
                case DIAMOND_CHESTPLATE:
                    pieceChecked = 2;
                    break;
                case DIAMOND_HELMET:
                    pieceChecked = 3;
                    break;
                default:
                    pieceChecked = 0;
            }
            if (event.isShiftClick()) {
                if (equipment[pieceChecked].getType().equals(Material.AIR)) {
                    event.setCancelled(true);
                    getCanceled(CanceledType.ENCHANTMENT_LIMIT, player);
                    return;
                }
                return;
            }
            if (equipment[pieceChecked].getType().equals(itemStack.getType())) {
                return;
            }
            event.setCancelled(true);
            getCanceled(CanceledType.EQUIPMENT_LIMIT, player);
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta.getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL) > enchantLimit || meta.getEnchantLevel(Enchantment.THORNS) > thornsLimit) {
            event.setCancelled(true);
            getCanceled(CanceledType.ENCHANTMENT_LIMIT, player);
        }
    }


    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (Main.getGameState() instanceof Waiting) return;
        ItemStack itemStack = event.getItem().getItemStack();
        Material material = itemStack.getType();
        if (material.equals(Material.DIAMOND_SWORD) && diamondSwordLimit) event.setCancelled(true);
        if (material.equals(Material.BOW) && bowLimit) event.setCancelled(true);
    }

    public int getNumberDiamondPiece(Player player) {
        List<ItemStack> armor = Arrays.asList(player.getInventory().getArmorContents());
        Iterator<ItemStack> iterator = armor.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Material material = iterator.next().getType();
            if (isDiamondPiece(material)) count++;
        }
        return count;
    }

    @EventHandler
    public void onArmorBarEquip(PlayerInteractEvent event) {
        if (Main.getGameState() instanceof Waiting) return;
        Action action = event.getAction();
        if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInHand();
        int pieceLimit;
        int enchantLimit;
        if (isDiamondArmorPiece(itemStack.getType())) {
            pieceLimit = diamondPieceLimit;
            enchantLimit = protectionDiamondLimit;
        } else if (isIronArmorPiece(itemStack.getType())) {
            ItemStack[] equipment = player.getInventory().getArmorContents();
            pieceLimit = equipment.length;
            enchantLimit = protectionIronLimit;
        } else {
            return;
        }
        if (pieceLimit == getCurrentPiece(player)) {
            event.setCancelled(true);
            getCanceled(CanceledType.EQUIPMENT_LIMIT, player);
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL) > enchantLimit || meta.getEnchantLevel(Enchantment.THORNS) > thornsLimit) {
            event.setCancelled(true);
            getCanceled(CanceledType.ENCHANTMENT_LIMIT, player);
        }
    }

    @EventHandler
    public void onInventoryGive(InventoryClickEvent event) {
        if (Main.getGameState() instanceof Waiting) return;
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getTeamManager().getGamePlayer(player.getUniqueId());
        if (gamePlayer == null) {
            return;
        }
        ItemStack itemStack;
        if (event.isShiftClick()) {
            itemStack = event.getCurrentItem();
        } else {
            itemStack = event.getCursor();
        }
        if (!itemStack.getType().equals(Material.DIAMOND_SWORD) || hasDiamondSword) {
            return;
        }
        if (!itemStack.getType().equals(Material.BOW) || hasBow) {
            return;
        }
        event.setCancelled(true);
        gamePlayer.dropItem(itemStack);
        getCanceled(CanceledType.WEAPON_LIMIT, player);
    }


    @EventHandler
    public void moveToMainBar(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        if (gamePlayer == null) {
            return;
        }
        ItemStack itemStack;
        if (event.isShiftClick()) {
            itemStack = event.getCurrentItem();
        } else {
            itemStack = event.getCursor();
        }
    }

     */

}