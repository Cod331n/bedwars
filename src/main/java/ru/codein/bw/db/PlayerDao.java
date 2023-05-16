package ru.codein.bw.db;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.codein.bw.player.PlayerData;

import java.util.UUID;

public interface PlayerDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS bedwars_players (player_id BINARY (16) PRIMARY KEY, kills INT, broken_blocks INT, " +
            "broken_beds INT, wins INT, looses INT)")
    void createTable();

    @SqlQuery("SELECT kills, broken_blocks, broken_beds, wins, looses" +
            " FROM bedwars_players WHERE player_id = :id")
    PlayerData select(@Bind("id") UUID id);

    @SqlUpdate("INSERT INTO bedwars_players (player_id, kills, broken_blocks, broken_beds, wins, looses)" +
            " VALUES (:id, :kills, :brokenBlocks, :brokenBeds, :wins, :looses)")
    void insert(@Bind("id") UUID id, @BindBean PlayerData data);

    @SqlUpdate("UPDATE bedwars_players SET kills = :kills, broken_blocks = :brokenBlocks, broken_beds = :brokenBeds, " +
            "wins = :wins, looses = :looses WHERE player_id = :id")
    void update(@Bind("id") UUID id, @BindBean PlayerData data);

}
