package json_stuff;

import org.json.JSONObject;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class JsonFileConverterCallable implements Callable<List<Object>> {

    private File file;

    JsonFileConverterCallable(File file){
        this.file = file;
    }

    public List<Object> call() throws Exception {
        try {
            TimeUnit.MILLISECONDS.sleep(2);

            FileToJsonObjectConverter fileToJsonConvertor = new FileToJsonObjectConverter();
            JSONObject jsonObject = fileToJsonConvertor.parseFileToJSONObject(this.file);

            if(jsonObject != null) {

                JsonObjectToJavaObjectConverter jsonToJavaConverter = new JsonObjectToJavaObjectConverter();

                jsonToJavaConverter.convertJsonObjectToJavaObjects(jsonObject);


                return jsonToJavaConverter.getObjectsList();
            }
            else {

                return null;
            }

        }
        catch (InterruptedException e) {
            throw new IllegalStateException("task interrupted", e);
        }
    }

}
