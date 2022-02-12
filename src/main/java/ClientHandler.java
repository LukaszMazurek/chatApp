import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private AuthorizationHandler authorizationHandler;
    private boolean login;

    public ClientHandler(Socket socket, AuthorizationHandler authorizationHandler) throws IOException {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.authorizationHandler = authorizationHandler;
        this.login = false;
    }

    @Override
    public void run() {

        while (socket.isConnected()){
            try {
                String command = bufferedReader.readLine();
                if(command.equals("REGISTER")){
                    String username = bufferedReader.readLine();
                    String password = bufferedReader.readLine();
                    if(authorizationHandler.handleRegistration(username, password)){
                        privateMessage("ACCOUNT CREATED");
                        clientUsername = username;
                        clientHandlers.add(this);
                        login = true;
                    }
                    else {
                        privateMessage("LOGIN TAKEN");
                    }
                }
                else if(command.equals("LOGIN")){
                    String username = bufferedReader.readLine();
                    String password = bufferedReader.readLine();
                    if(authorizationHandler.handleLogin(username, password)){
                        privateMessage("LOGIN");
                        clientUsername = username;
                        clientHandlers.add(this);
                        login = true;
                    }
                    else {
                        privateMessage("WRONG DATA");
                    }
                }
                else if (command.equals("MESSAGE")){
                    if (login) {
                        String msg = bufferedReader.readLine();
                        broadcastMessage(msg);
                    }
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void privateMessage(String messageToSend) throws IOException {
        bufferedWriter.write(messageToSend);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void broadcastMessage(String messageToSend) throws IOException {
        for(ClientHandler clientHandler : clientHandlers){
            if(!clientHandler.clientUsername.equals(clientUsername)){
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            }
        }
    }

    public void removeClientHandler() throws IOException {
        clientHandlers.remove(this);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            removeClientHandler();
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
