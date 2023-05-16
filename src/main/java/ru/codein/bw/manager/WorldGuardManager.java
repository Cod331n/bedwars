package ru.codein.bw.manager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldGuardManager implements IManager {

    @Getter
    private final Map<Block, Location> placedBlocksMap = new HashMap<>();

    public void clearPlacedBlocksMap() {
        placedBlocksMap.clear();
    }

    public void addBlockToMap(Block block, Location location) {
        placedBlocksMap.put(block, location);
    }

    public void removeBlockFromMap(Block block, Location location) {
        placedBlocksMap.remove(block, location);
    }

    public CompletableFuture<Boolean> canBreak(Block block, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            AtomicBoolean can = new AtomicBoolean(false);
            if (block.getType() == Material.BED_BLOCK) {
                can.set(true);
                return can.get();
            }

            placedBlocksMap.forEach((k, v) -> {
                if (block.equals(k) && location.equals(v)) {
                    can.set(true);
                }
            });

            return can.get();
        });
    }
}
