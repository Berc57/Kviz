package org.example.kviz;
import java.util.ArrayList;


public class Question {
    public String question;
    public ArrayList<String> answers;
    public int correct;

    public Question(String question, ArrayList<String> answers, int correct){
        this.setQuestion(question);
        this.setAnswers(answers);
        this.setCorrect(correct);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question= " + this.getQuestion() +
                ", answers= " + this.getAnswers() +
                ", correct= " + this.getCorrect() +
                "}";
    }
}
