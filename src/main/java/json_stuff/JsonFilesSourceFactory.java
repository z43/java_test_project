package json_stuff;

import java.util.HashMap;
import java.util.List;

public interface JsonFilesSourceFactory {

    //try the runnable approach for multithreading
    void readJsonFilesAndConvertToJavaObjectsRunnable();
    //try the callable approach for multithreading
    void readJsonFilesAndConvertToJavaObjectsCallable();

    HashMap<Class, List<Object>> getJavaObjects();

    void setSourcePath(String sourcePath);

}
