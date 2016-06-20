package database_stuff;

public interface DatabaseFactory {

    void startDatabaseConnection();
    void closeDatabaseConnection();

    void setUrl(String databaseUrl);
    void setUser(String databaseUser);
    void setPass(String databasePass);
    String getUrl();
    String getUser();
    String getPass();

    Object getDatabaseConnection();

    void executeInsertStatement(Object statement);
}
