package ru.codein.bw.db;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.statement.StatementContext;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class UUIDArgument implements Argument {

    private static final int UUID_SIZE = 16;
    private final UUID uuid;

    @Override
    public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
        ByteBuffer bb = ByteBuffer.wrap(new byte[UUID_SIZE]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        statement.setBytes(position, bb.array());
    }
}