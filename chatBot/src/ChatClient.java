import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private Consumer<String> onMessageReceived;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException{
            this.socket = new Socket(serverAddress,serverPort);
            System.out.println("Connected to the chat server!");

            this.onMessageReceived = onMessageReceived;

            this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String msg){
        out.println(msg);
    }

    public void startClient(){
        new Thread(() -> {
            try {
                String line;
                while((line = in.readLine()) != null){
                    onMessageReceived.accept(line);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
