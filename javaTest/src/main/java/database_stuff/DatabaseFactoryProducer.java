package database_stuff;

public class DatabaseFactoryProducer {

    public DatabaseFactory getDatabaseType(String databaseType){

        DataBaseType database;

        try {
            database = DataBaseType.valueOf(databaseType);
        }catch(IllegalArgumentException e){
            database = DataBaseType.valueOf("NONE");
        }

        switch (database) {
            case ORACLE:
                return new OracleDatabase();
            default:
                return null;
        }
    }
}
