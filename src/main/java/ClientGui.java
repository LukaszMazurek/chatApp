import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGui extends JFrame implements ActionListener {

    private Mediator mediator;
    private Container container = getContentPane();
    private JLabel userLabel = new JLabel("USERNAME");
    private JLabel passwordLabel= new JLabel("PASSWORD");
    private JTextField userTextField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JButton loginButton = new JButton("LOGIN");
    private JButton resetButton = new JButton("REGISTER");
    private JTextField msgToSend = new JTextField();
    private JTextField msgReceived = new JTextField();

    public ClientGui(Mediator mediator){
        this.mediator = mediator;
        setTitle("Chat");
        setVisible(true);
        setBounds(10,10,350,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public void setLayoutManager()
    {
        container.setLayout(null);
    }
    public void setLocationAndSize()
    {
        userLabel.setBounds(50,50,100,30);
        passwordLabel.setBounds(50,120,100,30);
        userTextField.setBounds(150,50,150,30);
        passwordField.setBounds(150,120,150,30);
        loginButton.setBounds(50,200,100,30);
        resetButton.setBounds(200,200,100,30);
    }
    public void addComponentsToContainer()
    {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(loginButton);
        container.add(resetButton);
    }

    public void addActionEvent(){
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
    }

    public void clearWindow() {
        container.removeAll();
    }

    public void setChatLocationAndSize(){
        msgToSend.setBounds(100, 100, 200, 50);
        msgReceived.setBounds(100, 200, 200, 50);
    }

    public void addComponentsToChat(){
        container.add(msgToSend);
        container.add(msgReceived);
    }

    public void refresh(){
        SwingUtilities.updateComponentTreeUI(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("LOGIN")) {
            String[] params = {userTextField.getText(), passwordField.getText()};
            mediator.notify("LOGIN", params);
        }else if(e.getActionCommand().equals("REGISTER")){

        }else if(e.getActionCommand().equals("MESSAGE")){

        }
    }
}
