import java.io.*;
import java.net.Socket;

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

    public String clientProtocol(String command, String[] params) {

        try {
            if (command.equals("LOGIN")) {
                sendMessage(command);
                username = params[0];
                sendMessage(params[0]);
                sendMessage(params[1]);
                Thread.sleep(100);

                if (msgFromClient.equals(command)) {
                    return "LOGIN";
                }
            } else if (command.equals("MESSAGE")) {
                sendMessage(command);
                sendMessage(username + " : " + params[0]);
            } else if (command.equals("REGISTER")) {
                sendMessage(command);
                username = params[0];
                sendMessage(params[0]);
                sendMessage(params[1]);
                Thread.sleep(100);
                if (msgFromClient.equals(command)) {
                    return "REGISTER";
                }
            } else {
                String[] serverParams = {msgFromClient};
                mediator.notify("RECEIVED MESSAGE", serverParams);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
}