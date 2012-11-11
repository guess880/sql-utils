package org.guess880.sql_utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ AbsPreparedStatementHandlerTest.class,
        AbsResultHandlerTest.class, AbsResultSetHandlerTest.class,
        SelectStatementHandlerTest.class, UpdateResultHandlerTest.class,
        UpdateStatementHandlerTest.class })
public class SqlUtilsAllTests {
}
