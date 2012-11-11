package org.guess880.sql_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbsPreparedStatementHandler {

    private Connection con;

    private String sql;

    protected Connection getConnection() {
        return this.con;
    }

    protected String getSql() {
        return this.sql;
    }

    public AbsPreparedStatementHandler setConnection(final Connection con) {
        this.con = con;
        return this;
    }

    public AbsPreparedStatementHandler setSql(final String sql) {
        this.sql = sql;
        return this;
    }

    protected static SQLException closeStatement(final PreparedStatement stmt,
            final SQLException ex) {
        SQLException ret = ex;
        try {
            stmt.close();
        } catch (final SQLException e) {
            if (ret == null) {
                ret = e;
            }
        }
        return ret;
    }

    public abstract void execute() throws SQLException;

    protected abstract void setParameters(final PreparedStatement stmt) throws SQLException;

}
