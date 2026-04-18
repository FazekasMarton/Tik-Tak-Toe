package org.example.tiktaktoe;

public class Move {
    public int row;
    public int col;

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Move m) {
        return this.row == m.row && this.col == m.col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "(" + this.row + "-" + this.col + ")";
    }
}
