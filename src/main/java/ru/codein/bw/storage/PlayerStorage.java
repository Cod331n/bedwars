package ru.codein.bw.storage;

import ru.codein.bw.player.PlayerData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerStorage {

    CompletableFuture<PlayerData> load(UUID uuid);
    CompletableFuture<Boolean> save(UUID uuid, PlayerData data);

}
