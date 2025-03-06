package com.lab;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;

import java.util.Random;

public class hard extends Application {
    private static final int SIZE_X = 30;
    private static final int SIZE_Y = 16;
    private static final int NUM_BOMBS = 99;
    private Button[][] buttons = new Button[SIZE_X][SIZE_Y];
    private boolean[][] bombs = new boolean[SIZE_X][SIZE_Y];
    private int[][] adjacentBombs = new int[SIZE_X][SIZE_Y];
    private boolean[][] revealed = new boolean[SIZE_X][SIZE_Y];
    private long startTime;
    private Text timerText;
    private Stage currentStage;

    @Override
    public void start(Stage stage) {
        currentStage = stage;
    
        AnchorPane root = new AnchorPane();
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
    
        
        double buttonWidth = 900.0 / SIZE_X;
        double buttonHeight = 600.0 / SIZE_Y;
        for (int row = 0; row < SIZE_Y; row++) {
            for (int col = 0; col < SIZE_X; col++) {
                Button button = new Button();
                button.setPrefSize(buttonWidth, buttonHeight); 
                button.setFont(Font.font(16));
                button.setStyle("-fx-background-color: #4a4a4a;-fx-text-fill: white;");
    
                final int r = row, c = col;
                button.setOnAction(e -> handleButtonClick(r, c));
                buttons[c][r] = button;
                grid.add(button, col, row);
            }
        }
    
        timerText = new Text("Time: 0");
        timerText.setX(10);
        timerText.setY(10);
    
        root.getChildren().addAll(grid, timerText);
    
        placeBombs();
        calculateAdjacentBombs();
    
        startTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = (now - startTime) / 1000000000;
                timerText.setText("Time: " + elapsedTime);
            }
        };
        timer.start();
    
        
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Hard Mode");
        stage.show();
    }
    

    private void handleButtonClick(int row, int col) {
        if (bombs[col][row]) {
            buttons[col][row].setText("B");
            buttons[col][row].setStyle("-fx-background-color: red; -fx-text-fill: red;");
            showGameOverDialog(false);
        } else {
            revealCell(row, col);
            if (checkWin()) {
                showGameOverDialog(true);
            }
        }
    }

    private void revealCell(int row, int col) {
        if (revealed[col][row]) return;
        revealed[col][row] = true;
        buttons[col][row].setText(adjacentBombs[col][row] == 0 ? "" : String.valueOf(adjacentBombs[col][row]));
        buttons[col][row].setDisable(true);
        if (adjacentBombs[col][row] == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int r = row + i;
                    int c = col + j;
                    if (r >= 0 && r < SIZE_Y && c >= 0 && c < SIZE_X) {
                        revealCell(r, c);
                    }
                }
            }
        }
    }

    private void placeBombs() {
        Random rand = new Random();
        int bombsPlaced = 0;
        while (bombsPlaced < NUM_BOMBS) {
            int row = rand.nextInt(SIZE_Y);
            int col = rand.nextInt(SIZE_X);
            if (!bombs[col][row]) {
                bombs[col][row] = true;
                bombsPlaced++;
            }
        }
    }

    private void calculateAdjacentBombs() {
        for (int row = 0; row < SIZE_Y; row++) {
            for (int col = 0; col < SIZE_X; col++) {
                if (bombs[col][row]) {
                    continue;
                }
                int count = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int r = row + i;
                        int c = col + j;
                        if (r >= 0 && r < SIZE_Y && c >= 0 && c < SIZE_X && bombs[c][r]) {
                            count++;
                        }
                    }
                }
                adjacentBombs[col][row] = count;
            }
        }
    }

    private boolean checkWin() {
        int revealedCells = 0;
        for (int row = 0; row < SIZE_Y; row++) {
            for (int col = 0; col < SIZE_X; col++) {
                if (!bombs[col][row] && revealed[col][row]) {
                    revealedCells++;
                }
            }
        }
        return revealedCells == (SIZE_X * SIZE_Y - NUM_BOMBS);
    }

    private void showGameOverDialog(boolean won) {
        String message = won ? "ชนะแล้วเก่งมากเลย" : "สมน้ำหน้าแพ้แล้ว5555";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(message);
        alert.setContentText("New Game");
        //ButtonType playAgainButton = new ButtonType("PlayAgain");
        ButtonType mainMenuButton = new ButtonType("NewGame");

        alert.getButtonTypes().setAll( mainMenuButton);
        alert.showAndWait().ifPresent(response -> {
            /*if (response == playAgainButton) {
                restartGame();
            }*/  if (response == mainMenuButton) {
                try {
                    goToMainMenu();
                } catch (Exception e) {

                    e.printStackTrace();
                }
            } else {
                System.exit(0);
            }
        });
    }

    private void restartGame() {
        currentStage.close();
        start(new Stage());
    }

    private void goToMainMenu() throws Exception {

        currentStage.close();
        new minesweeper();
    }

}
