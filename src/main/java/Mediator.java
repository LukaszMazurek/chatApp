import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Mediator {

    private Client client;
    private ClientGui gui;

    public Mediator(){
        setClient();
        gui = new ClientGui(this);
    }

    private void setClient(){
        Socket socket = null;
        try {
            socket = new Socket("localhost", 5000);
            client = new Client(socket, this);
            client.listenForMessage();
            // client.clientProtocol();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void notify(String command, String[] params) {
        try {
            if (command.equals("LOGIN")) {
                if (client.clientProtocol("LOGIN", params).equals("LOGIN")) {
                    gui.clearWindow();
                    gui.setChatLocationAndSize();
                    gui.addComponentsToChat();
                    gui.refresh();
                }
            } else if (command.equals("MESSAGE")) {
                client.clientProtocol(command, params);
            } else if (command.equals("RECEIVED MESSAGE")) {
                String receivedMsg = params[0];
                gui.setReceivedMsg(receivedMsg);
                gui.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Mediator mediator = new Mediator();
    }
}
