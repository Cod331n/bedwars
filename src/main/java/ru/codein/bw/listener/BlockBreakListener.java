package ru.codein.bw.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.codein.bw.BedWars;
import ru.codein.bw.manager.WorldGuardManager;

import java.util.concurrent.ExecutionException;

public class BlockBreakListener implements Listener {

    private final BedWars bedWars = BedWars.getInstance();
    private final WorldGuardManager worldGuardManager = bedWars.getWorldGuardManager();

    public BlockBreakListener() {
        bedWars.getListenerCache().add(this);
    }

    @EventHandler
    public void onBlockBreakEvent(org.bukkit.event.block.BlockBreakEvent event) {
        try {
            Block block = event.getBlock();
            Location blockLocation = block.getLocation();
            if (worldGuardManager.canBreak(block, blockLocation).get()) {
                worldGuardManager.removeBlockFromMap(block, blockLocation);
                bedWars.getGamePlayerManager().get(event.getPlayer()).addBrokenBlock();
            } else {
                event.setCancelled(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
