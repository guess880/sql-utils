package org.guess880.sql_utils.sample;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.guess880.sql_utils.AbsResultSetHandler;
import org.guess880.sql_utils.AbsSelectStatementHandler;
import org.guess880.sql_utils.AbsUpdateStatementHandler;

public class SqlUtilsSamples {

    public void selectSample() throws SQLException {
        final Connection con = DriverManager.getConnection("url", "user", "password");
        try {
            final AbsResultSetHandler<Map<String, Object>> resultHandler = new AbsResultSetHandler<Map<String,Object>>() {
                @Override
                protected void handle(final ResultSet rs) throws SQLException {
                    final Map<String, Object> ret = new HashMap<String, Object>();
                    if (rs.next()) {
                        ret.put("column1", rs.getString(1));
                        ret.put("column2", rs.getBigDecimal(2));
                        ret.put("column3", rs.getTimestamp(3));
                        setResult(ret);
                    } else {
                        throw new SQLException("No record.");
                    }
                }
            };
            new AbsSelectStatementHandler() {
                @Override
                protected void setParameters(final PreparedStatement stmt) throws SQLException {
                    stmt.setString(1, "key");
                    stmt.setLong(2, 1L);
                }
            }
            .setResultSetHandler(resultHandler)
            .setSql("SELECT string, bigdecimal, timestamp FROM table WHERE string = ? AND long = ?")
            .setConnection(con)
            .execute();
            System.out.println(resultHandler.getResult().get("column1"));
            System.out.println(resultHandler.getResult().get("column2"));
            System.out.println(resultHandler.getResult().get("column3"));
        } finally {
            con.close();
        }
    }

    public void updateSample() throws SQLException {
        final Connection con = DriverManager.getConnection("url", "user", "password");
        try {
            final AbsUpdateStatementHandler handler = (AbsUpdateStatementHandler) new AbsUpdateStatementHandler() {
                @Override
                protected void setParameters(final PreparedStatement stmt) throws SQLException {
                    stmt.setString(1, "key");
                    stmt.setBigDecimal(2, new BigDecimal("1"));
                    stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                }
            }
            .setSql("INSERT INTO table (string, bigdecimal, timestamp) VALUES (?, ?, ?)")
            .setConnection(con);
            handler.execute();
            System.out.println(handler.getResult());
        } finally {
            con.close();
        }
    }

}
