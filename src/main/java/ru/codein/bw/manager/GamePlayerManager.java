package ru.codein.bw.manager;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.codein.bw.player.GamePlayer;
import ru.codein.bw.player.PlayerData;
import ru.codein.bw.storage.PlayerStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class GamePlayerManager {
    public static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    private final PlayerStorage storage;
    private final Map<UUID, GamePlayer> players = new ConcurrentHashMap<>();

    public void initialize() {
        EXECUTOR.scheduleAtFixedRate(() -> {
            for (GamePlayer player : players.values()) {
                if (player.forceSave) {
                    CompletableFuture<Boolean> future = save(player);
                    future.whenComplete((bool, throwable) -> {
                        if (throwable != null) {
                            return;
                        }

                        if (bool) {
                            player.forceSave = false;
                        }
                    });
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public CompletableFuture<GamePlayer> createPlayer(UUID id, String name) {
        return storage.load(id).thenApply(data -> {
            GamePlayer player = new GamePlayer(id, name);
            player.load(data);
            players.put(id, player);
            return player;
        });
    }
    public CompletableFuture<Boolean> save(GamePlayer player) {
        PlayerData data = player.toData();
        return storage.save(player.getId(), data).whenComplete((result, throwable) -> {
            if (throwable != null || !result) {
                player. forceSave = true;
            }
        });
    }

    public GamePlayer get(UUID id) {
        return players.get(id);
    }

    public GamePlayer get(Player player) {
        return get(player.getUniqueId());
    }

    public GamePlayer get(String name) {
        val player = Bukkit.getPlayer(name);
        return get(player);
    }

    public GamePlayer invalidate(UUID id) {
        return players.remove(id);
    }

    public Collection<GamePlayer> all() {
        return Collections.unmodifiableCollection(players.values());
    }
}