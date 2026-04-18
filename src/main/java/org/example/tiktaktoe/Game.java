package org.example.tiktaktoe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;

public class Game {
    @FXML
    private GridPane board;

    private Character[][] table = new Character[3][3];
    private MI mi = new MI();

    public void newGame() {
        table = new Character[3][3];
    }

    public void step(int row, int col, Character symbol) throws Exception {
        if (table[row][col] == null) {
            table[row][col] = symbol;
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

    private boolean isDraw() {
        boolean draw = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (table[i][j] == null) {
                    draw = false;
                    break;
                }
            }
        }
        return draw;
    }

    @FXML
    private void startNewGame() {
        newGame();
        for (Node node : board.getChildren()) {
            if (node instanceof Button button) {
                button.setText("");
            }
        }
    }

    @FXML
    private void place(ActionEvent event) {
        try {
            Button button = (Button) event.getSource();
            int row = GridPane.getRowIndex(button);
            int col = GridPane.getColumnIndex(button);
            step(row, col, 'x');
            button.setText("x");
            if (isWin(row, col)) {
                showResultDialog("Nyertél!");
                return;
            }

            if(isDraw()) {
                showResultDialog("Döntetlen!");
                return;
            }

            Move MIMove = mi.step(table);
            step(MIMove.getRow(), MIMove.getCol(), 'o');
            getButton(MIMove.getRow(), MIMove.getCol()).setText("o");
            if (isWin(MIMove.row, MIMove.col)) {
                showResultDialog("Vesztettél!");
            }

            if(isDraw()) showResultDialog("Döntetlen!");
        } catch (Exception e) {
            e.printStackTrace();
        }
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