package org.example.board;

public interface BattleshipGenerator {

    char[][] generateMap();

    static BattleshipGenerator defaultInstance() {
        return new BattleshipGeneratorImpl();
    }

}
