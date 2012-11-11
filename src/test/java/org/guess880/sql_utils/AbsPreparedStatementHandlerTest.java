package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.guess880.sql_utils.mock.AbsConnectionMock;
import org.guess880.sql_utils.mock.AbsPreparedStatementMock;
import org.guess880.sql_utils.mock.PreparedStatementCloseMock;
import org.junit.Before;
import org.junit.Test;

public class AbsPreparedStatementHandlerTest {

    private AbsPreparedStatementHandlerMock handler;

    @Before
    public void setUp() {
        handler = new AbsPreparedStatementHandlerMock();
        handler.setResultHandler(new AbsResultHandler<Object>() {
            @Override
            protected void execute(PreparedStatement stmt) throws SQLException {
                // do nothing
            }
        });
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

    @Test
    public void testExecute() throws SQLException {
        final String sql = "SELECT * FROM dual";
        final AbsResultHandler<Object> expected = new AbsResultHandler<Object>() {
            @Override
            protected void execute(PreparedStatement stmt) throws SQLException {
                // do nothing
            }
        };
        @SuppressWarnings("unchecked")
        final AbsResultHandler<Object> actual = (AbsResultHandler<Object>) handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new AbsPreparedStatementMock() {};
            }
        }).setSql(sql)
        .setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
            }
        }).setResultHandler(expected)
        .execute();
        assertThat(actual, equalTo(expected));
        assertThat(handler.getSql(), equalTo(sql));
    }

    @Test
    public void testExecuteSQLExceptionOnHandle() throws SQLException {
        final SQLException ex = new SQLException();
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock();
            }
        }).setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                throw ex;
            }
        });
        try {
            handler.execute();
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(ex));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testExecuteSQLExceptionOnClose() throws SQLException {
        final SQLException ex = new SQLException();
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw ex;
                    }
                };
            }
        }).setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                // do nothing
            }
        });
        try {
            handler.execute();
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(ex));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testExecuteSQLExceptionOnHandleAndClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final SQLException exOnClose = new SQLException();
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        }).setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                throw exOnHandle;
            }
        });
        try {
            handler.execute();
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(exOnHandle));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testExecuteRuntimeExceptionOnHandle() throws SQLException {
        final RuntimeException ex = new RuntimeException();
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock();
            }
        }).setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                throw ex;
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(ex));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testExecuteRuntimeExceptionOnHandleAndClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final RuntimeException exOnClose = new RuntimeException();
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        }).setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                throw exOnHandle;
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnClose));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testExecuteSQLExceptionOnHandleAndRuntimeExceptionOnClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final RuntimeException exOnClose = new RuntimeException();
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        }).setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                throw exOnHandle;
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnClose));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    @Test
    public void testExecuteRuntimeExceptionOnHandleAndSQLExceptionOnClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final SQLException exOnClose = new SQLException();
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock() {
                    @Override
                    public void close() throws SQLException {
                        super.close();
                        throw exOnClose;
                    }
                };
            }
        }).setParameterHandler(new ParameterHandler() {
            @Override
            public void handle(PreparedStatement stmt) throws SQLException {
                throw exOnHandle;
            }
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnHandle));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    private static class AbsPreparedStatementHandlerMock extends AbsPreparedStatementHandler<AbsResultHandler<?>> {

        private String sql;

        private PreparedStatement stmt;

        @Override
        protected PreparedStatement prepareStatement(Connection con, String sql)
                throws SQLException {
            this.sql = sql;
            stmt = con.prepareStatement(sql);
            return stmt;
        }

        private String getSql() {
            return sql;
        }

        private PreparedStatement getStatement() {
            return stmt;
        }

    }

}
