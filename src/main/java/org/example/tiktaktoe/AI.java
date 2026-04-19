package org.example.tiktaktoe;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private Memory memory = new Memory();
    private MemorySlice newMemorySlice;
    private MemorySlice currentStep;
    private boolean started;

    public void newGame(boolean starter) {
        this.started = starter;
        newMemorySlice = null;
        currentStep = null;
    }

    public Move step(Integer pRow, Integer pCol, Character[][] table) {
        Move rightMove = getRightMove(pRow, pCol);
        if (rightMove != null) {
            return rightMove;
        } else {
            return randomMove(pRow, pCol, table);
        }
    }

    public Move getRightMove(Integer pRow, Integer pCol) {
        Move rightMove = null;
        Random rand = new Random();
        if (currentStep == null && started) {
            ArrayList<MemorySlice> moves = memory.getGoingFirst();
            if (!moves.isEmpty()) {
                MemorySlice nextMove = moves.get(rand.nextInt(moves.size()));
                newMemorySlice = nextMove;
                currentStep = nextMove;
                rightMove = nextMove.getMove();
            }
        } else if (currentStep == null) {
            ArrayList<MemorySlice> moves = memory.getGoingSecond();
            for (MemorySlice move : moves) {
                if (move.getMove().equals(new Move(pRow, pCol))) {
                    newMemorySlice = move;
                    if (!move.getNext().isEmpty()) {
                        MemorySlice nextMove = move.getNext().get(rand.nextInt(move.getNext().size()));
                        currentStep = nextMove;
                        rightMove = nextMove.getMove();
                    }
                    break;
                }
            }
        } else {
            Move playerMove = new Move(pRow, pCol);
            if (!currentStep.getNext().isEmpty()) {
                for (MemorySlice ms : currentStep.getNext()) {
                    if (ms.getMove().equals(playerMove)) {
                        currentStep = ms.getNext().get(rand.nextInt(ms.getNext().size()));
                        rightMove = currentStep.getMove();
                        break;
                    }
                }
            }
        }
        System.out.println(rightMove);
        return rightMove;
    }

    public Move randomMove(int pRow, int pCol, Character[][] table) {
        Move randomMove = getRandomMove(table);
        if (currentStep == null && started) {
            newMemorySlice = new MemorySlice(randomMove);
            currentStep = newMemorySlice;
        } else if (currentStep == null) {
            newMemorySlice = new MemorySlice(new Move(pRow, pCol));
            newMemorySlice.addNext(new MemorySlice(randomMove));
            currentStep = newMemorySlice.getNext().getFirst();
        } else {
            currentStep.addNext(new MemorySlice(new Move(pRow, pCol)));
            currentStep = currentStep.getNext().getFirst();

            currentStep.addNext(new MemorySlice(randomMove));
            currentStep = currentStep.getNext().getFirst();
        }
        return randomMove;
    }

    public Move getRandomMove(Character[][] table) {
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

    public void saveMemory(boolean won, Move lastMove) {
        if (started) {
            if (won) {
                memory.addGoingFirstMove(newMemorySlice);
            } else {
                currentStep.addNext(new MemorySlice(lastMove));
                memory.addGoingSecondMove(newMemorySlice);
            }
        } else {
            if (won) {
                memory.addGoingSecondMove(newMemorySlice);
            } else {
                currentStep.addNext(new MemorySlice(lastMove));
                memory.addGoingFirstMove(newMemorySlice);
            }
        }
    }

    public void printNewMemorySlice() {
        System.out.println("|-----|Start|-----|");
        if (newMemorySlice != null) {
            for (String string : this.newMemorySlice.toStringArray()) {
                System.out.println(string);
            }
        }
        System.out.println("|------|End|------|");
    }
}