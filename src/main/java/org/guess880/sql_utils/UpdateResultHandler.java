package org.guess880.sql_utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateResultHandler extends
        AbsResultHandler<Integer> {

    @Override
    protected void execute(final PreparedStatement stmt) throws SQLException {
        setResult(stmt.executeUpdate());
    }

}
