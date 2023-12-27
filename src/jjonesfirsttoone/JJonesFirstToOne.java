/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jjonesfirsttoone;

import java.util.Random;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author Thema
 */
public class JJonesFirstToOne extends Application {
    
    final Random random = new Random();
    final int startingPoints = 50;
    final int goalPoints = 1;
    final int maxRoll = 6;

    int player1Rolls = 0;
    int player2Rolls = 0;
    int player1Score = startingPoints;
    int player2Score = startingPoints;
    int maxLogLines = 6;
    int maxTurnCount = 10;
    int currentTurn = 1;

    boolean isPlayer1Turn = true;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        // Initializes the layout of the scene
        BorderPane layout = new BorderPane();
        Scene scene = new Scene(layout, 400, 400);
        
        // Sets the properties of the window
        primaryStage.setTitle("First To One");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(scene.getWidth());
        primaryStage.setWidth(scene.getWidth());
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setHeight(scene.getHeight());
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // Initializes the label that tells the user how to player the game, and sets it's properties
        Label infoLabel = new Label("In this game, each player will roll a " + maxRoll + "-sided die, until their score reaches exactly one. If their score would go under one, then their roll would be added to their score instead. If there is a trun limit, whichever player has the lower score wins!");
        infoLabel.setTextAlignment(TextAlignment.CENTER);
        infoLabel.setWrapText(true);
        
        // Initializes the button that starts the game
        Button startButton = new Button("Start");
        
        // Initializes the check box that sets if the game will have a turn limit
        CheckBox turnLimitEnabled = new CheckBox("Enable Turn Limit");
        
        // Initializes the label that notes the turn count
        Label turnLabel = new Label("Turn Count:");
        
