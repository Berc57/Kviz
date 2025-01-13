package org.example.kviz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KerdesController {
    @FXML
    public Button q1;
    @FXML
    public Button q2;
    @FXML
    public Button q3;
    @FXML
    public Button q4;
    @FXML
    public Button felezesButton;
    @FXML
    public Button kozonsegSzavazButton;
    @FXML
    public Button telefonButton;
    @FXML
    public Label currentPlayer;
    @FXML
    public Label question;
    public ArrayList<Question> questions1 = new ArrayList();
    public ArrayList<Question> questions2 = new ArrayList();
    public Player player1;
    public Player player2;
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

    public KerdesController() {
    }

    public void setPlayers(String player1Name, String player2Name) {
        this.player1 = new Player(player1Name, 0);
        this.player2 = new Player(player2Name, 0);
        this.initializeGame();
    }

    public void initializeGame() {
        try {
            this.loadFiles();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }

        this.currentPlayer.setText(this.player1.getName());
        this.loadNextQuestion();
    }

    @FXML
    public void loadFiles() throws Exception {
        BufferedReader f2 = new BufferedReader(new InputStreamReader((InputStream)Objects.requireNonNull(this.getClass().getResourceAsStream("/data/questions_player_1.txt")), StandardCharsets.UTF_8));

        String line;
        String[] parts;
        String questionText;
        String[] answersArray;
        int correctAnswer;
        ArrayList answers;
        try {
            while((line = f2.readLine()) != null) {
                parts = line.split("\\|");
                questionText = parts[0].split("=")[1];
                answersArray = parts[1].split("=")[1].split(";");
                correctAnswer = Integer.parseInt(parts[2].split("=")[1]);
                answers = new ArrayList();
                Collections.addAll(answers, answersArray);
                this.questions1.add(new Question(questionText, answers, correctAnswer));
            }
        } catch (Throwable var11) {
            try {
                f2.close();
            } catch (Throwable var9) {
                var11.addSuppressed(var9);
            }

            throw var11;
        }

        f2.close();
        f2 = new BufferedReader(new InputStreamReader((InputStream)Objects.requireNonNull(this.getClass().getResourceAsStream("/data/questions_player_2.txt")), StandardCharsets.UTF_8));

        try {
            while((line = f2.readLine()) != null) {
                parts = line.split("\\|");
                questionText = parts[0].split("=")[1];
                answersArray = parts[1].split("=")[1].split(";");
                correctAnswer = Integer.parseInt(parts[2].split("=")[1]);
                answers = new ArrayList(Arrays.asList(answersArray));
                this.questions2.add(new Question(questionText, answers, correctAnswer));
            }
        } catch (Throwable var10) {
            try {
                f2.close();
            } catch (Throwable var8) {
                var10.addSuppressed(var8);
            }

            throw var10;
        }

        f2.close();
    }

    @FXML
    public void loadNextQuestion() {
        if (this.current_player == 1) {
            if (this.solvedQuestionsPlayer1 >= this.questions1.size()) {
                this.displayResults();
                return;
            }

            this.currentPlayer.setText(this.player1.getName());
            this.displayQuestion((Question)this.questions1.get(this.solvedQuestionsPlayer1));
            this.tmpCorrect = ((Question)this.questions1.get(this.solvedQuestionsPlayer1)).getCorrect();
        } else {
            if (this.solvedQuestionsPlayer2 >= this.questions2.size()) {
                this.displayResults();
                return;
            }

            this.currentPlayer.setText(this.player2.getName());
            this.displayQuestion((Question)this.questions2.get(this.solvedQuestionsPlayer2));
            this.tmpCorrect = ((Question)this.questions2.get(this.solvedQuestionsPlayer2)).getCorrect();
        }

        this.updateFelezesButtonState();
        this.updateKozonsegSzavazasButtonState();
        this.updateTelefonButtonState();
    }

    private void displayQuestion(Question questionObj) {
        this.question.setText(questionObj.getQuestion());
        this.q1.setText((String)questionObj.getAnswers().get(0));
        this.q2.setText((String)questionObj.getAnswers().get(1));
        this.q3.setText((String)questionObj.getAnswers().get(2));
        this.q4.setText((String)questionObj.getAnswers().get(3));
        this.resetButtonStyles();
    }

    private void resetButtonStyles() {
        this.q1.setStyle("");
        this.q2.setStyle("");
        this.q3.setStyle("");
        this.q4.setStyle("");
        this.q1.setDisable(false);
        this.q2.setDisable(false);
        this.q3.setDisable(false);
        this.q4.setDisable(false);
    }

    @FXML
    public void onKozonsegSzavazas() {
        if (this.current_player == 1 && !this.kozonsegSzavazasUsedPlayer1) {
            this.kozonsegSzavazasUsedPlayer1 = true;
        } else {
            if (this.current_player != 2 || this.kozonsegSzavazasUsedPlayer2) {
                return;
            }

            this.kozonsegSzavazasUsedPlayer2 = true;
        }

        this.kozonsegSzavazButton.setDisable(true);
        Random rand = new Random();
        int correctPercent = 50 + rand.nextInt(31);
        int remainingPercent = 100 - correctPercent;
        int[] otherPercents = new int[3];

        for(int i = 0; i < 2; ++i) {
            otherPercents[i] = rand.nextInt(remainingPercent + 1);
            remainingPercent -= otherPercents[i];
        }

        otherPercents[2] = remainingPercent;
        List<Integer> indices = Arrays.asList(0, 1, 2);
        Collections.shuffle(indices);
        int[] finalPercents = new int[4];
        finalPercents[this.tmpCorrect] = correctPercent;
        int i = 0;

        for(int j = 0; i < 4; ++i) {
            if (i != this.tmpCorrect) {
                finalPercents[i] = otherPercents[(Integer)indices.get(j++)];
            }
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Közönség szavazás");
        alert.setHeaderText("A közönség szavazatai:");
        alert.setContentText("1: " + finalPercents[0] + "%\n2: " + finalPercents[1] + "%\n3: " + finalPercents[2] + "%\n4: " + finalPercents[3] + "%");
        alert.showAndWait();
    }

    @FXML
    public void onTelefon() {
        if (this.current_player == 1 && !this.telefonUsedPlayer1) {
            this.telefonUsedPlayer1 = true;
        } else {
            if (this.current_player != 2 || this.telefonUsedPlayer2) {
                return;
            }

            this.telefonUsedPlayer2 = true;
        }

        this.telefonButton.setDisable(true);
        Stage alertStage = new Stage();
        alertStage.setTitle("Telefonos segítség");
        Label contentLabel = new Label("30 másodperc");
        contentLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #333;");
        contentLabel.setWrapText(true);
        VBox vbox = new VBox(10.0, new Node[]{contentLabel});
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f0f4f8; -fx-border-color: #0078d7; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        Scene scene = new Scene(vbox);
        alertStage.setScene(scene);
        alertStage.show();
        alertStage.setWidth(400.0);
        alertStage.setHeight(300.0);
        (new Thread(() -> {
            try {
                for(int i = 30; i > 0; --i) {
                    Platform.runLater(() -> {
                        contentLabel.setText("" + i + " másodperc");
                    });
                    Thread.sleep(1000L);
                }

                Platform.runLater(() -> {
                    contentLabel.setText("Az idő lejárt!");
                    vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #ffcccc; -fx-border-color: #d9534f; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
                });
                Thread.sleep(1000L);
                Objects.requireNonNull(alertStage);
                Platform.runLater(alertStage::close);
            } catch (InterruptedException var5) {
                var5.printStackTrace();
            }

        })).start();
    }

    private void updateFelezesButtonState() {
        if (this.current_player == 1) {
            this.felezesButton.setDisable(this.felezesUsedPlayer1);
        } else {
            this.felezesButton.setDisable(this.felezesUsedPlayer2);
        }

    }

    private void updateKozonsegSzavazasButtonState() {
        if (this.current_player == 1) {
            this.kozonsegSzavazButton.setDisable(this.kozonsegSzavazasUsedPlayer1);
        } else {
            this.kozonsegSzavazButton.setDisable(this.kozonsegSzavazasUsedPlayer2);
        }

    }

    private void updateTelefonButtonState() {
        if (this.current_player == 1) {
            this.telefonButton.setDisable(this.telefonUsedPlayer1);
        } else {
            this.telefonButton.setDisable(this.telefonUsedPlayer2);
        }

    }

    @FXML
    public void onAnswerClick1() {
        this.handleAnswer(0, this.q1);
    }

    @FXML
    public void onAnswerClick2() {
        this.handleAnswer(1, this.q2);
    }

    @FXML
    public void onAnswerClick3() {
        this.handleAnswer(2, this.q3);
    }

    @FXML
    public void onAnswerClick4() {
        this.handleAnswer(3, this.q4);
    }

    private void handleAnswer(int answerIndex, Button clickedButton) {
        if (this.tmpCorrect == answerIndex) {
            clickedButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            if (this.current_player == 1) {
                this.player1.setScore(this.player1.getScore() + 1);
                ++this.solvedQuestionsPlayer1;
            } else {
                this.player2.setScore(this.player2.getScore() + 1);
                ++this.solvedQuestionsPlayer2;
            }
        } else {
            clickedButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            Button correctButton = this.getCorrectButton(this.tmpCorrect);
            correctButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            if (this.current_player == 1) {
                ++this.solvedQuestionsPlayer1;
            } else {
                ++this.solvedQuestionsPlayer2;
            }
        }

        this.current_player = this.current_player == 1 ? 2 : 1;
        (new Thread(() -> {
            try {
                Thread.sleep(1500L);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }

            Platform.runLater(this::loadNextQuestion);
        })).start();
    }

    private Button getCorrectButton(int correctIndex) {
        Button var10000;
        switch (correctIndex) {
            case 0:
                var10000 = this.q1;
                break;
            case 1:
                var10000 = this.q2;
                break;
            case 2:
                var10000 = this.q3;
                break;
            case 3:
                var10000 = this.q4;
                break;
            default:
                throw new IllegalArgumentException("Invalid correct answer index");
        }

        return var10000;
    }

    @FXML
    public void onFelezesClick() {
        if (this.current_player == 1 && !this.felezesUsedPlayer1) {
            this.felezesUsedPlayer1 = true;
            this.felezesButton.setDisable(true);
        } else {
            if (this.current_player != 2 || this.felezesUsedPlayer2) {
                return;
            }

            this.felezesUsedPlayer2 = true;
            this.felezesButton.setDisable(true);
        }

        ArrayList<Integer> indices = new ArrayList();

        for(int i = 0; i < 4; ++i) {
            if (i != this.tmpCorrect) {
                indices.add(i);
            }
        }

        Random rand = new Random();

        for(int i = 0; i < 2; ++i) {
            int index = (Integer)indices.remove(rand.nextInt(indices.size()));
            this.getButtonByIndex(index).setStyle("-fx-background-color: red;");
            this.getButtonByIndex(index).setDisable(true);
        }

    }

    private Button getButtonByIndex(int index) {
        Button var10000;
        switch (index) {
            case 0:
                var10000 = this.q1;
                break;
            case 1:
                var10000 = this.q2;
                break;
            case 2:
                var10000 = this.q3;
                break;
            case 3:
                var10000 = this.q4;
                break;
            default:
                throw new IllegalArgumentException("Invalid index: " + index);
        }

        return var10000;
    }

    private void displayResults() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("endQuiz.fxml"));
            Parent root = (Parent)loader.load();
            VegeController vegeController = (VegeController)loader.getController();
            vegeController.displayResults(this.player1, this.player2);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz Végeredmény");
            stage.show();
            Stage currentStage = (Stage)this.currentPlayer.getScene().getWindow();
            currentStage.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }
}