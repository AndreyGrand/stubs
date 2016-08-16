package ru.sbrf.efs.rmkmcib.bht.app.util;

import java.sql.Clob;
import java.sql.SQLException;

/**
 * Created by sbt-ziyangirov-id on 15.08.2016.
 */
public class DBUtils {

    // ToDo улучшить метод
    public static String clobToString(Clob clob){
        try {
            return clob.getSubString(0, (int) clob.length());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
