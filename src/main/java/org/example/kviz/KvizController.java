package org.example.kviz;

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


import java.awt.*;

import static java.awt.Color.*;

public class KvizController {
    @FXML
    public Button startBTN;
    public TextField p1, p2;
    public Label nameError;

    @FXML
    public void onStartBTNClick() throws IOException {
        if (p1.getText().trim().length() < 3 || p1.getText().trim().length() > 12 || p2.getText().trim().length() < 3 || p2.getText().trim().length() > 12 || p1.getText().trim().equals(p2.getText().trim())) {
            nameError.setTextFill(Color.RED);
            nameError.setText("Nem megfelelőek a felhasználónevek");
        } else {
            nameError.setTextFill(Color.GREEN);
            nameError.setText("Felhasználók elfogadva: " + p1.getText() + ", " + p2.getText());

            loadNextScene();
        }
    }
    public void loadNextScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("questionsQuiz.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) startBTN.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}