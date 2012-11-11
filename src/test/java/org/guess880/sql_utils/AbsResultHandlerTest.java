package org.guess880.sql_utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;


public class AbsResultHandlerTest {

    @Test
    public void testResult() {
        final Object expected = new Object();
        final AbsResultHandler<Object> handler = new AbsResultHandler<Object>() {
            @Override
            protected void execute(PreparedStatement stmt) throws SQLException {
                // do nothing
            }
        };
        assertThat("assert default value.", handler.getResult(), nullValue());
        handler.setResult(expected);
        assertThat("assert value after setting.", handler.getResult(), equalTo(expected));
    }

}
