package server.quizkampen;

import java.util.ArrayList;

public class Questions {

    protected static ArrayList<Questions> allQuestions = new ArrayList<>();
    protected static ArrayList<Questions> historyQuestions = new ArrayList<>();
    protected static ArrayList<Questions> geographyQuestions = new ArrayList<>();
    protected static ArrayList<Questions> cultureQuestions = new ArrayList<>();

    private final String questionTitle;
    private final String option1;
    private final String option2;
    private final String option3;
    private final String option4;
    private final int answer;

    public Questions(String questionTitle,
                     String option1,
                     String option2,
                     String option3,
                     String option4,
                     int answer){
        this.questionTitle = questionTitle;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;

        allQuestions.add(this);
    }

    //Just converts the answer to a string from an integer
    public String getAnswerToString(){
        return Integer.toString(this.answer);
    }


    //Just gets the whole object as a string, so we can send it
    public static String getQuestionString(int index){
        Questions question = allQuestions.get(index);
        return buildQuestionString(question);
    }


    //Takes all the info from the question object and turns it into a string
    public static String buildQuestionString(Questions question){
        String[] tempBuildArray = {question.questionTitle,
                question.option1,
                question.option2,
                question.option3,
                question.option4,
                question.getAnswerToString()};

        StringBuilder questionString = new StringBuilder();
        for (String questionBit : tempBuildArray){
            questionString.append(questionBit).append(",");
        }
        return questionString.toString();
    }

    public static void questionSplitter(String question){
        String[] questionBits = question.split(",");
        for(String questionBit : questionBits){
            questionBit = questionBit.replaceAll(",", "");
        }
    }
}
