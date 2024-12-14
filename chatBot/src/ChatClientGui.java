import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGui extends JFrame{
    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;

    public ChatClientGui(){
        super("Chat Application");
        setSize(400,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        
        String name = JOptionPane.showInputDialog(this,"Enter your name:","Name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat Application -" + name);

        textField = new JTextField();
        textField.addActionListener(e -> {
            String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]"
            + name + ": " + textField.getText();
            client.sendMessage(message);
            textField.setText("");
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            String departureMessage = name + "has left the chat";
            client.sendMessage(departureMessage);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            System.exit(0);
        });
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField,BorderLayout.CENTER);
        bottomPanel.add(exitButton,BorderLayout.EAST);
        add(bottomPanel,BorderLayout.SOUTH);


        try {
            this.client = new ChatClient("127.0.0.1", 2000, this::onMessageReceived);
            client.startClient();
        } catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the server","Connection error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void onMessageReceived(String message){
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String args[]){
        SwingUtilities.invokeLater(() -> {
            new ChatClientGui().setVisible(true);
        });
    }
}
