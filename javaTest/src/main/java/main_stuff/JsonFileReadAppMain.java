package main_stuff;

import app_objects.Player;
import database_stuff.*;
import json_stuff.JsonFilesSourceFactory;
import json_stuff.JsonFilesSourceFactoryProducer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonFileReadAppMain {

    static String dbType = "";

    public static void main(String [] args) throws Exception{

        System.out.println("App start");

        long appStart = System.currentTimeMillis();

        String jsonFilesSourceType = "";
        String jsonFilesSourcePath = "";
        dbType = "ORACLE";

        //input part
        if(args.length == 0) {
            jsonFilesSourcePath = "C:/input/";
            jsonFilesSourceType = "DIRECTORY";
        }
        else {

            jsonFilesSourcePath = args[0];

            if(args.length == 1 ) {
                jsonFilesSourceType = "DIRECTORY";
            }
            else{
                jsonFilesSourceType = args[1];
            }
        }

        //get json files and convert them to java objects part
        long readStart = System.currentTimeMillis();

        JsonFilesSourceFactoryProducer jsonFilesSourceFactory =
                new JsonFilesSourceFactoryProducer();
        JsonFilesSourceFactory jsonFilesSource =
                jsonFilesSourceFactory.getJsonFilesSource(jsonFilesSourceType);

        if(jsonFilesSource != null) {

            jsonFilesSource.setSourcePath(jsonFilesSourcePath);

            //read files from source and convert them to application Java Objects
            //both approaches produce use approximately the same time
            //try Runnable approach
            //jsonFilesSource.readJsonFilesAndConvertToJavaObjectsRunnable();
            //try Callable approach
            jsonFilesSource.readJsonFilesAndConvertToJavaObjectsCallable();

        }
        else{
            System.out.println("Unknown JSON files source type!");
        }

        System.out.println("Total time for reading and parsing Json files : "
                +(System.currentTimeMillis()- readStart) + " ms ");

        //database part
        //converts extracted Application objects and converts them to Application DatabaseObjects
        //then for objects of class Player : filter, order, insert
        System.out.println("Start database part!");
        long dbStart = System.currentTimeMillis();

        DatabaseFactoryProducer databaseFactory = new DatabaseFactoryProducer();
        DatabaseFactory database = databaseFactory.getDatabaseType(dbType);
        database.setUrl("add");
        database.setUser("add");
        database.setPass("add");

        System.out.println("Database login params :");
        System.out.println("url : " + database.getUrl());
        System.out.println("user : " + database.getUser());
        System.out.println("pass : " + database.getPass());


        if(database != null){

            //get extracted application Objects from files
            HashMap<Class, List<Object>> extractedObjects = jsonFilesSource.getJavaObjects();

            for (Map.Entry<Class, List<Object>> entry : extractedObjects.entrySet()) {

                AppObjectProducer dbObjectProducer = new AppObjectProducer();
                List<DatabaseObject> dbObjects = new ArrayList<DatabaseObject>();

                //convert extracted Objects from files to DatabaseObjects
                //in this case app_objects.Player is converted to app_objects.PlayerSQL
                for(Object obj : entry.getValue()){
                    DatabaseObject dbObj = dbObjectProducer.getCorrespondingDatabaseObject(obj);

                    if(dbObj != null) {
                        dbObjects.add(dbObj);
                    }
                }

                System.out.println(
                        entry.getValue().size()
                        + " objects of class " + entry.getKey().getName()
                        + " converted to DatabaseObjects of " + dbObjects.get(0).getClass() + "!"
                );
                System.out.println( "Number of objects converted : " + dbObjects.size() + "!");
                DatabaseObjectSet dbObjectsSet = new DatabaseObjectSet();

                //do app_objects.Player class specific actions
                if(entry.getKey() == Player.class) {

                    //apply filter to extracted app_objects.Player DatabaseObjects
                    System.out.println("Start Filter DatabaseObjects");
                    long filterStart = System.currentTimeMillis();

                    DatabaseObjectFilterOperation filterOperation = DatabaseObjectFilterOperation.HIGHER;

                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date filterDate = df.parse("01/01/1992");

                    dbObjects = dbObjectsSet.filterObjects(
                                                dbObjects,
                                                "dateOfBirth",
                                                filterDate,
                                                filterOperation
                                             );

                    System.out.println("Number of objects after filter : " + dbObjects.size());

                    System.out.println("Filter DatabaseObjects time : "
                            +(System.currentTimeMillis()- filterStart) + " ms ");
                    //end filter

                    //Order extracted app_objects.Player DatabaseObjects
                    System.out.println("Start Order DatabaseObjects");
                    long ordStart = System.currentTimeMillis();

                    dbObjects = dbObjectsSet.defaultOrderObjects(dbObjects);

                    System.out.println("Order DatabaseObjects time : "
                            +(System.currentTimeMillis()- ordStart) + " ms ");
                    //end ord

                    System.out.println("Number of objects to be inserted : " + dbObjects.size());

                    //insert Objects in the database
                    long insertrStart = System.currentTimeMillis();

                    database.startDatabaseConnection();

                    if (database.getDatabaseConnection() != null) {

                        for (DatabaseObject obj : dbObjects) {

                            database.executeInsertStatement(
                                        obj.createInsertStatement(database.getDatabaseConnection())
                                     );

                        }
                    }

                    database.closeDatabaseConnection();

                    System.out.println("Insert DatabaseObjects time : "
                            +(System.currentTimeMillis()- insertrStart) + " ms ");
                }

            }

        }
        else {
            System.out.println("Unknown Database type!");
        }

        System.out.println("Database operations time : "
                +(System.currentTimeMillis()- dbStart) + " ms ");

        System.out.println("Total execution time : "
                +(System.currentTimeMillis()- appStart) + " ms ");

    }


}
