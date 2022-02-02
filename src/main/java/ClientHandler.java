import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientUsername = bufferedReader.readLine();
        clientHandlers.add(this);
        broadcastMessage("SERVER: " + clientUsername + " has entered");
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){

            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, bufferedReader, bufferedWriter);
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
