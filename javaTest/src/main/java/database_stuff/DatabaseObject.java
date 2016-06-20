package database_stuff;

public interface DatabaseObject extends Comparable{

    Object createInsertStatement(Object connection);
    Object createUpdateStatement(Object connection);
    Object createDeleteStatement(Object connection);

    boolean filterObjectByField(
            String fieldName,
            Object fieldValue,
            DatabaseObjectFilterOperation filterOperation
    );
}
