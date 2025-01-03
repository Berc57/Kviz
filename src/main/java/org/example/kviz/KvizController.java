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

public class KvizController{

    @FXML
    public Button startBTN;
    @FXML
    public TextField p1, p2;
    @FXML
    public Label nameError;

    @FXML
    public void onStartBTNClick() throws Exception{
        if(p1.getText().trim().length() < 3 || p1.getText().trim().length() > 12 || p2.getText().trim().length() < 3 || p2.getText().trim().length() > 12 || p1.getText().trim().equals(p2.getText().trim())){
            nameError.setTextFill(Color.RED);
            nameError.setText("Nem megfelelőek a felhasználónevek");
        }else{
            nameError.setTextFill(Color.GREEN);
            nameError.setText("Felhasználók elfogadva: " + p1.getText() + ", " + p2.getText());

            loadNextScene(p1.getText(), p2.getText());
        }
    }

    @FXML
    public void loadNextScene(String player1Name, String player2Name) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("questionsQuiz.fxml"));
        Parent root = fxmlLoader.load();

        KerdesController controller = fxmlLoader.getController();
        controller.setPlayers(player1Name, player2Name);

        Scene scene = new Scene(root);
        Stage stage = (Stage) startBTN.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}