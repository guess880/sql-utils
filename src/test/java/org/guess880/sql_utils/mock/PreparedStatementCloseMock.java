package org.guess880.sql_utils.mock;

import java.sql.SQLException;

public class PreparedStatementCloseMock extends AbsPreparedStatementMock {

    private boolean closed;

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

}