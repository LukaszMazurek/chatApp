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
        if (command.equals("LOGIN")) {
            if(client.clientProtocol("LOGIN", params).equals("LOGIN")){
                gui.clearWindow();
                gui.setChatLocationAndSize();
                gui.addComponentsToChat();
                gui.refresh();
            }
        }

    }


    public static void main(String[] args) {
        Mediator mediator = new Mediator();
    }
}
