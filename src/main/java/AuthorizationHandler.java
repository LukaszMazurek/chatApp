import javax.print.attribute.standard.OutputDeviceAssigned;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationHandler {

    private final String path = "";
    private Map<String, String> users;
    private FileHandler fileHandler;

    public AuthorizationHandler(){
        users = new HashMap<>();
        users.put("bob1", "bob1");
        users.put("bob2", "bob2");
        fileHandler = new FileHandler();
    }

    public Boolean handleRegistration(String login, String password){

        if(users.containsKey(login)){
            return false;
        }

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
