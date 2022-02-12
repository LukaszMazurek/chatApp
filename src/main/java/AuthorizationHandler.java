import javax.print.attribute.standard.OutputDeviceAssigned;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationHandler {

    private final String path = "";
    private Map<String, String> users;
    private FileHandler fileHandler;

    public AuthorizationHandler(){
        fileHandler = new FileHandler();
        users = fileHandler.readFromFile();
    }

    public Boolean handleRegistration(String login, String password){

        if(users.containsKey(login)){
            return false;
        }

        fileHandler.writeToFile(login, password);
        users.put(login, password);
        return true;
    }

    public Boolean handleLogin(String login, String password){
        if(users.containsKey(login)){
            return users.get(login).equals(password);
        }
        return false;
    }

}
