package server.quizkampen;

import java.util.Random;

public class ServerActions {
    private static final Random random = new Random();

    //Utility class
    private ServerActions(){}


    public static void startGame(){
        sendQuestion();
    }

    public static void sendQuestion(){
        int tempRand = random.nextInt(12 - 1 + 1) + 1;

        for (ClientHandler clientHandler : ClientHandler.clientHandlers){
            clientHandler.sendQuestion(tempRand);
        }
    }

    public static void waitForAnswers(){

    }

}
