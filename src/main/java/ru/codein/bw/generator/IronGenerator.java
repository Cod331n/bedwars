package ru.codein.bw.generator;


import org.bukkit.Material;
import ru.codein.bw.BedWars;

public class IronGenerator extends Generator {

    @Override
    public void initialize() {
        setDelay(BedWars.getInstance().getConfig().getInt("ironGeneratorDelay"));
        setMaterial(Material.IRON_INGOT);
        setName("iron");
        super.initialize();
    }

}
