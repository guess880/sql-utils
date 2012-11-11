package org.guess880.sql_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectStatementHandler<T> extends
        AbsPreparedStatementHandler<AbsResultHandler<T>> {

    private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;

    private int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;

    private int resultSetHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;

    public SelectStatementHandler<T> setResultSetType(final int resultSetType) {
        this.resultSetType = resultSetType;
        return this;
    }

    public SelectStatementHandler<T> setResultSetConcurrency(
            final int resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
        return this;
    }

    public SelectStatementHandler<T> setResultSetHoldability(
            final int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
        return this;
    }

    @Override
    public PreparedStatement prepareStatement(final Connection con, final String sql) throws SQLException {
        return con.prepareStatement(
                sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

}
