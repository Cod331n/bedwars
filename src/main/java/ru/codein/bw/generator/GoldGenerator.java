package ru.codein.bw.generator;

import org.bukkit.Material;
import ru.codein.bw.BedWars;

public class GoldGenerator extends Generator {

    @Override
    public void initialize() {
        setDelay(BedWars.getInstance().getConfig().getInt("goldGeneratorDelay"));
        setMaterial(Material.GOLD_INGOT);
        setName("gold");
        super.initialize();
    }

}
