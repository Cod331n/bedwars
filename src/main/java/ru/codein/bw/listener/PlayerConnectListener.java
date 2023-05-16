package ru.codein.bw.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.codein.bw.BedWars;
import ru.codein.bw.game.GameStatus;
import ru.codein.bw.listener.event.GameStartEvent;
import ru.codein.bw.manager.DataManager;
import ru.codein.bw.manager.TeamManager;
import ru.codein.bw.runnable.CountdownForAll;
import ru.codein.bw.util.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class PlayerConnectListener implements Listener {

    public PlayerConnectListener() {
        BedWars.getInstance().getListenerCache().add(this);
    }

    @EventHandler
    public void onPlayerConnectEvent(PlayerJoinEvent event) {

        BedWars bedWars = BedWars.getInstance();
        DataManager dataManager = bedWars.getDataManager();
        TeamManager teamManager = bedWars.getTeamManager();

        int playersToStart;
        Player player = event.getPlayer();
        Location lobbySpawn;

        try {
            bedWars.getGamePlayerManager().createPlayer(player.getUniqueId(), player.getName()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        try {
            lobbySpawn = bedWars.getSpawnManager().getLobbySpawn();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        switch (dataManager.getGameStatus()) {
            case WAITING:
                checkPlayersToStart();
                playersToStart = dataManager.getRequiredPlayersToStart() - dataManager.getCurrentPlayers().size();

                event.getPlayer().teleport(lobbySpawn);
                dataManager.addCurrentPlayer(player);
                teamManager.addPlayerInLeastFullTeam(player);

                bedWars.getChatManager().sendMessage(player.getName(), " присоеденился к игре. Осталось: " + playersToStart);
                Logger.writeLog(Level.INFO, player.getName() + " connected. Left: " + playersToStart);

                break;
            case INGAME:
            case ENDING:
                player.getInventory().clear();
                player.setGameMode(GameMode.SPECTATOR);
                teamManager.addPlayerInTeam(player, teamManager.getTeam("spectator"));

                Logger.writeLog(Level.INFO, player.getName() + " connected as a spectator. Left: " + dataManager.getCurrentPlayers());
                break;
        }
    }

    private void checkPlayersToStart() {
        BedWars bedWars = BedWars.getInstance();
        DataManager dataManager = bedWars.getDataManager();

        int playersToStart = dataManager.getRequiredPlayersToStart() - dataManager.getCurrentPlayers().size();

        if (playersToStart < 0) {
            int index = dataManager.getCurrentPlayers().size();
            Player player = dataManager.getCurrentPlayers().get(index - 1);

            player.kickPlayer("Сервер переполнен");

            dataManager.getCurrentPlayers().remove(player);
            bedWars.getTeamManager().removePlayerFromTeam(player);
            return;
        }

        // Начало игры если игроков для старта 0
        if (playersToStart == 0 && dataManager.getGameStatus() != GameStatus.INGAME) {
            int timer = bedWars.getConfig().getInt("countdownPreGame");
            BukkitRunnable countdownTask = new CountdownForAll(timer, "игра начнется через: ");
            countdownTask.run();

            Bukkit.getScheduler().runTaskLater(bedWars, () -> {
                GameStartEvent gameStartEvent = new GameStartEvent();
                Bukkit.getPluginManager().callEvent(gameStartEvent);
            }, (timer + 1) * 20L);
        }
    }
}
