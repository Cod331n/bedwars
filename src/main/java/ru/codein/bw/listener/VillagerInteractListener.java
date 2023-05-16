package ru.codein.bw.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import ru.codein.bw.BedWars;

public class VillagerInteractListener implements Listener {

    @EventHandler
    public void onVillagerInteractEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getCustomName().equalsIgnoreCase("магазин")) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();

            event.getPlayer().openInventory(BedWars.getInstance().getShopManager().getShopMenu());
        }
    }
}
