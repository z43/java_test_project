package main_stuff;

import app_objects.Player;
import database_stuff.DataBaseType;
import database_stuff.DatabaseObject;
import json_stuff.AppJsonObject;
import json_stuff.JsonObjectToJavaObjectNameMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AppObjectProducer {

    public AppJsonObject getAppObjectFromJson(String jsonKeyName){

        JsonObjectToJavaObjectNameMap jsonKey;

        try {
            jsonKey = JsonObjectToJavaObjectNameMap.valueOf(jsonKeyName);
        }catch(IllegalArgumentException e){
            jsonKey = JsonObjectToJavaObjectNameMap.valueOf("NONE");
        }

        switch (jsonKey) {
            case Players:
                return new Player();
            default:
                return null;
        }
    }

    public DatabaseObject getCorrespondingDatabaseObject(Object obj){

        DataBaseType database;

        try {
            database = DataBaseType.valueOf(JsonFileReadAppMain.dbType);
        }catch(IllegalArgumentException e){
            database = DataBaseType.valueOf("NONE");
        }

        switch (database) {
            case ORACLE:
                try {
                    Class<?> cls = Class.forName(obj.getClass().getName()+"SQL");

                    if(obj.getClass().isAssignableFrom(cls) && DatabaseObject.class.isAssignableFrom(cls)){

                        Constructor<?> constr = null;

                        try {

                            Class[] cArg = new Class[1];
                            cArg[0] = obj.getClass();
                            constr = cls.getDeclaredConstructor(cArg);

                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }

                        Object dbObj = null;

                        try {
                            dbObj= constr.newInstance(obj);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        return (DatabaseObject) dbObj;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            default:
                return null;
        }
    }

}
