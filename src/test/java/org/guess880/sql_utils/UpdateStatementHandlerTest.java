package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.guess880.sql_utils.mock.AbsConnectionMock;
import org.guess880.sql_utils.mock.AbsPreparedStatementMock;
import org.junit.Test;

public class UpdateStatementHandlerTest {

    @Test
    public void testPrepareStatement() throws SQLException {
        final PreparedStatement stmt = new AbsPreparedStatementMock() {};
        final AbsConnectionMock con = new AbsConnectionMock() {
            @Override
            public PreparedStatement prepareStatement(String sql)
                    throws SQLException {
                return stmt;
            }
        };
        assertThat(new UpdateStatementHandler().setConnection(con)
                .prepareStatement(con, null), equalTo(stmt));
    }

}
