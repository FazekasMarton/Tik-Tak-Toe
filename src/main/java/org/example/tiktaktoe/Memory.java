package org.example.tiktaktoe;

import java.util.ArrayList;

public class Memory {
    private final ArrayList<MemorySlice> goingFirst = new ArrayList<>();
    private final ArrayList<MemorySlice> goingSecond = new ArrayList<>();

    public ArrayList<MemorySlice> getGoingFirst() {
        return goingFirst;
    }

    public ArrayList<MemorySlice> getGoingSecond() {
        return goingSecond;
    }

    public void addGoingFirstMove(MemorySlice move) {
        goingFirst.add(move);
    }

    public void addGoingSecondMove(MemorySlice move) {
        goingSecond.add(move);
    }
}
