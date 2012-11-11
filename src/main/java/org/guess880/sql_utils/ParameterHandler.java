package org.guess880.sql_utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {

    void handle(final PreparedStatement stmt) throws SQLException;

}
