package org.guess880.sql_utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbsResultSetHandler<T> extends AbsResultHandler<T> {

    protected void execute(final PreparedStatement stmt) throws SQLException {
        SQLException ex = null;
        final ResultSet rs = stmt.executeQuery();
        try {
            handle(rs);
        } catch (final SQLException e) {
            ex = e;
        } finally {
            ex = closeResultSet(rs, ex);
        }
        if (ex != null) {
            throw ex;
        }
    }

    private static SQLException closeResultSet(final ResultSet rs, final SQLException ex) {
        SQLException ret = ex;
        try {
            rs.close();
        } catch (final SQLException e) {
            if (ret == null) {
                ret = e;
            }
        }
        return ret;
    }

    protected abstract void handle(final ResultSet rs) throws SQLException;

}
