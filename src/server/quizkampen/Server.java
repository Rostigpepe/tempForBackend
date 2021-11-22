package server.quizkampen;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()){
                //socket.accept() is a blocking method, this means that the program will stop here
                //Until a connection is established
                Socket socket = serverSocket.accept();
                System.out.println("Successful connection");
                //Objects of this class will each handle communication with one client
                //The runnable interface is implemented on a class whose instances are executed by separate threads
                ClientHandler clientHandler = new ClientHandler(socket);

                //To handle multiple clients at once we need to assign every client a separate thread
                //Otherwise our program is only able to communicate with a single client at once
                Thread thread = new Thread(clientHandler);
                thread.start();

                if(ClientHandler.getClientHandlers() >= 4){
                    ServerActions.startGame();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public void closeServerSocket(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 12; i++) {
            int pain = i * 6;
            new Questions(Integer.toString(pain),
                    Integer.toString(pain - 1),
                    Integer.toString(pain- 2),
                    Integer.toString(pain - 3),
                    Integer.toString(pain - 4),
                    pain - 4);
        }

        ServerSocket serverSocket = new ServerSocket(7777);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
