package json_stuff;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileToJsonObjectConverter {

    public JSONObject parseFileToJSONObject(File file){
        //System.out.println("Start processing file: " + file.getName());

        StringBuilder jsonData = new StringBuilder();
        BufferedReader br = null;

        try {
            String line;
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                jsonData.append(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                System.out.println("Error closing file: " + file.getName());
            }
        }

        //System.out.println("End processing file: " + file.getName());
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData.toString());
        } catch (JSONException jsonEx) {
            jsonObject = null;
            System.out.println("File " + file.getName() + " is not a valid json!");
        }

        return jsonObject;
    }

}
