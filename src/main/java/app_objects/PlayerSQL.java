package app_objects;

import database_stuff.DatabaseObject;
import database_stuff.DatabaseObjectFilterOperation;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class PlayerSQL extends Player implements DatabaseObject {

    public PlayerSQL(Player parent){

        this.copyFields(parent, this);

    }

    public void copyFields(Object source, Object target) {
        Field[] fieldsSource = source.getClass().getDeclaredFields();
        Field[] fieldsTarget = target.getClass().getSuperclass().getDeclaredFields();

        for (Field fieldTarget : fieldsTarget)
        {
            for (Field fieldSource : fieldsSource)
            {
                if (fieldTarget.getName().equals(fieldSource.getName()))
                {
                    try
                    {
                        fieldTarget.set(target, fieldSource.get(source));
                    }
                    catch (SecurityException e) {
                        System.out.println(fieldSource.getName() + " not taken from parent!");
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println(fieldSource.getName() + " not taken from parent!");
                    }
                    catch (IllegalAccessException e) {
                        System.out.println(fieldSource.getName() + " not taken from parent!");
                    }
                    break;
                }
            }
        }
    }

    public Object createInsertStatement(Object connection) {

        try {

            Connection conn = (Connection) connection;

            try {

                PreparedStatement stmt =
                        conn.prepareStatement("INSERT INTO EURO2016 "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                        );

                stmt.setString(1, name);
                stmt.setString(2, bio);
                stmt.setString(3, photoDone);
                stmt.setString(4, specialPlayer);
                stmt.setString(5, position);
                stmt.setString(6, playerNumber);
                stmt.setString(7, caps);
                stmt.setString(8, goalsForCountry);
                stmt.setString(9, club);
                stmt.setString(10, league);
                stmt.setDate(11, new java.sql.Date(dateOfBirth.getTime()));
                stmt.setString(12, ratingMatch1);
                stmt.setString(13, ratingMatch2);
                stmt.setString(14, ratingMatch3);

                return stmt;

            } catch (SQLException ex) {
                System.out.println("Failed to create Insert statement for " + this.getClass().getName() + "!");
                return null;
            }
        } catch (ClassCastException e) {
            System.out.println("Connection is of wrong type!");
            return null;
        }
        catch (NullPointerException e){
            System.out.println("Connection object is null!");
            return null;
        }

    }

    public Object createUpdateStatement(Object connection) {

        return null;

    }

    public Object createDeleteStatement(Object connection) {

        return null;

    }

    public int compareTo(Object javaObject) {

        try {
            PlayerSQL otherPlayer = (PlayerSQL) javaObject;

            try{
                if(this.getDateOfBirth().after(otherPlayer.getDateOfBirth())){
                    return 1;
                }else if(this.getDateOfBirth().equals(otherPlayer.getDateOfBirth())){
                    return 0;
                } else{
                    return -1;
                }
            }
            catch(NullPointerException ex){
                return -1;
            }
        }
        catch(ClassCastException e){
            return -1;
        }

    }

    public boolean filterObjectByField(
            String fieldName,
            Object fieldValue,
            DatabaseObjectFilterOperation filterOperation
    ) {
        try {

            if (fieldName.equals("dateOfBirth")) {

                if (filterOperation == DatabaseObjectFilterOperation.HIGHER) {
                    return this.dateOfBirth.after((Date) fieldValue);
                }

                if (filterOperation == DatabaseObjectFilterOperation.LOWER) {
                    return this.dateOfBirth.before((Date) fieldValue);
                }

            }

        } catch(NullPointerException e){
            e.printStackTrace();
        }

        return false;
    }

}
