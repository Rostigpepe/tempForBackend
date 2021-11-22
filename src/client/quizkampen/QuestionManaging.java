package client.quizkampen;

public class QuestionManaging {

    //Utility class = Private constructor
    private QuestionManaging(){}

    public static String[] splitQuestion(String questionPacket){
        String[] splitQuestionInfo = questionPacket.split(",");

        for (int i = 0; i < splitQuestionInfo.length; i++) {
            splitQuestionInfo[i] = splitQuestionInfo[i].trim();
        }

        return splitQuestionInfo;
    }

}
