
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class FileHandler {

    private final String path = "C:\\Users\\ŁukaszMazurek\\IdeaProjects\\chatApp\\src\\main\\java\\db.txt";


    public void writeToFile(String username, String password){
        try {
            FileWriter myWriter = new FileWriter(path, true);
            myWriter.write(username + " " + password + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> readFromFile() {

        Map<String, String> users = new HashMap<>();
        File file = new File(
                path);

        try {
            BufferedReader br
                    = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                String[] data = st.split("\\s+");
                users.put(data[0], data[1]);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return users;
    }

}
