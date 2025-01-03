package org.example.kviz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class KerdesController {

    @FXML
    public Button q1, q2, q3, q4;
    @FXML
    public Label currentPlayer, question;

    public ArrayList<Question> questions1 = new ArrayList<>();
    public ArrayList<Question> questions2 = new ArrayList<>();
    public Player player1, player2;
    public int current_player = 1;
    public int solvedQuestionsPlayer1 = 0;
    public int solvedQuestionsPlayer2 = 0;
    public int tmpCorrect = -1;

    public void setPlayers(String player1Name, String player2Name) {
        player1 = new Player(player1Name, 0);
        player2 = new Player(player2Name, 0);
        initializeGame();
    }

    public void initializeGame() {
        try {
            loadFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        currentPlayer.setText(player1.getName());
        loadNextQuestion();
    }

    @FXML
    public void loadFiles() throws Exception {
        try (BufferedReader f1 = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/data/questions_player_1.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = f1.readLine()) != null) {
                String[] parts = line.split("\\|");
                String questionText = parts[0].split("=")[1];
                String[] answersArray = parts[1].split("=")[1].split(";");
                int correctAnswer = Integer.parseInt(parts[2].split("=")[1]);

                ArrayList<String> answers = new ArrayList<>();
                for (String answer : answersArray) {
                    answers.add(answer);
                }

                questions1.add(new Question(questionText, answers, correctAnswer));
            }
        }

        try (BufferedReader f2 = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/data/questions_player_2.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = f2.readLine()) != null) {
                String[] parts = line.split("\\|");
                String questionText = parts[0].split("=")[1];
                String[] answersArray = parts[1].split("=")[1].split(";");
                int correctAnswer = Integer.parseInt(parts[2].split("=")[1]);

                ArrayList<String> answers = new ArrayList<>();
                for (String answer : answersArray) {
                    answers.add(answer);
                }

                questions2.add(new Question(questionText, answers, correctAnswer));
            }
        }
    }

    @FXML
    public void loadNextQuestion() {
        if (current_player == 1) {
            if (solvedQuestionsPlayer1 >= questions1.size()) {
                return;
            }
            currentPlayer.setText(player1.getName());
            displayQuestion(questions1.get(solvedQuestionsPlayer1));
            tmpCorrect = questions1.get(solvedQuestionsPlayer1).getCorrect();
        } else {
            if (solvedQuestionsPlayer2 >= questions2.size()) {
                return;
            }
            currentPlayer.setText(player2.getName());
            displayQuestion(questions2.get(solvedQuestionsPlayer2));
            tmpCorrect = questions2.get(solvedQuestionsPlayer2).getCorrect();
        }
    }

    private void displayQuestion(Question questionObj) {
        question.setText(questionObj.getQuestion());
        q1.setText(questionObj.getAnswers().get(0));
        q2.setText(questionObj.getAnswers().get(1));
        q3.setText(questionObj.getAnswers().get(2));
        q4.setText(questionObj.getAnswers().get(3));
    }

    @FXML
    public void onAnswerClick1() {
        handleAnswer(0);
    }

    @FXML
    public void onAnswerClick2() {
        handleAnswer(1);
    }

    @FXML
    public void onAnswerClick3() {
        handleAnswer(2);
    }

    @FXML
    public void onAnswerClick4() {
        handleAnswer(3);
    }

    private void handleAnswer(int answerIndex) {
        if (tmpCorrect == answerIndex) {
            if (current_player == 1) {
                player1.setScore(player1.getScore() + 1);
                solvedQuestionsPlayer1++;
            } else {
                player2.setScore(player2.getScore() + 1);
                solvedQuestionsPlayer2++;
            }
        } else {
            if (current_player == 1) {
                solvedQuestionsPlayer1++;
            } else {
                solvedQuestionsPlayer2++;
            }
        }
        current_player = (current_player == 1) ? 2 : 1;
        loadNextQuestion();
    }
}