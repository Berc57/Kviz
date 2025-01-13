package org.example.kviz;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

public class KerdesController {

    @FXML
    public Button q1, q2, q3, q4, felezesButton, kozonsegSzavazButton, telefonButton;
    @FXML
    public Label currentPlayer, question;

    public ArrayList<Question> questions1 = new ArrayList<>();
    public ArrayList<Question> questions2 = new ArrayList<>();
    public Player player1, player2;
    public int current_player = 1;
    public int solvedQuestionsPlayer1 = 0;
    public int solvedQuestionsPlayer2 = 0;

    public int tmpCorrect = -1;

    private boolean felezesUsedPlayer1 = false;
    private boolean felezesUsedPlayer2 = false;
    private boolean kozonsegSzavazasUsedPlayer1 = false;
    private boolean kozonsegSzavazasUsedPlayer2 = false;
    private boolean telefonUsedPlayer1 = false;
    private boolean telefonUsedPlayer2 = false;

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
                Objects.requireNonNull(getClass().getResourceAsStream("/data/questions_player_1.txt")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = f1.readLine()) != null) {
                String[] parts = line.split("\\|");
                String questionText = parts[0].split("=")[1];
                String[] answersArray = parts[1].split("=")[1].split(";");
                int correctAnswer = Integer.parseInt(parts[2].split("=")[1]);

                ArrayList<String> answers = new ArrayList<>();
                Collections.addAll(answers, answersArray);

                questions1.add(new Question(questionText, answers, correctAnswer));
            }
        }

        try (BufferedReader f2 = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/data/questions_player_2.txt")), StandardCharsets.UTF_8))) {
            String line;
            while ((line = f2.readLine()) != null) {
                String[] parts = line.split("\\|");
                String questionText = parts[0].split("=")[1];
                String[] answersArray = parts[1].split("=")[1].split(";");
                int correctAnswer = Integer.parseInt(parts[2].split("=")[1]);

                ArrayList<String> answers = new ArrayList<>(Arrays.asList(answersArray));

                questions2.add(new Question(questionText, answers, correctAnswer));
            }
        }
    }

    @FXML
    public void loadNextQuestion() {
        if (current_player == 1) {
            if (solvedQuestionsPlayer1 >= questions1.size()) {
                displayResults();
                return;
            }
            currentPlayer.setText(player1.getName());
            displayQuestion(questions1.get(solvedQuestionsPlayer1));
            tmpCorrect = questions1.get(solvedQuestionsPlayer1).getCorrect();
        } else {
            if (solvedQuestionsPlayer2 >= questions2.size()) {
                displayResults();
                return;
            }
            currentPlayer.setText(player2.getName());
            displayQuestion(questions2.get(solvedQuestionsPlayer2));
            tmpCorrect = questions2.get(solvedQuestionsPlayer2).getCorrect();
        }
        updateFelezesButtonState();
        updateKozonsegSzavazasButtonState();
        updateTelefonButtonState();
    }

    private void displayQuestion(Question questionObj) {
        question.setText(questionObj.getQuestion());
        q1.setText(questionObj.getAnswers().get(0));
        q2.setText(questionObj.getAnswers().get(1));
        q3.setText(questionObj.getAnswers().get(2));
        q4.setText(questionObj.getAnswers().get(3));
        resetButtonStyles();
    }

    private void resetButtonStyles() {
        q1.setStyle("");
        q2.setStyle("");
        q3.setStyle("");
        q4.setStyle("");
        q1.setDisable(false);
        q2.setDisable(false);
        q3.setDisable(false);
        q4.setDisable(false);
    }

    @FXML
    public void onKozonsegSzavazas() {
        if (current_player == 1 && !kozonsegSzavazasUsedPlayer1) {
            kozonsegSzavazasUsedPlayer1 = true;
        } else if (current_player == 2 && !kozonsegSzavazasUsedPlayer2) {
            kozonsegSzavazasUsedPlayer2 = true;
        } else {
            return;
        }

        kozonsegSzavazButton.setDisable(true);

        Random rand = new Random();
        int correctPercent = 50 + rand.nextInt(31);
        int remainingPercent = 100 - correctPercent;

        int[] otherPercents = new int[3];
        for (int i = 0; i < 2; i++) {
            otherPercents[i] = rand.nextInt(remainingPercent + 1);
            remainingPercent -= otherPercents[i];
        }
        otherPercents[2] = remainingPercent;

        List<Integer> indices = Arrays.asList(0, 1, 2);
        Collections.shuffle(indices);
        int[] finalPercents = new int[4];
        finalPercents[tmpCorrect] = correctPercent;

        for (int i = 0, j = 0; i < 4; i++) {
            if (i != tmpCorrect) {
                finalPercents[i] = otherPercents[indices.get(j++)];
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Közönség szavazás");
        alert.setHeaderText("A közönség szavazatai:");
        alert.setContentText(
                "A: " + finalPercents[0] + "%\n" +
                        "B: " + finalPercents[1] + "%\n" +
                        "C: " + finalPercents[2] + "%\n" +
                        "D: " + finalPercents[3] + "%"
        );
        alert.showAndWait();
    }

    @FXML
    public void onTelefon() {
        if (current_player == 1 && !telefonUsedPlayer1) {
            telefonUsedPlayer1 = true;
        } else if (current_player == 2 && !telefonUsedPlayer2) {
            telefonUsedPlayer2 = true;
        } else {
            return;
        }

        telefonButton.setDisable(true);

        Stage alertStage = new Stage();
        alertStage.setTitle("Telefonos segítség");

        Label contentLabel = new Label("30 másodperc");
        contentLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #333;");
        contentLabel.setWrapText(true);


        VBox vbox = new VBox(10, contentLabel);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f0f4f8; -fx-border-color: #0078d7; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Scene scene = new Scene(vbox);
        alertStage.setScene(scene);
        alertStage.show();

        alertStage.setWidth(400);
        alertStage.setHeight(300);

        new Thread(() -> {
            try {
                for (int i = 30; i > 0; i--) {
                    int finalI = i;
                    Platform.runLater(() -> contentLabel.setText(finalI + " másodperc"));
                    Thread.sleep(1000);
                }
                Platform.runLater(() -> {
                    contentLabel.setText("Az idő lejárt!");
                    vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #ffcccc; -fx-border-color: #d9534f; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
                });
                Thread.sleep(1000);
                Platform.runLater(alertStage::close);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateFelezesButtonState() {
        if (current_player == 1) {
            felezesButton.setDisable(felezesUsedPlayer1);
        } else {
            felezesButton.setDisable(felezesUsedPlayer2);
        }
    }

    private void updateKozonsegSzavazasButtonState() {
        if (current_player == 1) {
            kozonsegSzavazButton.setDisable(kozonsegSzavazasUsedPlayer1);
        } else {
            kozonsegSzavazButton.setDisable(kozonsegSzavazasUsedPlayer2);
        }
    }

    private void updateTelefonButtonState() {
        if (current_player == 1) {
            telefonButton.setDisable(telefonUsedPlayer1);
        } else {
            telefonButton.setDisable(telefonUsedPlayer2);
        }
    }

    @FXML
    public void onAnswerClick1() {
        handleAnswer(0, q1);
    }

    @FXML
    public void onAnswerClick2() {
        handleAnswer(1, q2);
    }

    @FXML
    public void onAnswerClick3() {
        handleAnswer(2, q3);
    }

    @FXML
    public void onAnswerClick4() {
        handleAnswer(3, q4);
    }

    private void handleAnswer(int answerIndex, Button clickedButton) {
        if (tmpCorrect == answerIndex) {
            clickedButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            if (current_player == 1) {
                player1.setScore(player1.getScore() + 1);
                solvedQuestionsPlayer1++;
            } else {
                player2.setScore(player2.getScore() + 1);
                solvedQuestionsPlayer2++;
            }
        } else {
            clickedButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            Button correctButton = getCorrectButton(tmpCorrect);
            correctButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            if (current_player == 1) {
                solvedQuestionsPlayer1++;
            } else {
                solvedQuestionsPlayer2++;
            }
        }
        current_player = (current_player == 1) ? 2 : 1;

        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(this::loadNextQuestion);
        }).start();
    }

    private Button getCorrectButton(int correctIndex) {
        return switch (correctIndex) {
            case 0 -> q1;
            case 1 -> q2;
            case 2 -> q3;
            case 3 -> q4;
            default -> throw new IllegalArgumentException("Invalid correct answer index");
        };
    }

    @FXML
    public void onFelezesClick() {
        if (current_player == 1 && !felezesUsedPlayer1) {
            felezesUsedPlayer1 = true;
            felezesButton.setDisable(true);
        } else if (current_player == 2 && !felezesUsedPlayer2) {
            felezesUsedPlayer2 = true;
            felezesButton.setDisable(true);
        } else {
            return;
        }

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != tmpCorrect) {
                indices.add(i);
            }
        }
        Random rand = new Random();
        for (int i = 0; i < 2; i++) {
            int index = indices.remove(rand.nextInt(indices.size()));
            getButtonByIndex(index).setStyle("-fx-background-color: red;");
            getButtonByIndex(index).setDisable(true);
        }
    }

    private Button getButtonByIndex(int index) {
        return switch (index) {
            case 0 -> q1;
            case 1 -> q2;
            case 2 -> q3;
            case 3 -> q4;
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        };
    }

    private void displayResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("endQuiz.fxml"));
            Parent root = loader.load();

            VegeController vegeController = loader.getController();
            vegeController.displayResults(player1, player2);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz Végeredmény");
            stage.show();

            Stage currentStage = (Stage) currentPlayer.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}