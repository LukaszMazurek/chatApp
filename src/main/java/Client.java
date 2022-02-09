import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Mediator mediator;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String msgFromChat;
    private String username;


    public Client(Socket socket, Mediator mediator) throws IOException {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.msgFromChat = "NAN";
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

    public String clientProtocol(String command,String[] params) {


        if(command.equals("LOGIN")){

            try {
                sendMessage(command);
                sendMessage(params[0]);
                sendMessage(params[1]);
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            if(msgFromChat.equals(command)){
                return "LOGIN";
            }
        }

            /*
            try {

                Scanner scanner = new Scanner(System.in);

                while (!msgFromChat.equals("ACCOUNT CREATED") && !msgFromChat.equals("LOGIN")) {

                    System.out.println("SIGNIN 1");
                    System.out.println("SIGNUP 2");
                    String message = scanner.nextLine();

                    if (message.equals("1")) {
                        authorization(scanner, "SIGNIN");
                    } else if (message.equals("2")) {
                        authorization(scanner, "SIGNUP");
                    }

                    Thread.sleep(100);
                }

                while (socket.isConnected()) {
                    String message = scanner.nextLine();
                    sendMessage(username + ": " + message);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
            */
            return "NAN";
    }



    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (socket.isConnected()){
                    try {
                        msgFromChat = bufferedReader.readLine();
                        System.out.println(msgFromChat);
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