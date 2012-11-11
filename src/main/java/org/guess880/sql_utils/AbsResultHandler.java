package org.guess880.sql_utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbsResultHandler<T> {

    private T result;

    public void setResult(final T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    protected abstract void execute(final PreparedStatement stmt) throws SQLException;

}
