package ru.codein.bw.runnable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.codein.bw.BedWars;

@AllArgsConstructor
public class CountdownForPlayer extends BukkitRunnable {
    @Getter
    private int timer;
    @Getter
    private String text;
    @Getter
    private Player player;

    @Override
    public void run() {
        if (timer >= 0) {
            if (BedWars.getInstance().getDataManager().getCurrentPlayers().contains(player)) {
                CraftPlayer craftPlayer = (CraftPlayer) player;
                craftPlayer.sendTitle("", text + " " + timer);
                craftPlayer.playSound(craftPlayer.getLocation(), Sound.NOTE_PLING, 1f, 1f);

            } else {
                cancel();
            }
        } else {
            cancel();
            return;
        }

        timer--;
    }
}
