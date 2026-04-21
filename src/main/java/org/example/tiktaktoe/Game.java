package org.example.tiktaktoe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.util.Random;

public class Game {
    @FXML
    private GridPane board;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Text percentText;

    private Character[][] table;
    private int round = 0;
    private Boolean isPlayerWin;
    private final AI ai = new AI();

    @FXML
    private void initialize() {
        newGame();
    }

    private void newGame() {
        for (Node node : board.getChildren()) {
            if (node instanceof Button button) {
                button.setText("");
            }
        }

        table = new Character[3][3];
        isPlayerWin = null;
        round = 0;
        showMemory();
        System.out.println("New Game");
        ai.printMemory();
        Random rand = new Random();
        if (rand.nextBoolean()) {
            try {
                ai.newGame(true);
                aiPlace(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ai.newGame(false);
        }
    }

    private void showMemory() {
        percentText.setText(ai.getMemoryPercent() + "%");
    }

    private void step(int row, int col, Character symbol) throws Exception {
        if (table[row][col] == null) {
            table[row][col] = symbol;
            getButton(row, col).setText(symbol.toString());
            round++;
        } else {
            throw new Exception("Cell already occupied!");
        }
    }

    private boolean isWin(int row, int col) {
        Character symbol = table[row][col];
        return isHorizontalWin(row, symbol) || isVerticalWin(col, symbol) || isDiagonalWin(row, col, symbol);
    }

    private boolean isHorizontalWin(int row, Character symbol) {
        boolean win = true;

        for (int i = 0; i < 3; i++) {
            if (table[row][i] != symbol) {
                win = false;
                break;
            }
        }

        return win;
    }

    private boolean isVerticalWin(int col, Character symbol) {
        boolean win = true;

        for (int i = 0; i < 3; i++) {
            if (table[i][col] != symbol) {
                win = false;
                break;
            }
        }

        return win;
    }

    private boolean isDiagonalWin(int row, int col, Character symbol) {
        return isDiagonalDownWin(row, col, symbol) || isDiagonalUpWin(row, col, symbol);
    }

    private boolean isDiagonalDownWin(int row, int col, Character symbol) {
        if (row - col == 0) {
            boolean win = true;
            for (int i = 0; i < 3; i++) {
                if (table[i][i] != symbol) {
                    win = false;
                    break;
                }
            }
            return win;
        }
        return false;
    }

    private boolean isDiagonalUpWin(int row, int col, Character symbol) {
        if (row + col == 2) {
            boolean win = true;
            for (int i = 0; i < 3; i++) {
                if (table[2 - i][i] != symbol) {
                    win = false;
                    break;
                }
            }
            return win;
        }
        return false;
    }

    @FXML
    private void startNewGame() {
        newGame();
    }

    @FXML
    private void place(ActionEvent event) {
        Button button = (Button) event.getSource();
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);

        try {
            playerPlace(row, col);
            Move playerLastMove = new Move(row, col);
            boolean isEnd = checkResult(playerLastMove);

            if (!isEnd) {
                aiPlace(row, col);
                checkResult(playerLastMove);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playerPlace(int row, int col) throws Exception {
        step(row, col, 'x');

        if (isWin(row, col)) {
            isPlayerWin = true;
        }
    }

    private void aiPlace(Integer pRow, Integer pCol) throws Exception {
        Move AIMove = ai.step(pRow, pCol, table);
        step(AIMove.getRow(), AIMove.getCol(), 'o');

        if (isWin(AIMove.row, AIMove.col)) {
            isPlayerWin = false;
        }
    }

    private boolean checkResult(Move lastPlayerMove) {
        boolean isEnd = false;
        if (isPlayerWin != null) {
            if (isPlayerWin) {
                ai.saveMemory(false, lastPlayerMove);
                showResultDialog("Nyertél!");
                isEnd = true;
            } else {
                ai.saveMemory(true, lastPlayerMove);
                showResultDialog("Vesztettél!");
                isEnd = true;
            }
        }
        if (round >= 9) {
            showResultDialog("Döntetlen!");
            isEnd = true;
        }
        return isEnd;
    }

    private void showResultDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Játék vége");
        alert.setHeaderText(message);
        alert.setContentText("Szeretnél új játékot?");

        ButtonType yes = new ButtonType("Igen");
        ButtonType no = new ButtonType("Nem");

        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(response -> {
            if (response == yes) {
                startNewGame();
            } else {
                System.exit(0);
            }
        });
    }

    private Button getButton(int row, int col) {
        for (Node node : board.getChildren()) {

            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);

            if (r == null) r = 0;
            if (c == null) c = 0;

            if (node instanceof Button && r == row && c == col) {
                return (Button) node;
            }
        }
        return null;
    }
}