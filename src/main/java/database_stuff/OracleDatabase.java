package database_stuff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OracleDatabase implements DatabaseFactory {

    private String url;
    private String user;
    private String pass;

    Connection oracleConnection;

    public void startDatabaseConnection() {

        try {

            oracleConnection = DriverManager.getConnection(this.url, this.user, this.pass);

        } catch (SQLException e) {
            System.out.println("Failed to open connection to Oracle database!");
        }
    }

    public void closeDatabaseConnection() {

        try {
            oracleConnection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close connection to Oracle database!");
        }
        catch (NullPointerException e){
            System.out.println("Failed to close connection to Oracle database! Connection is null!");
        }

    }

    public Object getDatabaseConnection(){

        return this.oracleConnection;

    }

    public void executeInsertStatement(Object statement) {

        try{
            PreparedStatement stmt = (PreparedStatement) statement;

            try {
                stmt.executeUpdate();

                stmt.close();

            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        catch(ClassCastException e){
            System.out.println("Error! PreparedStatement expected got " + statement.getClass().getName());
        }
        catch(NullPointerException e) {
            System.out.println("PreparedStatement is null!");
        }
    }

    public void setUrl(String databaseUrl) {
        this.url = databaseUrl;
    }

    public void setUser(String databaseUser) {
        this.user = databaseUser;
    }

    public void setPass(String databasePass) {
        this.pass = databasePass;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPass() {
        return pass;
    }
}
