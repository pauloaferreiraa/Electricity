package Code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    Connection connection;
    Statement statement;

    public void connect() throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC"); //Carrega o jdbc
        connection = DriverManager.getConnection("jdbc:sqlite:be.db");
        statement = connection.createStatement();
    }


    public void close() throws SQLException{
        if(connection != null){
            connection.close();
        }
    }
}
