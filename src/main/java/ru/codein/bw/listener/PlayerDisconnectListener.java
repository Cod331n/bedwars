package ru.codein.bw.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.codein.bw.BedWars;
import ru.codein.bw.util.Logger;

import java.util.logging.Level;

public class PlayerDisconnectListener implements Listener {

    public PlayerDisconnectListener() {
        BedWars.getInstance().getListenerCache().add(this);
    }

    @EventHandler
    public void onPlayerDisconnectEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BedWars bedWars = BedWars.getInstance();

        bedWars.getGamePlayerManager().save(bedWars.getGamePlayerManager().get(player));
        switch (bedWars.getDataManager().getGameStatus()) {
            case WAITING:
            case INGAME:
            case ENDING:
                player.getInventory().clear();
                bedWars.getTeamManager().removePlayerFromTeam(player);
                bedWars.getDataManager().removeCurrentPlayer(player);

                Logger.writeLog(Level.INFO, player.getName() + " disconnected. Left: " + bedWars.getDataManager().getCurrentPlayers());
                break;
        }
    }
}
