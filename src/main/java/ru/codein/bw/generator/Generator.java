package ru.codein.bw.generator;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.codein.bw.BedWars;

import java.io.IOException;

@Getter
@Setter
public abstract class Generator {

    private int delay;
    private Material material;
    private BukkitRunnable task;
    private String name;

    private BedWars bedWars = BedWars.getInstance();

    public void initialize() {
        start();
    }

    public Generator() {
        bedWars.getGeneratorCache().add(this);
    }

    public void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    bedWars.getSpawnManager().getGeneratorsLoc(name).forEach(generatorLoc -> {
                        bedWars.getServer().getWorld(generatorLoc.getWorld().getName()).dropItem(generatorLoc, new ItemStack(material, 1));
                    });
                } catch (IOException | InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        task.runTaskTimer(bedWars, 0L, delay * 20L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }

}
