import java.net.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private static final int PORT = 5432;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serversocket = new ServerSocket(PORT);
            System.out.println("Server is running waiting for connection..");

            // Accept incoming connection
            while (true) {
                Socket ClientSocket = serversocket.accept();
                System.out.println("New Client Connected " + ClientSocket);

                // create a new client handler for the connected client
                ClientHandler clienthandler = new ClientHandler(ClientSocket);
                clients.add(clienthandler);
                new Thread(clienthandler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void brodCast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {

        private Socket ClientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String UserName;

        // Constructor
        public ClientHandler(Socket socket) {
            this.ClientSocket = socket;

            try {
                out = new PrintWriter(ClientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                UserName = getUsername();
                System.out.println("User " + UserName + " Connected");

                out.println("Welcome to the chat, " + UserName + "!");
                out.println("Type your Message..");
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("[" + UserName + "]" + inputLine);

                    brodCast("[" + UserName + "]" + inputLine, this);
                }

                clients.remove(this);

                in.close();
                out.close();
                ClientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getUsername() throws IOException {
            out.println("Enter your name: ");
            return in.readLine();
        }

        public void sendMessage(String message) {
            out.println(message);
            out.println("Type your Message");
        }
    }

}
