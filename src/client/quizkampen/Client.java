package client.quizkampen;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    //The clientHandler will await an assigned ID, so we'll send that right away
    public void sendAnswer(){
        sendPacket(username);

        //Replacement for future implementation of a GUI and buttons
        Scanner uInput = new Scanner(System.in);
        while(socket.isConnected()){
            String packet = uInput.nextLine();
            sendPacket(packet);
        }
    }


    //Lil method to not have to write all the below memes over and over again
    public void sendPacket(String inputToSend){
        try {
            bufferedWriter.write(inputToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    //This whole method will be a blocking one, therefore we'll run it in a difference thread
    public void listenForPacket(){
        new Thread(() -> {
            String receivedPacket;

            while(socket.isConnected()){
                try{
                    receivedPacket = bufferedReader.readLine();
                    questionHandling(receivedPacket);
                } catch (IOException e){
                    closeAll(socket, bufferedReader, bufferedWriter);
                }
            }

        }).start();
    }


    public void questionHandling(String questionPacket){
        String[] splitQuestion = QuestionManaging.splitQuestion(questionPacket);

        for (int i = 0; i < splitQuestion.length; i++) {
            switch (i){
                case 0 -> System.out.println(splitQuestion[i]);
                case 1,2,3,4 -> System.out.println("Option " + i + ": " + splitQuestion[i]);
                case 5 -> System.out.print("Please enter your answer: ");
                default -> System.out.println("Pain i dont know whats happening");
            }
        }
    }


    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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


    public static void main(String[] args) throws IOException {
        Scanner uInput = new Scanner(System.in);

        System.out.print("Ples gib name: ");
        String name = uInput.nextLine();

        Socket socket = new Socket("localhost", 7777);
        Client client = new Client(socket, name);
        client.listenForPacket();
        client.sendAnswer();
    }
}
