package ru.codein.bw.listener;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.codein.bw.BedWars;
import ru.codein.bw.game.GameStatus;

public class BlockPlaceListener implements Listener {

    private final BedWars bedWars = BedWars.getInstance();

    public BlockPlaceListener() {
        bedWars.getListenerCache().add(this);
    }

    @EventHandler
    public void onBlockPlaceEvent(org.bukkit.event.block.BlockPlaceEvent event) {
        if (bedWars.getDataManager().getGameStatus().equals(GameStatus.INGAME)) {
            Block block = event.getBlock();
            bedWars.getWorldGuardManager().addBlockToMap(block, block.getLocation());
        } else {
            event.setCancelled(true);
        }

    }
}
