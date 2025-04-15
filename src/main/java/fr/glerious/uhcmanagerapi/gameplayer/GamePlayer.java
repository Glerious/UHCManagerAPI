package fr.glerious.uhcmanagerapi.gameplayer;

import fr.glerious.uhcmanagerapi.limitation.EnchantmentLimitation;
import fr.glerious.uhcmanagerapi.limitation.MiningLimitation;
import fr.glerious.uhcmanagerapi.limitation.StuffLimitation;
import fr.glerious.uhcmanagerapi.permission.Grade;
import fr.glerious.uhcmanagerapi.utils.Methods;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;

    private Grade grade = Grade.PLAYER;

    private final SideBar sideBar;

    private boolean isDead = false;

    private int kill;

    private Team gameTeam;

    private MiningLimitation miningLimitation = new MiningLimitation();

    private StuffLimitation stuffLimitation = new StuffLimitation();

    private EnchantmentLimitation enchantmentLimitation = new EnchantmentLimitation();

    public GamePlayer(UUID uuid)
    {
        this.uuid = uuid;
        this.sideBar = new SideBar(this);
        this.sideBar.showScoreboard();
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(uuid);
    }

    public Grade getGrade()
    {
        return grade;
    }

    public void setGrade(Grade grade)
    {
        this.grade = grade;
    }

    public SideBar getSideBar()
    {
        return sideBar;
    }

    public boolean isDead()
    {
        return isDead;
    }

    public void setDead(boolean dead)
    {
        isDead = dead;
    }

    public int getKill()
    {
        return kill;
    }

    public void setKill(int kill)
    {
        this.kill = kill;
    }

    public Team getGameTeam()
    {
        return gameTeam;
    }

    public void setGameTeam(Team gameTeam)
    {
        this.gameTeam = gameTeam;
    }

    public void setTeam(Team team)
    {
        this.gameTeam = team;
    }

    public MiningLimitation getMiningLimitation() {
        return miningLimitation;
    }

    public void setMiningLimitation(MiningLimitation miningLimitation) {
        this.miningLimitation = miningLimitation;
    }

    public StuffLimitation getStuffLimitation() {
        return stuffLimitation;
    }

    public void setStuffLimitation(StuffLimitation stuffLimitation) {
        this.stuffLimitation = stuffLimitation;
    }

    public EnchantmentLimitation getEnchantmentLimitation() {
        return enchantmentLimitation;
    }

    public void setEnchantmentLimitation(EnchantmentLimitation enchantmentLimitation) {
        this.enchantmentLimitation = enchantmentLimitation;
    }

    //////////////////////////

    public String getPseudo()
    {
        return getPlayer().getName();
    }

    public boolean isHost()
    {
        return getGrade().equals(Grade.HOST);
    }

    public boolean hasGarde(Grade grade) {
        return getGrade().equals(grade);
    }

    public void increaseKill()
    {
        this.kill++;
    }

    public void decreaseKill()
    {
        this.kill--;
    }

    public void initialiseStats()
    {
        if (!isDead) setKill(0);
    }

    public void sendActionBar(String message) {
        Player player = getPlayer();
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ChatComponentText actionBarText = new ChatComponentText(message);
        PacketPlayOutChat actionBarPacket = new PacketPlayOutChat(actionBarText, (byte) 2);
        craftPlayer.getHandle().playerConnection.sendPacket(actionBarPacket);
    }

    public Boolean hasItem(ItemStack item)
    {
        return getPlayer().getInventory().contains(item);
    }

    public boolean isFull() {
        ItemStack[] content = getPlayer().getInventory().getContents();
        for (ItemStack item: content)
            if (item == null) return false;
        return true;
    }

    public void giveItem(ItemStack item) {
        if (isFull()) {
            dropItem(item);
            return;
        }
        getPlayer().getInventory().addItem(item);
        getPlayer().updateInventory();
    }

    public void giveItems(List<ItemStack> items) {
        for (ItemStack i: items) giveItem(i);
    }

    public void dropItem(ItemStack item) {
        if (hasItem(item))
            getPlayer().getInventory().remove(item);
        getPlayer().getWorld().dropItem(getPlayer().getLocation(), item);
    }

    public void dropItem(Material material)
    {
        dropItem(new ItemStack(material));
    }

    public void revive() {
        if (gameTeam != null)
            getPlayer().setGameMode(GameMode.SURVIVAL);
            //TODO réutiliser la méthode de téléportation random (créer cette méthode dans Teleport.java
    }

    public void information() {
        getPlayer().sendMessage(
                Methods.stylized(
                "&8&lPseudo : &7" + getPseudo() + "\n"
                + "&8&lGrade : &7" + grade.name() + "\n"
                + "&8&lTeam : &7" + ((gameTeam != null) ? gameTeam.getPrefix() : "Aucune")
                + "&8&lKill : " + kill +  "\n"
                + "&8&lDeath : " + isDead +  "\n"
                )
        );
    }
}
