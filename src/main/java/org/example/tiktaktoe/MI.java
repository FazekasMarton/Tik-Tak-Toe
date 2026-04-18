package org.example.tiktaktoe;

import java.util.ArrayList;
import java.util.Random;

public class MI {
    public Move step(Character[][] table){
        return randomMove(table);
    }

    public Move randomMove(Character[][] table){
        ArrayList<Move> availableMoves = new ArrayList<>();

        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table[i].length; j++){
                if(table[i][j] == null){
                    availableMoves.add(new Move(i, j));
                }
            }
        }

        Random rand = new Random();
        return availableMoves.get(rand.nextInt(availableMoves.size()));
    }
}
