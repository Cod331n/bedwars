package ru.codein.bw.storage;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import ru.codein.bw.db.PlayerDao;
import ru.codein.bw.player.PlayerData;
import ru.codein.bw.util.Logger;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@RequiredArgsConstructor
public class MysqlPlayerStorage implements PlayerStorage {

    private final Executor executor = Executors.newFixedThreadPool(2);
    private final Jdbi jdbi;
    private final PlayerDao playerDao;

    @Override
    public CompletableFuture<PlayerData> load(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerData data = new PlayerData(0, 0, 0, 0, 0);
            PlayerData newData = playerDao.select(id);
            if (newData != null) {
                return newData;
            }

            playerDao.insert(id, data);
            return data;
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> save(UUID id, PlayerData data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                jdbi.useTransaction(handle -> playerDao.update(id, data));
                return true;
            } catch (Exception exception) {
                Logger.writeLog(Level.WARNING, "Error during transaction " + exception);
                return false;
            }
        });
    }
}
