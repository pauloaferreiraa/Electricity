package Code;


import java.sql.*;

public class Database {

    Connection connection;
    Statement statement;

    public void connect() throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC"); //Carrega o jdbc
        connection = DriverManager.getConnection("jdbc:sqlite:be.db");
        statement = connection.createStatement();
    }

    public ResultSet getData(String query){
        ResultSet rs = null;

        try{
            rs = statement.executeQuery(query);
        }catch (Exception e){
            e.printStackTrace();
        }

        return rs;
    }


    public void close() throws SQLException{
        if(connection != null){
            connection.close();
        }
    }
}
