package ru.codein.bw.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import ru.codein.bw.BedWars;
import ru.codein.bw.game.TeamState;
import ru.codein.bw.listener.event.GameEndEvent;
import ru.codein.bw.manager.SpawnManager;
import ru.codein.bw.runnable.CountdownForPlayer;

import java.io.IOException;
import java.util.Map;

public class RespawnListener implements Listener {

    public RespawnListener() {
        BedWars.getInstance().getListenerCache().add(this);
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerDeathEvent event) throws IOException, InvalidConfigurationException {
        BedWars bedWars = BedWars.getInstance();
        SpawnManager spawnManager = bedWars.getSpawnManager();
        Map<Player, Team> teamsMap = bedWars.getTeamManager().getTeamsMap();
        Team team = teamsMap.get(event.getEntity());
        Player player = event.getEntity();
        Location spectatorLocation = spawnManager.getSpectatorSpawn();

        if (bedWars.getTeamManager().getTeamsState().get(team).equals(TeamState.CAN_RESPAWN)) {
            Location respawnLocation = spawnManager.getTeamSpawn(teamsMap.get(player));

            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(spectatorLocation);

            int timer = bedWars.getConfig().getInt("respawnDelay");
            BukkitRunnable countdownTask = new CountdownForPlayer(timer, "Возрождение через: ", event.getEntity());
            countdownTask.runTaskTimer(bedWars, 0, 20L);

            Bukkit.getScheduler().runTaskLater(bedWars, () -> {

                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(respawnLocation);

            }, (timer) * 20L);
        } else {
            Team teamBefore = teamsMap.get(player);
            bedWars.getTeamManager().removePlayerFromTeam(player);
            bedWars.getTeamManager().addPlayerInTeam(player, bedWars.getTeamManager().getTeam("spectators"));

            // Если в мапе есть еще члены команды, иначе
            if (teamsMap.containsValue(teamBefore)) {
                teamsMap.values().forEach(System.out::println);
            } else {
                bedWars.getDataManager().setWon(teamsMap.get(player.getKiller()));
                GameEndEvent gameEndEvent = new GameEndEvent();
                Bukkit.getPluginManager().callEvent(gameEndEvent);
            }

            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(spectatorLocation);

            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.sendTitle("", "Вы погибли");
            craftPlayer.playSound(craftPlayer.getLocation(), Sound.NOTE_PLING, 1f, 1f);
        }

    }
}
