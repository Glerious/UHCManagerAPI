package fr.glerious.uhcmanagerapi.utils;

import fr.glerious.uhcmanagerapi.gameplayer.BetterItems;
import fr.glerious.uhcmanagerapi.utils.menu.Page;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;

public class Menu {

    public Menu(String name) {
        this.name = name;
        pages.put("0", blankPage);
    }

    protected final String name;

    protected final Page blankPage = new Page("Nothing", 1,
            Collections.singletonList(0),
            Collections.singletonList(new BetterItems(Material.AIR)));

    protected Page actualPage = blankPage;

    protected final HashMap<String, Page> pages = new HashMap<>();

    public void openInventory(Player player) {
        actualPage.plot();
        player.updateInventory();
    }

    protected void changePage(String id) {
        actualPage = pages.get(id);
    }
}
