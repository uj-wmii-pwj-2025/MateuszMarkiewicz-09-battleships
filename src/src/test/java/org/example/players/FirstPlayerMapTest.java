package org.example.players;

import org.example.HitState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstPlayerMapTest {

    @Test
    void hit_on_same_spot_twice_returns_hit_again() {
        // Given
        char[][] board = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = '.';
            }
        }
        board[0][0] = '#';
        FirstPlayerMap map = new FirstPlayerMap(board);

        // When
        map.hit("A1");
        HitState result = map.hit("A1");

        // Then
        assertEquals(HitState.HIT_SUNK, result);
        assertEquals('@', board[0][0]);
    }

    @Test
    void last_ship_sunk_returns_last_sunk() {
        // Given
        char[][] board = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = '.';
            }
        }
        board[0][0] = '#';
        FirstPlayerMap map = new FirstPlayerMap(board);

        // When
        HitState result = map.hit("A1");

        // Then
        assertEquals(HitState.LAST_SUNK, result);
        assertEquals('@', board[0][0]);
    }

    @Test
    void invalid_coordinates_throws_exception() {
        // Given
        char[][] board = new char[10][10];
        FirstPlayerMap map = new FirstPlayerMap(board);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> map.hit("A11"));
        assertThrows(IllegalArgumentException.class, () -> map.hit("K1"));
        assertThrows(IllegalArgumentException.class, () -> map.hit("A"));
        assertThrows(IllegalArgumentException.class, () -> map.hit(""));
        assertThrows(IllegalArgumentException.class, () -> map.hit(null));
    }

    @Test
    void horizontal_two_ship_requires_two_hits_to_sink() {
        // Given
        char[][] board = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = '.';
            }
        }
        board[0][0] = '#';
        board[0][1] = '#';
        FirstPlayerMap map = new FirstPlayerMap(board);

        // When
        HitState firstHit = map.hit("A1");
        HitState secondHit = map.hit("A2");

        // Then
        assertEquals(HitState.HIT, firstHit);
        assertEquals(HitState.LAST_SUNK, secondHit);
        assertEquals('@', board[0][0]);
        assertEquals('@', board[0][1]);
    }

    @Test
    void vertical_three_ship_requires_three_hits_to_sink() {
        // Given
        char[][] board = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = '.';
            }
        }
        board[0][0] = '#';
        board[1][0] = '#';
        board[2][0] = '#';
        FirstPlayerMap map = new FirstPlayerMap(board);

        // When
        HitState firstHit = map.hit("A1");
        HitState secondHit = map.hit("B1");
        HitState thirdHit = map.hit("C1");

        // Then
        assertEquals(HitState.HIT, firstHit);
        assertEquals(HitState.HIT, secondHit);
        assertEquals(HitState.LAST_SUNK, thirdHit);
        assertEquals('@', board[0][0]);
        assertEquals('@', board[1][0]);
        assertEquals('@', board[2][0]);
    }

    @Test
    void surrounding_cells_marked_after_ship_sunk() {
        // Given
        char[][] board = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = '.';
            }
        }
        board[1][1] = '#';
        FirstPlayerMap map = new FirstPlayerMap(board);

        // When
        map.hit("B2");
        // Then
        assertEquals('@', board[1][1]);
        assertEquals('~', board[0][0]);
        assertEquals('~', board[0][1]);
        assertEquals('~', board[0][2]);
        assertEquals('~', board[1][0]);
        assertEquals('~', board[1][2]);
        assertEquals('~', board[2][0]);
        assertEquals('~', board[2][1]);
        assertEquals('~', board[2][2]);
    }

    @Test
    void coordinates_conversion_works_correctly() {
        // Given
        char[][] board = new char[10][10];
        FirstPlayerMap map = new FirstPlayerMap(board);

        // When & Then
        map.hit("A1");
        assertEquals('~', board[0][0]);

        map.hit("J10");
        assertEquals('~', board[9][9]);

        map.hit("E5");
        assertEquals('~', board[4][4]);

        map.hit("a1");
        assertEquals('~', board[0][0]);
    }
}