package fr.glerious.uhcmanagerapi.timeline.gamestates;

import com.google.common.collect.Lists;
import fr.glerious.uhcmanagerapi.Main;
import fr.glerious.uhcmanagerapi.gameplayer.BetterItems;
import fr.glerious.uhcmanagerapi.gameplayer.GamePlayer;
import fr.glerious.uhcmanagerapi.permission.Grade;
import fr.glerious.uhcmanagerapi.permission.HostMenu;
import fr.glerious.uhcmanagerapi.team.MenuTeam;
import fr.glerious.uhcmanagerapi.timeline.GameState;
import fr.glerious.uhcmanagerapi.utils.Methods;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Waiting extends GameState implements Listener {

    public Waiting() {
        super("En Attente");
        betterItems.add(new BetterItems(Material.COMPASS, Methods.stylized("&6Team")));
        betterItems.add(new BetterItems(Material.NETHER_STAR, Methods.stylized("&6Menu Host"), Grade.HOST));
    }

    private final List<BetterItems> betterItems = new ArrayList<>();

    private List<ItemStack> getBetterItemsToItemsStack() {
        return Lists.transform(betterItems, BetterItems::getItemStack);
    }

    private HashMap<Integer, ItemStack> placeInMainBar(List<ItemStack> itemStacks) {
        int size = itemStacks.size();
        List<Integer> places = new ArrayList<>();
        if (Methods.isOneOf(size, 5, 6, 7, 8, 9)) {
            places.add(0);
            places.add(8);
        }
        if (Methods.isOneOf(size, 4, 6, 7, 8, 9)) {
            places.add(1);
            places.add(7);
        }
        if (Methods.isOneOf(size, 3, 5, 7, 8, 9)) {
            places.add(2);
            places.add(6);
        }
        if (Methods.isOneOf(size, 2, 4, 6, 8, 9)) {
            places.add(3);
            places.add(5);
        }
        if (Methods.isOneOf(size, 1, 3, 5, 7, 8, 9)) places.add(4);
        return Methods.MergeList(places, itemStacks);
    }

    @Override
    public void next() {
        super.next();
        Main.setGameState(new Starting(false));
    }

    @EventHandler
    public void giveItemInMainBar(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Main.getGamePlayer(player.getUniqueId());
        assert gamePlayer != null;
        if (betterItems.isEmpty()) return;
        if (betterItems.size() > 9) {
            betterItems.clear();
            betterItems.add(new BetterItems(Material.BARRIER,
                    Methods.stylized("&c&lTO MANY ITEMS TO GIVE"), true));
        }
        List<ItemStack> providedItems = new ArrayList<>();
        for (BetterItems betterItems : betterItems) {
            ItemStack itemStack = betterItems.usable(gamePlayer.getGrade());
            if (itemStack != null) providedItems.add(itemStack);
        }
        HashMap<Integer, ItemStack> placementOfItem = placeInMainBar(providedItems);
        for (Integer integer : placementOfItem.keySet())
            player.getInventory().setItem(integer, placementOfItem.get(integer));
        player.updateInventory();
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (getBetterItemsToItemsStack().contains(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickInItem(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (getBetterItemsToItemsStack().contains(item) || event.getCursor() != null) {
            if (Methods.isOneOf(event.getClick(), ClickType.LEFT,
                    ClickType.SHIFT_LEFT, ClickType.CONTROL_DROP, ClickType.DROP,
                    ClickType.RIGHT, ClickType.SHIFT_RIGHT))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        event.setCancelled(true);
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

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (Methods.isOneOf(action, Action.LEFT_CLICK_BLOCK,
                Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.RIGHT_CLICK_AIR)) {
            switch (player.getItemInHand().getItemMeta().getDisplayName()) {
                case "§6Team":
                    new MenuTeam().openInventory(player); break;
                case "§6Menu Host":
                    new HostMenu().openInventory(player); break;
            }
        }
    }
}
