package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.guess880.sql_utils.mock.AbsConnectionMock;
import org.guess880.sql_utils.mock.PreparedStatementCloseMock;
import org.junit.Before;
import org.junit.Test;

public class AbsPreparedStatementHandlerTest {

    private AbsPreparedStatementHandler handler;

    @Before
    public void setUp() {
        handler = new AbsPreparedStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                // do nothing
            }
            @Override
            public void execute() throws SQLException {
                // do nothing
            }
        };
    }

    @Test
    public void testGetAndSetConnection() {
        assertThat(handler.getConnection(), is(nullValue()));
        final AbsConnectionMock con = new AbsConnectionMock() {};
        handler.setConnection(con);
        assertThat((AbsConnectionMock) handler.getConnection(), equalTo(con));
    }

    @Test
    public void testGetAndSetSql() {
        assertThat(handler.getSql(), is(nullValue()));
        final String sql = "SELECT * FROM dual";
        handler.setSql(sql);
        assertThat(handler.getSql(), equalTo(sql));
    }

    @Test
    public void testCloseStatementExIsNull() throws SQLException {
        final PreparedStatementCloseMock stmt = new PreparedStatementCloseMock();
        assertThat(AbsPreparedStatementHandler.closeStatement(stmt, null), nullValue());
        assertThat(stmt.isClosed(), equalTo(true));
    }

    @Test
    public void testCloseStatementExIsNullAndThrowsSQLException() throws SQLException {
        final SQLException ex = new SQLException();
        final PreparedStatementCloseMock stmt = new PreparedStatementCloseMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw ex;
            }
        };
        assertThat(AbsPreparedStatementHandler.closeStatement(stmt, null), equalTo(ex));
        assertThat(stmt.isClosed(), equalTo(true));
    }

    @Test
    public void testCloseStatementExIsNullAndThrowsRuntimeException() throws SQLException {
        final RuntimeException ex = new RuntimeException();
        final PreparedStatementCloseMock stmt = new PreparedStatementCloseMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw ex;
            }
        };
        try {
            AbsPreparedStatementHandler.closeStatement(stmt, null);
            fail("Expected exception is RuntimeException.");
        } catch (RuntimeException e) {
            assertThat(e, equalTo(ex));
            assertThat(stmt.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testCloseStatementExIsNotNullAndThrowsSQLException() throws SQLException {
        final SQLException exOriginal = new SQLException();
        final SQLException exOnClose = new SQLException();
        final PreparedStatementCloseMock stmt = new PreparedStatementCloseMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw exOnClose;
            }
        };
        assertThat(AbsPreparedStatementHandler.closeStatement(stmt, exOriginal), equalTo(exOriginal));
        assertThat(stmt.isClosed(), equalTo(true));
    }

    @Test
    public void testCloseStatementExIsNotNullAndThrowsRuntimeException() throws SQLException {
        final SQLException exOriginal = new SQLException();
        final RuntimeException exOnClose = new RuntimeException();
        final PreparedStatementCloseMock stmt = new PreparedStatementCloseMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw exOnClose;
            }
        };
        try {
            AbsPreparedStatementHandler.closeStatement(stmt, exOriginal);
            fail("Expected exception is RuntimeException.");
        } catch (RuntimeException e) {
            assertThat(e, equalTo(exOnClose));
            assertThat(stmt.isClosed(), equalTo(true));
        }
    }

}
