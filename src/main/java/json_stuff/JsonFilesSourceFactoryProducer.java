package json_stuff;

public class JsonFilesSourceFactoryProducer {

    public JsonFilesSourceFactory getJsonFilesSource(String sourceType){

        JsonFilesSource jsonFilesSource;

        try {
            jsonFilesSource = JsonFilesSource.valueOf(sourceType);
        }catch(IllegalArgumentException e){
            jsonFilesSource = JsonFilesSource.valueOf("NONE");
        }

        switch (jsonFilesSource) {
            case DIRECTORY:
                return new JsonFilesDirectory();
            default:
                return null;
        }
    }
}
