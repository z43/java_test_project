package json_stuff;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import main_stuff.AppObjectProducer;

public class JsonObjectToJavaObjectConverter {

    private List<Object> objectsList = new ArrayList();

    public void convertJsonObjectToJavaObjects(JSONObject jsonObject){

        Iterator iterator = jsonObject.keys();
        String key = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();

            try {
                JsonObjectToJavaObjectNameMap jsonName
                        = JsonObjectToJavaObjectNameMap.valueOf(key);

                AppObjectProducer jsonObjectToJavaObjectFactoryProducer
                        = new AppObjectProducer();

                AppJsonObject jsonObjectToJavaObjectFactory;

                try {

                    JSONArray jsonArray = jsonObject.getJSONArray(key);

                    for (int i = 0; i < jsonArray.length(); ++i) {

                        JSONObject jsonArrElement = jsonArray.getJSONObject(i);

                        jsonObjectToJavaObjectFactory = jsonObjectToJavaObjectFactoryProducer.getAppObjectFromJson(key);

                        try {
                            Object javaObject
                                    = jsonObjectToJavaObjectFactory.parseJsonToObject(jsonArrElement);

                            objectsList.add(javaObject);

                        }catch(NullPointerException ex){
                            System.out.println(jsonName.getJavaClassName() + " class not found!");
                        }
                    }

                }
                catch(JSONException ex){
                    try {
                        jsonObjectToJavaObjectFactory
                                = jsonObjectToJavaObjectFactoryProducer.getAppObjectFromJson(key);

                        JSONObject innerKeyObject = jsonObject.getJSONObject(key);

                        Object javaObject
                                = jsonObjectToJavaObjectFactory.parseJsonToObject(innerKeyObject);

                        objectsList.add(javaObject);

                    }catch(NullPointerException exNull){
                        System.out.println(jsonName.getJavaClassName() + " class not found!");
                    }

                }

            }catch(IllegalArgumentException e){

                try {
                    JSONObject innerObject = jsonObject.getJSONObject(key);
                    convertJsonObjectToJavaObjects(innerObject);
                } catch (NullPointerException ex) {
                    System.out.println(key + " not found!");
                }
            }

        }

    }

    public List<Object> getObjectsList() {
        return this.objectsList;
    }
}
