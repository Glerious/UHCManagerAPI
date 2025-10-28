package fr.glerious.uhcmanagerapi.utils;

import fr.glerious.uhcmanagerapi.utils.BetterItems;
import fr.glerious.uhcmanagerapi.utils.Methods;
import fr.glerious.uhcmanagerapi.utils.menu.Page;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class Menu {

    public Menu(String name) {
        this.name = name;
        pages.add(blankPage);
    }

    protected final String name;

    protected final List<Page> pages = new ArrayList<>();

    protected final Page blankPage = new Page(
            UUID.randomUUID(), "", 1,
            new HashMap<Integer, BetterItems>() {{
                put(0, new BetterItems(Material.AIR));
            }});

    protected Page actualPage = blankPage;

    public void openInventory(Player player) {
        actualPage.plot();
        player.openInventory(actualPage.getInventory());
        player.updateInventory();
    }

    protected Page getPage(UUID uuid) {
        return Methods.filter(pages, page -> page.getUuid().equals(uuid));
    }

    protected void modifyBasePage(int lineNumber, HashMap<Integer, BetterItems> slots) {
        Page oldBasePage = getPage(blankPage.getUuid());
        Page newBasePage = new Page(blankPage.getUuid(), name, lineNumber, slots);
        pages.add(pages.indexOf(oldBasePage), newBasePage);
        actualPage = newBasePage;
    }

    protected void changeActualPage(UUID uuid) {
        actualPage = getPage(uuid);
    }

    public Page getActualPage() {
        return actualPage;
    }
}
