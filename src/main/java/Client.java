import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String msgFromChat;
    private String username;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.msgFromChat = "NAN";
    }

    public void sendMessage() {
        try {
            // bufferedWriter.write(username);
            // bufferedWriter.newLine();
            // bufferedWriter.flush();


            Scanner scanner = new Scanner(System.in);

            while (!msgFromChat.equals("ACCOUNT CREATED") && !msgFromChat.equals("LOGIN")){
                System.out.println("SIGNIN 1");
                System.out.println("SIGNUP 2");
                String message = scanner.nextLine();

                if(message.equals("1")){
                    System.out.println("PUT USERNAME");
                    username = scanner.nextLine();
                    System.out.println("PUT PASSWORD");
                    String password = scanner.nextLine();
                    bufferedWriter.write("SIGNIN");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    bufferedWriter.write(username);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    bufferedWriter.write(password);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }

            while (socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(username + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
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


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 5000);
        Client client = new Client(socket);
        client.listenForMessage();
        client.sendMessage();
    }
}