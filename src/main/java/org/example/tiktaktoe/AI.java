package org.example.tiktaktoe;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private Memory memory = new Memory();
    private MemorySlice newMemorySlice;
    private MemorySlice currentStep;
    private boolean started;

    public AI(boolean started) {
        this.started = started;
    }

    public Move step(Integer pRow, Integer pCol, Character[][] table) {
        Move rightMove = getRightMove(pRow, pCol);
        if (rightMove != null) {
            return rightMove;
        } else {
            if (currentStep == null) {
                currentStep = new MemorySlice(new Move(pRow, pCol));
                newMemorySlice = currentStep;
            } else {
                currentStep.addNext(new MemorySlice(new Move(pRow, pCol)));
                currentStep = currentStep.getNext().getFirst();
            }
            Move randomMove = randomMove(table);
            currentStep.addNext(new MemorySlice(randomMove));
            currentStep = currentStep.getNext().getFirst();
            return randomMove;
        }
    }

    public Move getRightMove(Integer pRow, Integer pCol) {
        Random rand = new Random();
        if (currentStep == null) {
            ArrayList<MemorySlice> moves = started ? memory.getGoingFirst() : memory.getGoingSecond();
            if(moves.isEmpty()) return null;
            MemorySlice nextMove = moves.get(rand.nextInt(moves.size()));
            return nextMove.getMove();
        }
        Move playerMove = new Move(pRow, pCol);
        MemorySlice nextMove = null;
        if (!currentStep.getNext().isEmpty()) {
            for (MemorySlice ms : currentStep.getNext()) {
                if (ms.getMove().equals(playerMove)) {
                    nextMove = ms.getNext().get(rand.nextInt(ms.getNext().size()));
                    break;
                }
            }
        }
        if (nextMove != null) {
            currentStep = nextMove;
            return nextMove.getMove();
        }
        return null;
    }

    public Move randomMove(Character[][] table) {
        ArrayList<Move> availableMoves = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] == null) {
                    availableMoves.add(new Move(i, j));
                }
            }
        }
        Random rand = new Random();
        return availableMoves.get(rand.nextInt(availableMoves.size()));
    }


    public void printNewMemorySlice() {
        System.out.println("|-----|Start|-----|");
        for (String string : this.newMemorySlice.toStringArray()) {
            System.out.println(string);
        }
        System.out.println("|------|End|------|");
    }
}