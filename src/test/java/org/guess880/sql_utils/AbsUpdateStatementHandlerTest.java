package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.guess880.sql_utils.mock.AbsConnectionMock;
import org.guess880.sql_utils.mock.PreparedStatementCloseMock;
import org.junit.Test;

public class AbsUpdateStatementHandlerTest {

    @Test
    public void testGetResult() throws SQLException {
        final int result = 1;
        final AbsUpdateStatementHandler handler = new AbsUpdateStatementHandlerMock() {};
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock() {
                    @Override
                    public int executeUpdate() throws SQLException {
                        return result;
                    }
                };
            }
        });
        handler.execute();
        assertThat(handler.getResult(), equalTo(result));
    }

    @Test
    public void testSQLExceptionOnSetParameters() throws SQLException {
        final SQLException ex = new SQLException();
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                super.setParameters(stmt);
                throw ex;
            }
        };
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock();
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
    public void testSQLExceptionOnClose() throws SQLException {
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {};
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
    public void testSQLExceptionOnSetParametersAndClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                super.setParameters(stmt);
                throw exOnHandle;
            }
        };
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
    public void testRuntimeExceptionOnSetParameters() throws SQLException {
        final RuntimeException ex = new RuntimeException();
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                super.setParameters(stmt);
                throw ex;
            }
        };
        handler.setConnection(new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return new PreparedStatementCloseMock();
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
    public void testRuntimeExceptionOnClose() throws SQLException {
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {};
        final RuntimeException ex = new RuntimeException();
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
    public void testRuntimeExceptionOnSetParametersAndClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                super.setParameters(stmt);
                throw exOnHandle;
            }
        };
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
    public void testSQLExceptionOnSetParametersAndRuntimeExceptionOnClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                super.setParameters(stmt);
                throw exOnHandle;
            }
        };
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
    public void testRuntimeExceptionOnSetParametersAndSQLExceptionOnClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final AbsUpdateStatementHandlerMock handler = new AbsUpdateStatementHandlerMock() {
            @Override
            protected void setParameters(final PreparedStatement stmt) throws SQLException {
                super.setParameters(stmt);
                throw exOnHandle;
            }
        };
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
        });
        try {
            handler.execute();
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnHandle));
            assertThat(handler.getStatement().isClosed(), equalTo(true));
        }
    }

    private static class AbsUpdateStatementHandlerMock extends AbsUpdateStatementHandler {

        private PreparedStatement stmt;

        @Override
        protected void setParameters(PreparedStatement stmt)
                throws SQLException {
            this.stmt = stmt;
        }

        private PreparedStatement getStatement() {
            return stmt;
        }

    }

}
