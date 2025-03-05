package com.lab;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Random;

public class easy extends Application {
    private static final int SIZE = 9; // ขนาด 9x9
    private static final int NUM_BOMBS = 10;
    private Button[][] buttons = new Button[SIZE][SIZE];
    private boolean[][] bombs = new boolean[SIZE][SIZE];
    private int[][] adjacentBombs = new int[SIZE][SIZE];
    private boolean[][] revealed = new boolean[SIZE][SIZE];
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

    
        double buttonSize = 900.0 / SIZE;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(buttonSize, buttonSize); 
                button.setFont(Font.font(16));
                final int r = row, c = col;
                button.setOnAction(e -> handleButtonClick(r, c));
                buttons[row][col] = button;
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
        stage.setTitle("Easy Minesweeper");
        stage.show();
    }

    private void handleButtonClick(int row, int col) {
        if (bombs[row][col]) {
            buttons[row][col].setText("B");
            buttons[row][col].setStyle("-fx-base: red;");
            showGameOverDialog(false);
        } else {
            revealCell(row, col);
            if (checkWin()) {
                showGameOverDialog(true);
            }
        }
    }

    private void revealCell(int row, int col) {
        if (revealed[row][col]) return;
        revealed[row][col] = true;
        buttons[row][col].setText(adjacentBombs[row][col] == 0 ? "" : String.valueOf(adjacentBombs[row][col]));
        buttons[row][col].setDisable(true);

        if (adjacentBombs[row][col] == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int r = row + i;
                    int c = col + j;
                    if (r >= 0 && r < SIZE && c >= 0 && c < SIZE) {
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
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (!bombs[row][col]) {
                bombs[row][col] = true;
                bombsPlaced++;
            }
        }
    }

    private void calculateAdjacentBombs() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (bombs[row][col]) {
                    continue;
                }
                int count = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int r = row + i;
                        int c = col + j;
                        if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && bombs[r][c]) {
                            count++;
                        }
                    }
                }
                adjacentBombs[row][col] = count;
            }
        }
    }

    private boolean checkWin() {
        int revealedCells = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!bombs[row][col] && revealed[row][col]) {
                    revealedCells++;
                }
            }
        }
        return revealedCells == (SIZE * SIZE - NUM_BOMBS);
    }

    private void showGameOverDialog(boolean won) {
        String message = won ? "You Win!" : "Game Over! You Lost!";
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(message);
        alert.setContentText("Do you want to play again?");
        ButtonType playAgainButton = new ButtonType("Play Again");
        ButtonType mainMenuButton = new ButtonType("Main Menu");

        alert.getButtonTypes().setAll(playAgainButton, mainMenuButton);
        alert.showAndWait().ifPresent(response -> {
            if (response == playAgainButton) {
                restartGame();
            } else if (response == mainMenuButton) {
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
        start(new Stage());
    }

    private void goToMainMenu() throws Exception {

        currentStage.close();
        new minesweeper().start(new Stage());
    }

}
