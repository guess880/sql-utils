package org.guess880.sql_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbsPreparedStatementHandler<T extends AbsResultHandler<?>> {

    private Connection con;

    private String sql;

    private ParameterHandler paramHandler;

    private T resultHandler;

    public AbsPreparedStatementHandler<T> setConnection(final Connection con) {
        this.con = con;
        return this;
    }

    public AbsPreparedStatementHandler<T> setSql(final String sql) {
        this.sql = sql;
        return this;
    }

    public AbsPreparedStatementHandler<T> setParameterHandler(final ParameterHandler handler) {
        this.paramHandler = handler;
        return this;
    }

    public AbsPreparedStatementHandler<T> setResultHandler(final T handler) {
        this.resultHandler = handler;
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

    public T execute() throws SQLException {
        SQLException ex = null;
        final PreparedStatement stmt = prepareStatement(con, sql);
        try {
            paramHandler.handle(stmt);
            resultHandler.execute(stmt);
        } catch (final SQLException e) {
            ex = e;
        } finally {
            ex = closeStatement(stmt, ex);
        }
        if (ex != null) {
            throw ex;
        }
        return resultHandler;
    }

    protected abstract PreparedStatement prepareStatement(final Connection con, final String sql) throws SQLException;

}
