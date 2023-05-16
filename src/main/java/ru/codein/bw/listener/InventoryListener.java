package ru.codein.bw.listener;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.codein.bw.BedWars;
import ru.codein.bw.manager.ChatManager;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        event.setCancelled(true);

        HumanEntity whoClicked = event.getWhoClicked();
        PlayerInventory playerInventory = event.getWhoClicked().getInventory();
        ChatManager chatManager = BedWars.getInstance().getChatManager();

        if (event.getInventory().getName().equalsIgnoreCase("магазин")) {
            switch (event.getSlot()) {
                case 0:
                    if (playerInventory.contains(Material.IRON_INGOT, 2)) {
                        playerInventory.addItem(new ItemStack(Material.WOOL, 2));
                        playerInventory.removeItem(new ItemStack(Material.IRON_INGOT, 2));
                    } else {
                        chatManager.sendError((Player) whoClicked, "У вас недостаточно ресурсов");
                    }

                    break;
                case 1:
                    if (playerInventory.contains(Material.GOLD_INGOT, 4)) {
                        playerInventory.addItem(new ItemStack(Material.SHEARS, 1));
                        playerInventory.removeItem(new ItemStack(Material.GOLD_INGOT, 4));
                    } else {
                        chatManager.sendError((Player) whoClicked, "У вас недостаточно ресурсов");
                    }

                    break;
                case 2:
                    if (playerInventory.contains(Material.GOLD_INGOT, 6)) {
                        playerInventory.addItem(new ItemStack(Material.STONE_SWORD, 1));
                        playerInventory.removeItem(new ItemStack(Material.GOLD_INGOT, 6));
                    } else {
                        chatManager.sendError((Player) whoClicked, "У вас недостаточно ресурсов");
                    }

                    break;
            }
        }
    }
}
