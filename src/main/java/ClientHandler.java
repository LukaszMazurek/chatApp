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
        // clientHandlers.add(this);
        // broadcastMessage("SERVER: " + clientUsername + " has entered");
    }

    @Override
    public void run() {

        while (socket.isConnected()){
            try {
                String messageFromClient = bufferedReader.readLine();
                handleMessage(messageFromClient);
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void handleMessage(String messageFromClient) throws IOException {
        if(messageFromClient.equals("SIGNUP")){
            String username = bufferedReader.readLine();
            String password = bufferedReader.readLine();
            if(authorizationHandler.handleRegistration(username, password)){
                bufferedWriter.write("ACCOUNT CREATED");
                clientUsername = username;
                clientHandlers.add(this);
                login = true;
            }
            else {
                bufferedWriter.write("LOGIN TAKEN");
            }
        }
        else if(messageFromClient.equals("SIGNIN")){
            String username = bufferedReader.readLine();
            String password = bufferedReader.readLine();
            if(authorizationHandler.handleLogin(username, password)){
                bufferedWriter.write("LOGIN");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                clientUsername = username;
                clientHandlers.add(this);
                login = true;
            }
            else {
                bufferedWriter.write("WRONG DATA");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        else {
            if (login) {
                broadcastMessage(messageFromClient);
            }
        }

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
        broadcastMessage("SERVER " + clientUsername + " left");
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
