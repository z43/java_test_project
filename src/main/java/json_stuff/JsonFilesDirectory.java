package json_stuff;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static java.util.Collections.synchronizedList;

import org.json.JSONObject;


public class JsonFilesDirectory implements JsonFilesSourceFactory {

    private final int NUM_THREADS = 8;

    private HashMap<Class, List<Object>> javaObjectsMap;

    private List<Object> javaObjectsRunnable;
    private List<Future<List<Object>>> javaObjectsCallable;

    private String sourcePath;

    public void readJsonFilesAndConvertToJavaObjectsRunnable(){

        javaObjectsMap = new HashMap<Class, List<Object>>();

        System.out.println("Starting runnable approach for reading files!");

        javaObjectsRunnable = synchronizedList(new ArrayList<Object>());

        readDirectoryFilesRunnable(this.sourcePath);
    }

    public void readJsonFilesAndConvertToJavaObjectsCallable(){

        javaObjectsMap = new HashMap<Class, List<Object>>();

        System.out.println("Starting callable approach for reading files!");

        javaObjectsCallable = synchronizedList(new ArrayList<Future<List<Object>>>());

        readDirectoryFilesCallable(this.sourcePath);
    }

    public HashMap<Class, List<Object>> getJavaObjects(){
        return this.javaObjectsMap;
    }

    //Runnable approach
    public void readDirectoryFilesRunnable(String dirPath) {

        long start = System.currentTimeMillis();

        ExecutorService fileConverters = Executors.newFixedThreadPool(NUM_THREADS);

        File folder = new File(dirPath);
        if(folder.length() > 0) {

            for (File fileEntry : folder.listFiles()) {

                if (fileEntry.isDirectory()) {

                    readDirectoryFilesRunnable(fileEntry.getName());

                } else {

                    fileConverters.execute(new JsonFileConverterRunnable(fileEntry));

                }
            }

            try {
                fileConverters.shutdown();
                fileConverters.awaitTermination(5, TimeUnit.SECONDS);
            }
            catch (InterruptedException e) {
                System.err.println("tasks interrupted (runnable)");
            }
            finally {
                if (!fileConverters.isTerminated()) {
                    System.err.println("cancel non-finished tasks (runnable)");
                }
                fileConverters.shutdownNow();
                System.out.println("shutdown finished (runnable)");
            }
            while(!fileConverters.isTerminated());

            copyJavaObjectsRunnable();

        } else {
            System.out.println("Directory not found or directory is empty!");
        }

        System.out.println("Time Taken by Runnable concurrent reading/parsing : "
                                +(System.currentTimeMillis()-start) + " ms ");
    }

    //Callable approach
    public void readDirectoryFilesCallable(String dirPath) {

        long start = System.currentTimeMillis();

        ExecutorService fileConverters = Executors.newFixedThreadPool(NUM_THREADS);

        File folder = new File(dirPath);

        System.out.println("Start reading "+ ((folder.isDirectory())?"Directory":"File") + " " + folder.getName() + "!");

        if(folder.length() > 0 || folder.isDirectory()) {
            for (File fileEntry : folder.listFiles()) {

                if (fileEntry.isDirectory()) {

                    readDirectoryFilesCallable((dirPath.endsWith("/")?dirPath:dirPath + "/") + fileEntry.getName());

                } else {

                    Future<List<Object>> futureJsonObjects =
                            fileConverters.submit(new JsonFileConverterCallable(fileEntry));

                    javaObjectsCallable.add(futureJsonObjects);

                }
            }

            for (Future<List<Object>> futureObjects : javaObjectsCallable) {
                try {

                    futureObjects.get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            try {
                fileConverters.shutdown();
                fileConverters.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.err.println("tasks interrupted (callable)");
            } finally {
                if (!fileConverters.isTerminated()) {
                    System.err.println("cancel non-finished tasks (callable)");
                }
                fileConverters.shutdownNow();
                System.out.println("shutdown finished (callable)");
            }
            while (!fileConverters.isTerminated()) ;

            copyJavaObjectsCallable();

        } else {
            System.out.println("Directory " + folder.getName() + " : not found or directory is empty!");
        }

        System.out.println("Time Taken by Callable concurrent reading/parsing of " + folder.getName() + ": "
                +(System.currentTimeMillis()-start) + " ms ");

    }

    public void setSourcePath(String sourcePath) {

        this.sourcePath = sourcePath;
    }

    public void copyJavaObjectsRunnable(){

        for(Object javaObject : javaObjectsRunnable) {

            addJavaObjectsHashMapElement(javaObject);

        }

    }

    public void copyJavaObjectsCallable(){
        for(Future<List<Object>> innerList : javaObjectsCallable) {

            if (innerList.isDone()){
                try {

                    for (Object javaObject : innerList.get()) {

                        addJavaObjectsHashMapElement(javaObject);

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    ;
                }


            }

        }

    }

    private void addJavaObjectsHashMapElement(Object javaObject){

        if(javaObjectsMap.containsKey(javaObject.getClass())){
            List<Object> tmpArr = javaObjectsMap.get(javaObject.getClass());
            tmpArr.add(javaObject);
            javaObjectsMap.put(javaObject.getClass(), tmpArr);
        }
        else {
            List<Object> tmpArr = new ArrayList<Object>();
            tmpArr.add(javaObject);
            javaObjectsMap.put(javaObject.getClass(), tmpArr);
        }

    }

    //used for the runnable approach
    private class JsonFileConverterRunnable implements Runnable {

        private final File file;

        public JsonFileConverterRunnable(File file) {
            this.file = file;
        }

        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
                FileToJsonObjectConverter fileToJsonConvertor = new FileToJsonObjectConverter();
                JSONObject jsonObject = fileToJsonConvertor.parseFileToJSONObject(this.file);

                if(jsonObject != null) {

                    JsonObjectToJavaObjectConverter jsonToJavaConverter = new JsonObjectToJavaObjectConverter();

                    jsonToJavaConverter.convertJsonObjectToJavaObjects(jsonObject);

                    for (Object listEntry : jsonToJavaConverter.getObjectsList())
                    {
                        javaObjectsRunnable.add(listEntry);
                    }

                }
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
            //System.out.println(this.file.getName() + " added to list of json objects!");
        }
    }

}
