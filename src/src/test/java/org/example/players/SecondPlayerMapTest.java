package org.example.players;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecondPlayerMapTest {

    @Test
    void new_map_has_all_question_marks() {
        // Given & When
        SecondPlayerMap map = new SecondPlayerMap();

        // Then
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals('?', map.map[i][j]);
            }
        }
    }

    @Test
    void miss_updates_cell_to_dot() {
        // Given
        SecondPlayerMap map = new SecondPlayerMap();

        // When
        map.updateMap("A1", "pudło");

        // Then
        assertEquals('.', map.map[0][0]);
        assertEquals('?', map.map[0][1]);
        assertEquals('?', map.map[1][0]);
    }

    @Test
    void hit_updates_cell_to_hash() {
        // Given
        SecondPlayerMap map = new SecondPlayerMap();

        // When
        map.updateMap("B2", "trafiony");

        // Then
        assertEquals('#', map.map[1][1]);
        assertEquals('?', map.map[1][0]);
        assertEquals('?', map.map[0][1]);
    }

    @Test
    void ship_sunk_marks_surrounding_cells() {
        // Given
        SecondPlayerMap map = new SecondPlayerMap();

        // When
        map.updateMap("C3", "trafiony zatopiony");

        // Then
        assertEquals('#', map.map[2][2]);

        assertEquals('.', map.map[1][1]);
        assertEquals('.', map.map[1][2]);
        assertEquals('.', map.map[1][3]);
        assertEquals('.', map.map[2][1]);
        assertEquals('.', map.map[2][3]);
        assertEquals('.', map.map[3][1]);
        assertEquals('.', map.map[3][2]);
        assertEquals('.', map.map[3][3]);
    }

    @Test
    void multiple_hits_on_same_ship_work_correctly() {
        // Given
        SecondPlayerMap map = new SecondPlayerMap();

        // When
        map.updateMap("D4", "trafiony");
        map.updateMap("E4", "trafiony");
        map.updateMap("E4", "trafiony zatopiony");

        // Then
        assertEquals('#', map.map[3][3]);
        assertEquals('#', map.map[4][3]);
        assertEquals('.', map.map[3][2]);
        assertEquals('#', map.map[3][3]);
        assertEquals('.', map.map[3][4]);
        assertEquals('.', map.map[4][2]);
        assertEquals('.', map.map[4][4]);
        assertEquals('.', map.map[5][2]);
        assertEquals('.', map.map[5][3]);
        assertEquals('.', map.map[5][4]);
    }
}