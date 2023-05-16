package ru.codein.bw.runnable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import ru.codein.bw.BedWars;

@AllArgsConstructor
public class CountdownForAll extends BukkitRunnable {
    @Getter
    private int timer;
    @Getter
    private String text;

    @Override
    public void run() {
        if (timer >= 0) {
            BedWars.getInstance().getDataManager().getCurrentPlayers().forEach(player -> {
                if (BedWars.getInstance().getDataManager().getCurrentPlayers().contains(player)) {
                    CraftPlayer craftPlayer = (CraftPlayer) player;
                    craftPlayer.sendTitle("", text + "" + timer);
                    craftPlayer.playSound(craftPlayer.getLocation(), Sound.NOTE_PLING, 1f, 1f);

                } else {
                    cancel();
                }
            });

        } else {
            cancel();
            return;
        }

        timer--;
    }

}
