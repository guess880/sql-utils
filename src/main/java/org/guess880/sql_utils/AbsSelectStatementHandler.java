package org.guess880.sql_utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbsSelectStatementHandler extends
        AbsPreparedStatementHandler {

    private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;

    private int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;

    private int resultSetHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;

    private AbsResultSetHandler<?> handler;

    public AbsSelectStatementHandler setResultSetType(final int resultSetType) {
        this.resultSetType = resultSetType;
        return this;
    }

    public AbsSelectStatementHandler setResultSetConcurrency(
            final int resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
        return this;
    }

    public AbsSelectStatementHandler setResultSetHoldability(
            final int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
        return this;
    }

    public AbsSelectStatementHandler setResultSetHandler(
            final AbsResultSetHandler<?> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public void execute() throws SQLException {
        SQLException ex = null;
        final PreparedStatement stmt = getConnection().prepareStatement(
                getSql(), resultSetType, resultSetConcurrency,
                resultSetHoldability);
        try {
            setParameters(stmt);
            handler.execute(stmt);
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
