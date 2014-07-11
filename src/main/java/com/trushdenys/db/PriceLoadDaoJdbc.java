package com.trushdenys.db;

import com.trushdenys.db.dbexceptions.DBSystemException;
import org.apache.log4j.Logger;
import java.sql.*;
import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

public class PriceLoadDaoJdbc implements PriceLoadDao {

        static final String DRIVER_CLASS_NAME = LoadProperties.loadProperties().getProperty("DRIVER_CLASS_DB");
        private static final String JDBC_URL = LoadProperties.loadProperties().getProperty("DBURL");
        private static final String SELECT_BY_ID_SQL = LoadProperties.loadProperties().getProperty("SELECT_BY_ID_SQL");
        private static final String SELECT_GET_COUNT = LoadProperties.loadProperties().getProperty("SELECT_GET_COUNT");

        private static final Logger logger = Logger.getLogger(getCurrentClassName());

        static {
            JdbcUtils.initDriver();
        }

        private Connection getConnection() throws DBSystemException{
            try {
                return DriverManager.getConnection(JDBC_URL);
            } catch (SQLException e) {
                throw new DBSystemException(e);
            }
        }

        public PriceDownload selectById(int id) {
            Connection conn = null;
            try {
                conn = getConnection();
            } catch (DBSystemException e) {
                logger.error("Can't get connection to DB, exception: ", e);
            }
            PreparedStatement ps = null;
            ResultSet rs = null;
            PriceDownload priceDownload = null;
            try {
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(SELECT_BY_ID_SQL);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs.next()){
                    priceDownload = new PriceDownload(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                }
                conn.commit();
            } catch (SQLException e){
                logger.error("Can't execute query '" + SELECT_BY_ID_SQL + "', exception: ", e);
                JdbcUtils.rollbackQuietly(conn);
            } finally {
                JdbcUtils.closeQuietly(rs);
                JdbcUtils.closeQuietly(ps);
                JdbcUtils.closeQuietly(conn);
            }
            return priceDownload;
        }

        public int selectCountTab() {
            Connection conn = null;
            try {
                conn = getConnection();
            } catch (DBSystemException e) {
                logger.error("Can't get connection to DB, exception: ", e);
            }
            Statement statement = null;
            ResultSet rs = null;
            int count = 0;
            try{
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conn.setAutoCommit(false);
                statement = conn.createStatement();
                rs = statement.executeQuery(SELECT_GET_COUNT);
                rs.last();
                count = rs.getInt(1);
                conn.commit();
            } catch (SQLException e){
                logger.error("Can't execute query '" + SELECT_GET_COUNT + "', exception: ", e);
                JdbcUtils.rollbackQuietly(conn);
            } finally {
                JdbcUtils.closeQuietly(rs);
                JdbcUtils.closeQuietly(statement);
                JdbcUtils.closeQuietly(conn);
            }
            return count;
        }
}
