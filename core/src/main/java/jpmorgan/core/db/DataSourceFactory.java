package jpmorgan.core.db;

import jpmorgan.core.Utils;
import org.hsqldb.jdbc.JDBCDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;


public class DataSourceFactory {

    private static final String SET_SQL_ORA_SYNTAX = "SET DATABASE SQL SYNTAX ORA TRUE;";
    private static final String SQL_RESOURCE_PATH = "/init.sql";

    private DataSourceFactory() {
        init();
    }

    private void init() {
        try (Connection connection = createDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SET_SQL_ORA_SYNTAX);
            statement.execute(Utils.readFileAsString(SQL_RESOURCE_PATH));
        } catch (Exception e) {
            throw Utils.convertToRuntimeException(e);
        }
    }

    public DataSource createDataSource() {
        JDBCDataSource dataSource = new JDBCDataSource();
        // all data persists in memory as required
        dataSource.setURL("jdbc:hsqldb:mem:mymemdb");
        dataSource.setUser("SA");
        dataSource.setPassword("");
        return dataSource;
    }

    public static DataSourceFactory getInstance() {
        return DataSourceFactoryHolder.INSTANCE;
    }

    private static class DataSourceFactoryHolder {
        private static final DataSourceFactory INSTANCE = new DataSourceFactory();
    }
}
