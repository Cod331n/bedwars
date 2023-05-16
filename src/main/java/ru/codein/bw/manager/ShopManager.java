package ru.codein.bw.manager;

import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopManager implements IManager {
    public void spawnNPS(Location location) {
        Villager villager = location.getWorld().spawn(location, Villager.class);
        villager.setCustomName("Магазин");
        villager.setCustomNameVisible(true);
        villager.setCanPickupItems(false);
        villager.setNoDamageTicks(Integer.MAX_VALUE);
        ((CraftVillager) villager).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0);
    }

    public Inventory getShopMenu() {
        Inventory inventory = Bukkit.createInventory(null, 9, "Магазин");

        ItemStack blocks = new ItemStack(Material.WOOL, 1);
        List<String> blocksLore = new ArrayList<>();
        blocksLore.add("");
        blocksLore.add(ChatColor.WHITE + "Купить 4 блока шерсти");
        blocksLore.add(ChatColor.GOLD + "Стоимость: 2 железа");
        blocks.setItemMeta(setItemDisplayName(blocks, ChatColor.GRAY + "Блок шерсти"));
        blocks.setItemMeta(setItemLore(blocks, blocksLore));
        inventory.setItem(0, blocks);

        ItemStack shears = new ItemStack(Material.SHEARS, 1);
        List<String> shearsLore = new ArrayList<>();
        shearsLore.add("");
        shearsLore.add(ChatColor.WHITE + "Купить ножницы");
        shearsLore.add(ChatColor.GOLD + "Стоимость: 4 золота");
        shears.setItemMeta(setItemDisplayName(shears, ChatColor.GRAY + "Ножницы"));
        shears.setItemMeta(setItemLore(shears, shearsLore));
        inventory.setItem(1, shears);

        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        List<String> swordLore = new ArrayList<>();
        swordLore.add("");
        swordLore.add(ChatColor.WHITE + "Купить меч");
        swordLore.add(ChatColor.GOLD + "Стоимость: 6 золота");
        sword.setItemMeta(setItemDisplayName(sword, ChatColor.GRAY + "Меч"));
        sword.setItemMeta(setItemLore(sword, swordLore));
        inventory.setItem(2, sword);

        return inventory;
    }

    private static ItemMeta setItemDisplayName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        return meta;
    }

    private static ItemMeta setItemLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        return meta;
    }
}
