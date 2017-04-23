import java.util.Properties;
import java.util.logging.*;
import java.util.Map;
import java.sql.*;

public class SqlHandler extends Handler {

    Connection connection;
    Properties connectionProps = new Properties();
    Statement stmt;
    int t;

    public SqlHandler(Map dbParams, int trace)
    {
        // TODO: Change dbparams for properties
        connectionProps.put("user", dbParams.get("userName"));
        connectionProps.put("password", dbParams.get("password"));
        t = trace;
        try {
            connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName") + ":" + dbParams.get("portNumber") + "/", connectionProps);
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(LogRecord record)
    {
        try {
            stmt.executeUpdate("insert into Log_Values('" + record.getMessage() + "', " + String.valueOf(t) + ")");
        } catch ( SQLException e ) {
            System.err.println("Error on open: " + e);
        }

    }

    @Override
    public void close()
    {
        try {
            if ( connection!=null )
                connection.close();
        } catch ( SQLException e ) {
            System.err.println("Error on close: " + e);
        }
    }

    @Override
    public void flush()
    {
    }
}