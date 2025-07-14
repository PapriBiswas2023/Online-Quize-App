package quiz.application;

public class Question {
    public String question;
    public String optionA, optionB, optionC, optionD;
    public String correctOption;

    public Question(String question, String optionA, String optionB, String optionC, String optionD, String correctOption) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }
}
