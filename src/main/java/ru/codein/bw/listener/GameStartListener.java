package ru.codein.bw.listener;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.codein.bw.BedWars;
import ru.codein.bw.game.GameStatus;
import ru.codein.bw.generator.Generator;
import ru.codein.bw.listener.event.GameStartEvent;

import java.io.IOException;

public class GameStartListener implements Listener {

    private final BedWars bedWars = BedWars.getInstance();

    public GameStartListener() {
        bedWars.getListenerCache().add(this);
    }

    @EventHandler
    public void onGameStartEvent(GameStartEvent event) {
        bedWars.getDataManager().setGameStatus(GameStatus.INGAME);
        bedWars.getTeamManager().getTeamsMap().forEach((k, v) -> {
            try {
                k.teleport(bedWars.getSpawnManager().getTeamSpawn(v));
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        });

        bedWars.getGeneratorCache().getGenerators().forEach(Generator::initialize);

    }
}
