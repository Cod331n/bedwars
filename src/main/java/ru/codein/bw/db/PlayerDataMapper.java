package ru.codein.bw.db;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.codein.bw.player.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDataMapper implements ColumnMapper<PlayerData> {

    @Override
    public PlayerData map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        return new PlayerData(
                rs.getInt("kills"),
                rs.getInt("broken_blocks"),
                rs.getInt("broken_beds"),
                rs.getInt("wins"),
                rs.getInt("looses")
        );
    }
}
