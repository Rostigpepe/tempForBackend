package server.quizkampen;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    //Arraylist to keep track of all current connected clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    //A socket represents the connection between a server and client
    //The socket also has an input and output stream
    private Socket socket;
    //Receive input from clients
    private BufferedReader bufferedReader;
    //Send output to clients
    private BufferedWriter bufferedWriter;
    private String clientID;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            //Character streams always ends with writer/reader, byte streams always end with stream
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Makes it so that each client gets assigned the number in which order they connect
            this.clientID = Integer.toString(clientHandlers.size() + 1);
            this.clientUsername = bufferedReader.readLine();

            //Adding this instance of clientHandler to the arraylist we'll use for overall communication
            clientHandlers.add(this);

        } catch (IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override
    public void run() {
        String receivedInput;

        while(socket.isConnected()){
            try {
                //This is a blocking operation, yet another reason we want to run everything in separate threads
                receivedInput = bufferedReader.readLine();
                updateScore(receivedInput);
            } catch (IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }


    public void sendQuestion(int index){
            try {
                String packet = Questions.getQuestionString(index);
                this.bufferedWriter.write(packet);
                //We are listening for a new line, so we're sending one
                this.bufferedWriter.newLine();
                //We need to flush since it only auto flushes once the buffer is full, and we do NOT fill it
                this.bufferedWriter.flush();
            } catch(IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
            }
    }


    public void updateScore(String answer){
        //Add logic to check if the currently asked question

    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        System.out.println("Successfully removed");
    }

    //We only need to close the outer wrappers of the streams, the inner parts are auto closed
    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int getClientHandlers(){
        return clientHandlers.size();
    }

    public String getUsername(){
        return this.clientUsername;
    }
}
