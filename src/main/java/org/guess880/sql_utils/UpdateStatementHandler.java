package org.guess880.sql_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateStatementHandler extends
        AbsPreparedStatementHandler<UpdateResultHandler> {

    @Override
    public PreparedStatement prepareStatement(final Connection con, final String sql) throws SQLException {
        return con.prepareStatement(sql);
    }

}