        // Initializes the field that holds the turn limit for the game and it's properties
        TextField turnField = new TextField(maxTurnCount + "");
        turnField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    turnField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        // Initializes the button that sets the turn limit
        Button turnSetButton = new Button("Set");
        turnSetButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(!turnField.getText().isEmpty() && turnField.getText().matches("[0-9]+")){
                    maxTurnCount = Integer.parseInt(turnField.getText());
                }
            }
        });
        
        // Groups the turn elements into a row, and sets the row's properties
        HBox turnRow = new HBox(turnLabel, turnField, turnSetButton);
        turnRow.setAlignment(Pos.CENTER);
        turnRow.setPadding(new Insets(10));
        turnRow.setSpacing(5);
        turnRow.disableProperty().bind(turnLimitEnabled.selectedProperty().not().or(turnLimitEnabled.disableProperty()));
        
        // Groups the game setting elements into a column, and sets the column's properties
        VBox infoCol = new VBox(infoLabel, startButton, turnLimitEnabled, turnRow);
        infoCol.setAlignment(Pos.CENTER);
        infoCol.setPadding(new Insets(10));
        infoCol.setSpacing(5);
        
        layout.setTop(infoCol);
        
        // Initializes the labels and column that shows the first player's score, and their properties
        Label player1Label = new Label("Player 1");
        Label player1ScoreLabel = new Label("Score: " + player1Score);
        VBox player1Col = new VBox(player1Label, player1ScoreLabel);
        player1Col.setAlignment(Pos.CENTER);
        player1Col.setPadding(new Insets(10));
        player1Col.setSpacing(5);
        layout.setLeft(player1Col);
        
        // Initializes the labels and column that shows the second player's score, and their properties
        Label player2Label = new Label("Player 2");
        Label player2ScoreLabel = new Label("Score: " + player2Score);
        VBox player2Col = new VBox(player2Label, player2ScoreLabel);
        player2Col.setAlignment(Pos.CENTER);
        player2Col.setPadding(new Insets(10));
        player2Col.setSpacing(5);
        layout.setRight(player2Col);
        
        // Initializes the label that shows the results of each turn for the players, and it's properties
        Label rollLog = new Label();
        rollLog.setTextAlignment(TextAlignment.CENTER);
        rollLog.setAlignment(Pos.CENTER);
        rollLog.maxWidthProperty().bind(primaryStage.widthProperty().divide(2));
        rollLog.setMaxHeight(300);
        rollLog.setPrefWidth(rollLog.getMaxWidth());
        rollLog.setPrefHeight(rollLog.getMaxHeight());
        rollLog.setWrapText(true);
        
        // Initializes the button that performs the roll for each player and it's properties
        Button rollButton = new Button("Roll");
        rollButton.setDisable(true);
        rollButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                // The roll for the turn
                int roll = random.nextInt(maxRoll) + 1;
                
                // If the game is turn limited, show the total amount of turns alongside the current turn number
                if(turnLimitEnabled.isSelected()){
                    rollLog.setText(rollLog.getText() + "\nTurn " + currentTurn + " of " + maxTurnCount + ": ");
                } 
                // Otherwise, just show the current turn number
                else {
                    rollLog.setText(rollLog.getText() + "\nTurn " + currentTurn + ": ");
                }
                
                // If it is the first player's turn, then update the first player's score
                if(isPlayer1Turn){
                    if(player1Score - roll < goalPoints){
                        player1Score+=roll;
                    } else {
                        player1Score-=roll;
                    }
                    player1ScoreLabel.setText("Score: " + player1Score);
                    if(rollLog.getText().split("\n").length >= maxLogLines){
                        rollLog.setText(rollLog.getText().substring(rollLog.getText().indexOf("\n") + 1));
                    }
                    rollLog.setText(rollLog.getText() + "Player 1 rolled a " + roll);
                } 
                // Otherwise, update the second player's score
                else {
                    if(player2Score - roll < goalPoints){
                        player2Score += roll;
                    } else {
                        player2Score -= roll;
                    }
                    player2ScoreLabel.setText("Score: " + player2Score);
                    if(rollLog.getText().split("\n").length >= maxLogLines){
                        rollLog.setText(rollLog.getText().substring(rollLog.getText().indexOf("\n") + 1));
                    }
                    rollLog.setText(rollLog.getText() + "Player 2 rolled a " + roll);
                }
                
                // Updates the current turn
                currentTurn++;
                
                // If the game is turn limited and the limit has been reached, determine and show the winner and reset the game
                if (turnLimitEnabled.isSelected() && currentTurn > maxTurnCount){
                    rollLog.setText(rollLog.getText() + "\nTurn Limit Reached!");
                    if(player1Score < player2Score){
                        rollLog.setText(rollLog.getText() + "\nPlayer 1 wins!");
                    } else if (player2Score < player1Score){
                        rollLog.setText(rollLog.getText() + "\nPlayer 2 wins!");
                    } else {
                        rollLog.setText(rollLog.getText() + "\nIt's a tie!");
                    }
                    startButton.setDisable(false);
                    turnLimitEnabled.setDisable(false);
                    rollButton.setDisable(true);
                }
                // Otherwise, if either player reaches one, then show the winner and reset the game
                else if (player1Score == goalPoints || player2Score == goalPoints){
                    if(player1Score == goalPoints){
                        rollLog.setText(rollLog.getText() + "\nPlayer 1 wins!");
                    } else if(player2Score == goalPoints){
                        rollLog.setText(rollLog.getText() + "\nPlayer 2 wins!");
                    }
                    startButton.setDisable(false);
                    turnLimitEnabled.setDisable(false);
                    rollButton.setDisable(true);
                }
                isPlayer1Turn = !isPlayer1Turn;
            }
        });
        
        // Starts the game when the start button is pressed
        startButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                startButton.setDisable(true);
                turnLimitEnabled.setDisable(true);
                rollButton.setDisable(false);
                rollLog.setText("");
                currentTurn = 1;
                player1Score = startingPoints;
                player2Score = startingPoints;
                player1ScoreLabel.setText("Score: " + player1Score);
                player2ScoreLabel.setText("Score: " + player2Score);
            }
        });
        
        // Groups the roll elements into a column and sets the column's properties
        VBox rollCol = new VBox(rollLog, rollButton);
        rollCol.setAlignment(Pos.CENTER);
        rollCol.setPadding(new Insets(10));
        rollCol.setSpacing(5);
        
        layout.setCenter(rollCol);
    }
}
