package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.sql.SQLException;

import org.guess880.sql_utils.mock.AbsPreparedStatementMock;
import org.junit.Test;


public class UpdateResultHandlerTest {

    @Test
    public void testExecute() throws SQLException {
        final int result = 2;
        final UpdateResultHandler handler = new UpdateResultHandler();
        handler.execute(new AbsPreparedStatementMock() {
            @Override
            public int executeUpdate() throws SQLException {
                return result;
            }
        });
        assertThat(handler.getResult(), equalTo(result));
    }

}
