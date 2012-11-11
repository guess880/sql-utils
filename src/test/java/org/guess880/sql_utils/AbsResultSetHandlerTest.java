package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.guess880.sql_utils.mock.AbsPreparedStatementMock;
import org.guess880.sql_utils.mock.AbsResultSetMock;
import org.junit.Test;

public class AbsResultSetHandlerTest {

    @Test
    public void testGetAndSetResult() throws SQLException {
        final Object result = new Object();
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                setResult(result);
            }
        };
        assertThat("assert default value.", handler.getResult(), is(nullValue()));
        handler.execute(new AbsPreparedStatementMock() {
            @Override
            public ResultSet executeQuery() throws SQLException {
                return new AbsResultSetMock() {};
            }
        });
        assertThat("assert value after setting.", handler.getResult(), equalTo(result));
    }

    @Test
    public void testSQLExceptionOnHandle() throws SQLException {
        final SQLException ex = new SQLException();
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                throw ex;
            }
        };
        final ResultSetMock rs = new ResultSetMock(){};
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(ex));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testSQLExceptionOnClose() throws SQLException {
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                // do nothing
            }
        };
        final SQLException ex = new SQLException();
        final ResultSetMock rs = new ResultSetMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw ex;
            }
        };
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(ex));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testSQLExceptionOnHandleAndClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                throw exOnHandle;
            }
        };
        final SQLException exOnClose = new SQLException();
        final ResultSetMock rs = new ResultSetMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw exOnClose;
            }
        };
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is SQLException.");
        } catch (final SQLException e) {
            assertThat(e, equalTo(exOnHandle));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnHandle() throws SQLException {
        final RuntimeException ex = new RuntimeException();
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                throw ex;
            }
        };
        final ResultSetMock rs = new ResultSetMock(){};
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(ex));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnClose() throws SQLException {
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                // do nothing
            }
        };
        final RuntimeException ex = new RuntimeException();
        final ResultSetMock rs = new ResultSetMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw ex;
            }
        };
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(ex));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnHandleAndClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                throw exOnHandle;
            }
        };
        final RuntimeException exOnClose = new RuntimeException();
        final ResultSetMock rs = new ResultSetMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw exOnClose;
            }
        };
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnClose));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testSQLExceptionOnHandleAndRuntimeExceptionOnClose() throws SQLException {
        final SQLException exOnHandle = new SQLException();
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                throw exOnHandle;
            }
        };
        final RuntimeException exOnClose = new RuntimeException();
        final ResultSetMock rs = new ResultSetMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw exOnClose;
            }
        };
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnClose));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    @Test
    public void testRuntimeExceptionOnHandleAndSQLExceptionOnClose() throws SQLException {
        final RuntimeException exOnHandle = new RuntimeException();
        final AbsResultSetHandler<Object> handler = new AbsResultSetHandler<Object>() {
            @Override
            public void handle(final ResultSet rs) throws SQLException {
                throw exOnHandle;
            }
        };
        final SQLException exOnClose = new SQLException();
        final ResultSetMock rs = new ResultSetMock() {
            @Override
            public void close() throws SQLException {
                super.close();
                throw exOnClose;
            }
        };
        try {
            handler.execute(new AbsPreparedStatementMock() {
                @Override
                public ResultSet executeQuery() throws SQLException {
                    return rs;
                }
            });
            fail("Expected exception is RuntimeException.");
        } catch (final RuntimeException e) {
            assertThat(e, equalTo(exOnHandle));
            assertThat(rs.isClosed(), equalTo(true));
        }
    }

    private abstract class ResultSetMock extends AbsResultSetMock {

        private boolean closed;

        @Override
        public boolean isClosed() {
            return closed;
        }

        @Override
        public void close() throws SQLException {
            this.closed = true;
        }

    }

}
