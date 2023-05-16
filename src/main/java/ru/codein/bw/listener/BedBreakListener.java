package ru.codein.bw.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.Team;
import ru.codein.bw.BedWars;

public class BedBreakListener implements Listener {

    private final BedWars bedWars = BedWars.getInstance();

    public BedBreakListener(){
        bedWars.getListenerCache().add(this);
    }

    @EventHandler
    public void onBedBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.BED_BLOCK)) {
            Team team = bedWars.getDataManager().getBedsMap().get(event.getBlock().getLocation());
            bedWars.getTeamManager().prohibitRespawn(team);
            bedWars.getDataManager().removeBedFromMap(team);
            bedWars.getGamePlayerManager().get(event.getPlayer()).addBrokenBed();

            bedWars.getServer().getOnlinePlayers().forEach(player -> {
                CraftPlayer craftPlayer = (CraftPlayer) player;
                craftPlayer.sendTitle("",  "Кровать команды: " + team.getName() + "сломана" );
                craftPlayer.playSound(craftPlayer.getLocation(), Sound.ENDERDRAGON_DEATH, 1f, 1f);
            });

        }
    }
}
