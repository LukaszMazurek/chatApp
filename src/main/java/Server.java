import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    final private ServerSocket serverSocket;
    final private AuthorizationHandler authorizationHandler;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.authorizationHandler = new AuthorizationHandler();
    }

    public void startServer() throws IOException {
        while (!serverSocket.isClosed()){
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");
            ClientHandler clientHandler = new ClientHandler(socket, authorizationHandler);
            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }

    public void closeServerSocket() throws IOException {
        if (serverSocket != null){
            serverSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
