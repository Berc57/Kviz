package org.example.kviz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class KerdesekController {
    @FXML
    public Button startBTN;
    public TextField p1, p2;
    public Label nameError;

    @FXML
    public void onStartBTNClick() {
        if (p1.getText().trim().length() <= 3 || p2.getText().trim().length() <= 3 || p1.getText().trim().equals(p2.getText().trim())) {
            nameError.setTextFill(Color.RED);
            nameError.setText("Nem megfelelőek a felhasználónevek");
        } else {
            nameError.setText("");
            nameError.setTextFill(Color.GREEN);
            nameError.setText("Felhasználók elfogadva: " + p1.getText() + ", " + p2.getText());
        }
    }
}