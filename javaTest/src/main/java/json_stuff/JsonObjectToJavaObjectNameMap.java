package json_stuff;

public enum JsonObjectToJavaObjectNameMap {
    Players("app_objects.Player"),
    NONE("NONE");

    private final String javaClassName;

    private JsonObjectToJavaObjectNameMap(final String text) {

        this.javaClassName = text;
    }

    public String getJavaClassName(){

        return this.javaClassName;

    }
}
