package com.trushdenys.db;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

class JdbcUtils {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private static boolean initialized;

    public static synchronized void initDriver(){
        if (!initialized){
            try {
                Class.forName(PriceLoadDaoJdbc.DRIVER_CLASS_NAME);
            } catch (ClassNotFoundException e){
                logger.error("Can't load db driver, exception: ", e);
            }
        }
        initialized = true;

    }
    public static void closeQuietly(ResultSet resultSet){
        if (resultSet != null){
            try{
                resultSet.close();
            } catch (SQLException e){
                logger.error("Can't close ResultSet quietly, exception: ", e);
            }
        }

    }
    public static void rollbackQuietly(Connection conn){
        if (conn != null){
            try{
                conn.rollback();
            } catch (SQLException e){
                logger.error("Can't rollback quietly, exception: ", e);
            }
        }
    }

    public static void closeQuietly(Statement statement) {
        if (statement != null){
            try{
                statement.close();
            } catch (SQLException e){
                logger.error("Can't close Statement quietly, exception: ", e);
            }
        }
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null){
            try{
                conn.close();
            } catch (SQLException e){
                logger.error("Can't close Connection quietly, exception: ", e);
            }
        }
    }
}
