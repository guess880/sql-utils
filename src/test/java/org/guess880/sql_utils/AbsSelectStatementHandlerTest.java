package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.guess880.sql_utils.mock.AbsConnectionMock;
import org.guess880.sql_utils.mock.AbsResultSetMock;
import org.guess880.sql_utils.mock.PreparedStatementCloseMock;
import org.junit.Before;
import org.junit.Test;

public class AbsSelectStatementHandlerTest {

    private static final Object OBJ = new Object();

    private AbsResultSetHandler<ResultSetMock> rHandler;

    private AbsSelectStatementHandler sHandler;

    @Before
    public void setUp() {
        rHandler = new AbsResultSetHandler<ResultSetMock>() {
            @Override
            protected void handle(final ResultSet rs) throws SQLException {
                setResult((ResultSetMock) rs);
            }
        };
        sHandler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                stmt.setObject(1, OBJ);
            }
        };
        sHandler.setResultSetHandler(rHandler);
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

    @Test
    public void testSetResultSetHandler() throws SQLException {
        // every test call setResultSetHandler.
    }

    @Test
    public void testSQLExceptionOnSetParameters() throws SQLException {
        final SQLException ex = new SQLException();
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                rHandler.setResult(new ResultSetMock().setStatement(stmt));
                throw ex;
            }
        };
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock();
            }
        });
        try {
            handler.execute();
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(ex));
            assertThat((getStatementOfResultSetHandler()).isClosed(), equalTo(true));
        }
    }

    @Test
    public void testSQLExceptionOnClose() throws SQLException {
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                // do nothing
            }
        };
        final SQLException ex = new SQLException();
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw ex;
                    }
                };
            }
        });
        try {
            handler.execute();
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(ex));
            assertThat(getStatementOfResultSetHandler().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testSQLExceptionOnSetParametersAndClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                rHandler.setResult(new ResultSetMock().setStatement(stmt));
                throw exOnHandle;
            }
        };
        final SQLException exOnClose = new SQLException();
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        });
        try {
            handler.execute();
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(exOnHandle));
            assertThat(getStatementOfResultSetHandler().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnSetParameters() throws SQLException {
        final RuntimeException ex = new RuntimeException();
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                rHandler.setResult(new ResultSetMock().setStatement(stmt));
                throw ex;
            }
        };
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock();
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(ex));
            assertThat((getStatementOfResultSetHandler()).isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnClose() throws SQLException {
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                // do nothing
            }
        };
        final RuntimeException ex = new RuntimeException();
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw ex;
                    }
                };
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(ex));
            assertThat(getStatementOfResultSetHandler().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnSetParametersAndClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                rHandler.setResult(new ResultSetMock().setStatement(stmt));
                throw exOnHandle;
            }
        };
        final RuntimeException exOnClose = new RuntimeException();
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnClose));
            assertThat(getStatementOfResultSetHandler().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testSQLExceptionOnSetParametersAndRuntimeExceptionOnClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                rHandler.setResult(new ResultSetMock().setStatement(stmt));
                throw exOnHandle;
            }
        };
        final RuntimeException exOnClose = new RuntimeException();
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnClose));
            assertThat(getStatementOfResultSetHandler().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnSetParametersAndSQLExceptionOnClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final AbsSelectStatementHandler handler = new AbsSelectStatementHandler() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                rHandler.setResult(new ResultSetMock().setStatement(stmt));
                throw exOnHandle;
            }
        };
        final SQLException exOnClose = new SQLException();
        handler.setResultSetHandler(rHandler).setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql, int resultSetType,
                    int resultSetConcurrency, int resultSetHoldability)
                    throws SQLException {
                return new PreparedStatementMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnHandle));
            assertThat(getStatementOfResultSetHandler().isClosed(), equalTo(true));
        }
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
