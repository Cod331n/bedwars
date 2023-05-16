package ru.codein.bw.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import ru.codein.bw.BedWars;
import ru.codein.bw.cache.GeneratorCache;
import ru.codein.bw.game.GameStatus;
import ru.codein.bw.generator.Generator;
import ru.codein.bw.listener.event.GameEndEvent;
import ru.codein.bw.manager.WorldGuardManager;
import ru.codein.bw.runnable.CountdownForAll;

public class GameEndListener implements Listener {

    private final BedWars bedWars = BedWars.getInstance();


    public GameEndListener() {
        bedWars.getListenerCache().add(this);
    }

    @EventHandler
    public void onGameEndEvent(GameEndEvent event) {
        GeneratorCache generatorCache = bedWars.getGeneratorCache();

        bedWars.getDataManager().setGameStatus(GameStatus.ENDING);
        bedWars.getTeamManager().getTeamsMap().forEach((k, v) -> {

        });

        WorldGuardManager worldGuardManager = bedWars.getWorldGuardManager();

        // Очистка мира от поставленных блоков
        worldGuardManager.getPlacedBlocksMap().forEach(((block, location) -> location.getBlock().setType(Material.AIR)));
        worldGuardManager.clearPlacedBlocksMap();

        bedWars.getTeamManager().getTeamsMap().forEach((k,v) -> {
            if (v.equals(bedWars.getDataManager().getWon())) {
                bedWars.getGamePlayerManager().get(k).addWin();
            }
        });

        generatorCache.getGenerators().forEach(Generator::stop);

        int timer = 5;
        BukkitRunnable countdown = new CountdownForAll(5, "завершение игры через: ");
        countdown.run();

        Bukkit.getScheduler().runTaskLater(bedWars, () -> Bukkit.spigot().restart(), timer * 20L);

    }
}
