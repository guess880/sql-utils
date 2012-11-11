package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.guess880.sql_utils.mock.AbsConnectionMock;
import org.guess880.sql_utils.mock.AbsResultSetMock;
import org.guess880.sql_utils.mock.PreparedStatementCloseMock;
import org.junit.Before;
import org.junit.Test;

public class SelectStatementHandlerTest {

    private AbsResultSetHandler<ResultSetMock> rHandler;

    private SelectStatementHandler<ResultSetMock> sHandler;

    @Before
    public void setUp() {
        rHandler = new AbsResultSetHandler<ResultSetMock>() {
            @Override
            protected void handle(final ResultSet rs) throws SQLException {
                setResult((ResultSetMock) rs);
            }
        };
        sHandler = new SelectStatementHandler<ResultSetMock>();
        sHandler.setResultHandler(rHandler);
        sHandler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock()
                    .setResultSetType(resultSetType)
                    .setResultSetConcurrency(resultSetConcurrency)
                    .setResultSetHoldability(resultSetHoldability);
            }
        });
        sHandler.setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                // do nothing
            }
        });
    }

    @Test
    public void testSetResultSetType() throws SQLException {
        sHandler.execute();
        assertThat("assert default value.", getStatementOfResultSetHandler().getResultSetType(), equalTo(ResultSet.TYPE_FORWARD_ONLY));
        final int resultSetType = -1;
        sHandler.setResultSetType(resultSetType).execute();
        assertThat("assert value after setting.", getStatementOfResultSetHandler().getResultSetType(), equalTo(resultSetType));
    }

    @Test
    public void testSetResultSetConcurrency() throws SQLException {
        sHandler.execute();
        assertThat("assert default value.", getStatementOfResultSetHandler().getResultSetConcurrency(), equalTo(ResultSet.CONCUR_READ_ONLY));
        final int resultSetConcurrency = -2;
        sHandler.setResultSetConcurrency(resultSetConcurrency).execute();
        assertThat("assert value after setting.", getStatementOfResultSetHandler().getResultSetConcurrency(), equalTo(resultSetConcurrency));
    }

    @Test
    public void testSetResultSetHoldability() throws SQLException {
        sHandler.execute();
        assertThat("assert default value.", getStatementOfResultSetHandler().getResultSetHoldability(), equalTo(ResultSet.HOLD_CURSORS_OVER_COMMIT));
        final int resultSetHoldability = -3;
        sHandler.setResultSetHoldability(resultSetHoldability).execute();
        assertThat("assert value after setting.", getStatementOfResultSetHandler().getResultSetHoldability(), equalTo(resultSetHoldability));
    }

    private Statement getStatementOfResultSetHandler() throws SQLException {
        return rHandler.getResult().getStatement();
    }

    private static class PreparedStatementMock extends PreparedStatementCloseMock {

        private int resultSetType;

        private int resultSetConcurrency;

        private int resultSetHoldability;

        @Override
        public int getResultSetType() {
            return resultSetType;
        }

        private PreparedStatementMock setResultSetType(final int resultSetType) {
            this.resultSetType = resultSetType;
            return this;
        }

        @Override
        public int getResultSetConcurrency() {
            return resultSetConcurrency;
        }

        private PreparedStatementMock setResultSetConcurrency(final int resultSetConcurrency) {
            this.resultSetConcurrency = resultSetConcurrency;
            return this;
        }

        @Override
        public int getResultSetHoldability() {
            return resultSetHoldability;
        }

        private PreparedStatementMock setResultSetHoldability(final int resultSetHoldability) {
            this.resultSetHoldability = resultSetHoldability;
            return this;
        }

        @Override
        public ResultSet executeQuery() throws SQLException {
            return new ResultSetMock().setStatement(this);
        }

    }

    private static class ResultSetMock extends AbsResultSetMock {

        private Statement stmt;

        @Override
        public Statement getStatement() throws SQLException {
            return stmt;
        }

        private ResultSetMock setStatement(final Statement stmt) {
            this.stmt = stmt;
            return this;
        }

    }

}
