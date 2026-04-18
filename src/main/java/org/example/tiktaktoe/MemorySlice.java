package org.example.tiktaktoe;

import java.util.ArrayList;

public class MemorySlice {
    private final Move move;
    private final ArrayList<MemorySlice> nextMoves = new ArrayList<MemorySlice>();

    public MemorySlice(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    public ArrayList<MemorySlice> getNext() {
        return nextMoves;
    }

    public void addNext(MemorySlice next) {
        nextMoves.add(next);
    }

    public ArrayList<String> toStringArray() {
        ArrayList<String> strings = new ArrayList<>();

        if (nextMoves.isEmpty()) {
            strings.add(move == null ? "" : move.toString());
            return strings;
        }

        for (MemorySlice next : nextMoves) {
            for (String nextString : next.toStringArray()) {

                if (move == null) {
                    strings.add(nextString);
                } else {
                    strings.add(move + " -> " + nextString);
                }
            }
        }

        return strings;
    }
}
