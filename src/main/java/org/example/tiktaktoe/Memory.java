package org.example.tiktaktoe;

import java.util.ArrayList;

public class Memory {
    private final ArrayList<MemorySlice> goingFirst = new ArrayList<>();
    private final ArrayList<MemorySlice> goingSecond = new ArrayList<>();

    public ArrayList<MemorySlice> getGoingFirst() {
        ArrayList<MemorySlice> copy = new ArrayList<>();
        for (MemorySlice slice : goingFirst) {
            copy.add(this.cloneSlice(slice));
        }
        return copy;
    }

    public ArrayList<MemorySlice> getGoingSecond() {
        ArrayList<MemorySlice> copy = new ArrayList<>();
        for (MemorySlice slice : goingSecond) {
            copy.add(this.cloneSlice(slice));
        }
        return copy;
    }

    public void addGoingFirstMove(MemorySlice move) {
        System.out.println("New Memory(First):");
        move.print();
        ArrayList<MemorySlice> route = new ArrayList<>();
        route.add(cloneSlice(move));
        placeRoute(route, goingFirst);
    }

    public void addGoingSecondMove(MemorySlice move) {
        System.out.println("New Memory(Second):");
        move.print();
        ArrayList<MemorySlice> route = new ArrayList<>();
        route.add(cloneSlice(move));
        placeRoute(route, goingSecond);
    }

    public void setMemory(Memory memory) {
        this.goingFirst.clear();
        for (MemorySlice slice : memory.getGoingFirst()) {
            this.goingFirst.add(this.cloneSlice(slice));
        }
        this.goingSecond.clear();
        for (MemorySlice slice : memory.getGoingSecond()) {
            this.goingSecond.add(this.cloneSlice(slice));
        }
    }

    public void placeRoute(ArrayList<MemorySlice> newRoute, ArrayList<MemorySlice> root) {
        for (MemorySlice newSlice : newRoute) {
            MemorySlice existing = null;
            for (MemorySlice r : root) {
                if (r.getMove().equals(newSlice.getMove())) {
                    existing = r;
                    break;
                }
            }

            if (existing == null) {
                root.add(cloneSlice(newSlice));
            } else {
                placeRoute(newSlice.getNext(), existing.getNext());
            }
        }
    }

    private MemorySlice cloneSlice(MemorySlice slice) {
        MemorySlice copy = new MemorySlice(slice.getMove());
        for (MemorySlice child : slice.getNext()) {
            copy.getNext().add(cloneSlice(child));
        }
        return copy;
    }
}