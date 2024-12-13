import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    // List to keep track of all connected clients

    private static List<ClientHandler> clients = new ArrayList<>();
    public static void main(String args[]) throws IOException{

        //create a server socket to wait for clients to connect on the port 3000
        ServerSocket serverSocket = new ServerSocket(2000);

        System.out.println("Server started. Waiting for clients...");
        
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            // Spawn a new thread for each client

            ClientHandler clientThread = new ClientHandler(clientSocket,clients);
            clients.add(clientThread);
            //Brush up on thread fundamentals, they need an instance of a runnable object for the constructor
            new Thread(clientThread).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.clients = clients;
        //creating a new printwriter which outputs what the client socket sent
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void run() {
        try {
            String inputLine;
            while(( inputLine = in.readLine()) != null){
                // Broadcast message to all clients
                for(ClientHandler aClient : clients){
                    aClient.out.println(inputLine);
                }
            }
        } catch(IOException e) {
            System.out.println("An error occured: " + e.getMessage());
        } finally {
            try{
                in.close();
                out.close();
                clientSocket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
