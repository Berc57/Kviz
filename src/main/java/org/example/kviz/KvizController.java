package org.example.kviz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class KvizController {
    @FXML
    private Button startBTN, q1, q2, q3, q4;
    @FXML
    private TextField p1, p2;
    @FXML
    private Label nameError, question, currentPlayer;

    private final ArrayList<Question> questions1 = new ArrayList<>();
    private final ArrayList<Question> questions2 = new ArrayList<>();

    private Player player1;
    private Player player2;
    private int currentPlayerIndex = 1;
    private int solvedQuestionsPlayer1 = 0;
    private int solvedQuestionsPlayer2 = 0;

    @FXML
    public void initialize() {
        if (question == null || currentPlayer == null || q1 == null || q2 == null || q3 == null || q4 == null) {
            System.err.println("Error: FXML elements are not properly initialized. Check the FXML file and fx:id attributes.");
        }
    }

    @FXML
    public void onAnswerClick() {
        if (currentPlayerIndex == 1) {
            handleAnswer(solvedQuestionsPlayer1, questions1);
            solvedQuestionsPlayer1++;
            currentPlayerIndex = 2;
            currentPlayer.setText(player2.getName());
        } else {
            handleAnswer(solvedQuestionsPlayer2, questions2);
            solvedQuestionsPlayer2++;
            currentPlayerIndex = 1;
            currentPlayer.setText(player1.getName());
        }
        updateQuestion();
    }

    public void startQuiz(Player player1, Player player2, ArrayList<Question> questions1, ArrayList<Question> questions2) {
        this.player1 = player1;
        this.player2 = player2;
        this.questions1.addAll(questions1);
        this.questions2.addAll(questions2);
        currentPlayer.setText(player1.getName());
        updateQuestion();
    }

    private void handleAnswer(int questionIndex, ArrayList<Question> questions) {
        // Check if the answer is correct
        // Logic for updating player score can be added here
    }

    private void updateQuestion() {
        Question currentQuestion;
        if (currentPlayerIndex == 1 && solvedQuestionsPlayer1 < questions1.size()) {
            currentQuestion = questions1.get(solvedQuestionsPlayer1);
        } else if (currentPlayerIndex == 2 && solvedQuestionsPlayer2 < questions2.size()) {
            currentQuestion = questions2.get(solvedQuestionsPlayer2);
        } else {
            // End of the quiz logic
            endQuiz();
            return;
        }
        displayQuestion(currentQuestion);
    }

    private void displayQuestion(Question questionObj) {
        question.setText(questionObj.getQuestion());
        q1.setText(questionObj.getAnswers().get(0));
        q2.setText(questionObj.getAnswers().get(1));
        q3.setText(questionObj.getAnswers().get(2));
        q4.setText(questionObj.getAnswers().get(3));
    }

    private void endQuiz() {
        // Logic for ending the quiz (e.g., displaying scores)
        question.setText("Quiz vége!");
        q1.setDisable(true);
        q2.setDisable(true);
        q3.setDisable(true);
        q4.setDisable(true);
    }

    public void loadNextScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("questionsQuiz.fxml"));
        Parent root = fxmlLoader.load();
        KvizController controller = fxmlLoader.getController();

        controller.startQuiz(player1, player2, questions1, questions2);

        Scene scene = new Scene(root);
        Stage stage = (Stage) q1.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

//package org.example.kviz;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import java.util.*;
//import java.io.*;
//
//import java.io.IOException;
//
//public class KvizController {
//    @FXML
//    public Button startBTN, q1, q2, q3, q4;
//    public TextField p1, p2;
//    public Label nameError, question, currentPlayer;
//
//    public ArrayList<Question> questions1 = new ArrayList<>();
//    public ArrayList<Question> questions2 = new ArrayList<>();
//
//
//    @FXML
//    public void onStartBTNClick() throws IOException {
//        if (p1.getText().trim().length() < 3 || p1.getText().trim().length() > 12 || p2.getText().trim().length() < 3 || p2.getText().trim().length() > 12 || p1.getText().trim().equals(p2.getText().trim())) {
//            nameError.setTextFill(Color.RED);
//            nameError.setText("Nem megfelelőek a felhasználónevek");
//        } else {
//            nameError.setTextFill(Color.GREEN);
//            nameError.setText("Felhasználók elfogadva: " + p1.getText() + ", " + p2.getText());
//
//            loadNextScene();
//
//            try{
//                RandomAccessFile f1 = new RandomAccessFile("src/main/resources/data/questions_player_1.txt", "r");
//
//                String line = f1.readLine();
//                while (line != null) {
//                    String[] parts = line.split("\\|");
//                    String questionText = parts[0].split("=")[1];
//                    String[] answersArray = parts[1].split("=")[1].split(";");
//                    int correctAnswer = Integer.parseInt(parts[2].split("=")[1]);
//
//                    ArrayList<String> answers = new ArrayList<>();
//                    for (String answer : answersArray) {
//                        answers.add(answer);
//                    }
//
//                    questions1.add(new Question(questionText, answers, correctAnswer));
//                    line = f1.readLine();
//                }
//            }catch(Exception err){
//                System.err.println("ERROR: " + err);
//            }
//            try{
//                RandomAccessFile f2 = new RandomAccessFile("src/main/resources/data/questions_player_2.txt", "r");
//
//                String line = f2.readLine();
//                while (line != null) {
//                    String[] parts = line.split("\\|");
//                    String questionText = parts[0].split("=")[1];
//                    String[] answersArray = parts[1].split("=")[1].split(";");
//                    int correctAnswer = Integer.parseInt(parts[2].split("=")[1]);
//
//                    ArrayList<String> answers = new ArrayList<>();
//                    for (String answer : answersArray) {
//                        answers.add(answer);
//                    }
//
//                    questions2.add(new Question(questionText, answers, correctAnswer));
//                    line = f2.readLine();
//                }
//            }catch(Exception err){
//                System.err.println("ERROR: " + err);
//            }
//
//            Player player1 = new Player(p1.getText(), 0);
//            Player player2 = new Player(p2.getText(), 0);
//            int current_player = 1;
//            int solvedQuestionsPlayer1 = 0;
//            int solvedQuestionsPlayer2 = 0;
//
//                if(current_player == 1){
//                    currentPlayer.setText(player1.getName());
//
//                    for(int i = solvedQuestionsPlayer1; i < questions1.size() + questions2.size(); i++) {
//                        question.setText(questions1.get(i).getQuestion());
//                        q1.setText(questions1.get(i).getAnswers().get(0));
//                        q2.setText(questions1.get(i).getAnswers().get(1));
//                        q3.setText(questions1.get(i).getAnswers().get(2));
//                        q4.setText(questions1.get(i).getAnswers().get(3));
//
//
//
//
//
//                        solvedQuestionsPlayer1 = i;
//                    }
//                    current_player = 2;
//                }else{
//                    currentPlayer.setText(player2.getName());
//
//                    for(int ii = solvedQuestionsPlayer2; ii < questions1.size() + questions2.size(); ii++) {
//
//                        solvedQuestionsPlayer2 = ii;
//                    }
//
//                    current_player = 1;
//                }
//        }//quiz
//    }
//
//    public void loadNextScene() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("questionsQuiz.fxml"));
//        Parent root = fxmlLoader.load();
//
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) startBTN.getScene().getWindow();
//        stage.setScene(scene);
//        stage.show();
//    }
//}