package database_stuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseObjectSet {

    public  List<DatabaseObject> filterObjects(
            List<DatabaseObject> databaseObjects,
            String fieldName,
            Object fieldValue,
            DatabaseObjectFilterOperation filterOperation
    ){

        List<DatabaseObject> filterObjects = new ArrayList<DatabaseObject>();

        for (DatabaseObject dbObj : databaseObjects){

            if(dbObj.filterObjectByField(fieldName, fieldValue, filterOperation)) {
                filterObjects.add(dbObj);
            }

        }

        return filterObjects;

    }

    public  List<DatabaseObject> defaultOrderObjects(List<DatabaseObject> databaseObjects){

        Collections.sort(databaseObjects);

        return databaseObjects;

    }
}
