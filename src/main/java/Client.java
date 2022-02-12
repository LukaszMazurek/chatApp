import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Mediator mediator;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String msgFromClient;
    private String username;


    public Client(Socket socket, Mediator mediator) throws IOException {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.msgFromClient = "NAN";
        this.mediator = mediator;
    }

    public void sendMessage(String msgToSend) throws IOException {
        bufferedWriter.write(msgToSend);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void authorization(Scanner scanner, String command) throws IOException {
        System.out.println("PUT USERNAME");
        username = scanner.nextLine();
        System.out.println("PUT PASSWORD");
        String password = scanner.nextLine();
        sendMessage(command);
        sendMessage(username);
        sendMessage(password);
    }

    public String clientProtocol(String command, String[] params) throws IOException {


        if(command.equals("LOGIN")){

            try {
                sendMessage(command);
                username = params[0];
                sendMessage(params[0]);
                sendMessage(params[1]);
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            if(msgFromClient.equals(command)){
                return "LOGIN";
            }
        }
        else if(command.equals("MESSAGE")){
            try {
                sendMessage(command);
                sendMessage(username + " : " + params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            String[] serverParams = {msgFromClient};
            mediator.notify("RECEIVED MESSAGE", serverParams);
        }
        return "NAN";
    }



    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (socket.isConnected()){
                    try {
                        msgFromClient = bufferedReader.readLine();
                        clientProtocol("", new String[]{});
                        System.out.println(msgFromClient);
                    }catch (IOException e){
                        e.printStackTrace();
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
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

    /*
    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        try {
            socket = new Socket("localhost", 5000);
            Client client = new Client(socket);
            client.listenForMessage();
            client.clientProtocol();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClientGui gui = new ClientGui();

    }*/
}