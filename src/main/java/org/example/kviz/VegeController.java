package org.example.kviz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.*;

public class VegeController{

    @FXML
    public Label result1, result2;

    public void displayResults(Player player1, Player player2){
        result1.setStyle("-fx-text-fill: green;");
        if(player1.getScore() > player2.getScore()){
            result1.setText(player1.getName() + " nyert!");
        }else if(player2.getScore() > player1.getScore()){
            result1.setText(player2.getName() + " nyert!");
        }else{
            result1.setStyle("-fx-text-fill: white;");
            result1.setText("Döntetlen!");
        }
        result2.setText("Pontszámok: \n" + player1.getName() + " - " + player1.getScore() + ", \n" + player2.getName() + " - " + player2.getScore());
    }
}