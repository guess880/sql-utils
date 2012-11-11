package org.guess880.sql_utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbsUpdateStatementHandler extends
        AbsPreparedStatementHandler {

    private int result;

    public int getResult() {
        return this.result;
    }

    @Override
    public void execute() throws SQLException {
        SQLException ex = null;
        final PreparedStatement stmt = getConnection().prepareStatement(getSql());
        try {
            setParameters(stmt);
            result = stmt.executeUpdate();
        } catch (final SQLException e) {
            ex = e;
        } finally {
            ex = closeStatement(stmt, ex);
        }
        if (ex != null) {
            throw ex;
        }
    }

}
