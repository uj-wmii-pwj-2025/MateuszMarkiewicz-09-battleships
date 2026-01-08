package org.example;

import org.example.board.MapReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MapReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void valid_map_file_returns_correct_map() throws Exception {
        // Given
        String mapContent = """
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("test_map.txt");
        Files.writeString(mapFile, mapContent);

        // When
        char[][] result = MapReader.parseMapFile(mapFile.toString());

        // Then
        assertEquals(10, result.length);
        assertEquals(10, result[0].length);
        assertEquals('.', result[0][0]);
        assertEquals('.', result[9][9]);
    }

    @Test
    void file_with_ships_returns_map_with_ships() throws Exception {
        // Given
        String mapContent = """
                #.........
                .#........
                ..#.......
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("ships.txt");
        Files.writeString(mapFile, mapContent);

        // When
        char[][] result = MapReader.parseMapFile(mapFile.toString());

        // Then
        assertEquals('#', result[0][0]);
        assertEquals('#', result[1][1]);
        assertEquals('#', result[2][2]);
        assertEquals('.', result[0][1]);
    }

    @Test
    void empty_lines_are_ignored() throws Exception {
        // Given
        String mapContent = """
                
                ..........
                
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                
                """;
        Path mapFile = tempDir.resolve("empty_lines.txt");
        Files.writeString(mapFile, mapContent);

        // When
        char[][] result = MapReader.parseMapFile(mapFile.toString());

        // Then
        assertEquals(10, result.length);
        assertEquals(10, result[0].length);
    }

    @Test
    void whitespace_is_trimmed() throws Exception {
        // Given
        String mapContent = """
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("whitespace.txt");
        Files.writeString(mapFile, "   " + mapContent + "   ");

        // When
        char[][] result = MapReader.parseMapFile(mapFile.toString());

        // Then
        assertEquals(10, result.length);
        assertEquals(10, result[0].length);
    }

    @Test
    void too_few_rows_throws_exception() throws Exception {
        // Given
        String mapContent = """
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("few_rows.txt");
        Files.writeString(mapFile, mapContent);

        // When & Then
        Exception exception = assertThrows(Exception.class,
                () -> MapReader.parseMapFile(mapFile.toString()));
        assertTrue(exception.getMessage().contains("row"));
    }

    @Test
    void too_many_rows_throws_exception() throws Exception {
        // Given
        StringBuilder mapContent = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            mapContent.append("..........\n");
        }
        Path mapFile = tempDir.resolve("many_rows.txt");
        Files.writeString(mapFile, mapContent.toString());

        // When & Then
        Exception exception = assertThrows(Exception.class,
                () -> MapReader.parseMapFile(mapFile.toString()));
        assertTrue(exception.getMessage().contains("row"));
    }

    @Test
    void row_too_short_throws_exception() throws Exception {
        // Given
        String mapContent = """
                ..........
                .......
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("short_row.txt");
        Files.writeString(mapFile, mapContent);

        // When & Then
        Exception exception = assertThrows(Exception.class,
                () -> MapReader.parseMapFile(mapFile.toString()));
        assertTrue(exception.getMessage().contains("row"));
    }

    @Test
    void row_too_long_throws_exception() throws Exception {
        // Given
        String mapContent = """
                ..........
                ............
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("long_row.txt");
        Files.writeString(mapFile, mapContent);

        // When & Then
        Exception exception = assertThrows(Exception.class,
                () -> MapReader.parseMapFile(mapFile.toString()));
        assertTrue(exception.getMessage().contains("row"));
    }

    @Test
    void invalid_symbol_throws_exception() throws Exception {
        // Given
        String mapContent = """
                ..........
                .....X....
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("invalid_symbol.txt");
        Files.writeString(mapFile, mapContent);

        // When & Then
        Exception exception = assertThrows(Exception.class,
                () -> MapReader.parseMapFile(mapFile.toString()));
        assertTrue(exception.getMessage().contains("symbol"));
    }

    @Test
    void mixed_valid_symbols_are_parsed_correctly() throws Exception {
        // Given
        String mapContent = """
                #.........
                .#........
                ..#.......
                ...#......
                ....#.....
                .....#....
                ......#...
                .......#..
                ........#.
                .........#
                """;
        Path mapFile = tempDir.resolve("diagonal.txt");
        Files.writeString(mapFile, mapContent);

        // When
        char[][] result = MapReader.parseMapFile(mapFile.toString());

        // Then
        assertEquals('#', result[0][0]);
        assertEquals('#', result[1][1]);
        assertEquals('#', result[9][9]);
        assertEquals('.', result[0][1]);
        assertEquals('.', result[9][8]);
    }

    @Test
    void only_ships_returns_all_hash_symbols() throws Exception {
        // Given
        String mapContent = """
                ##########
                ##########
                ##########
                ##########
                ##########
                ##########
                ##########
                ##########
                ##########
                ##########
                """;
        Path mapFile = tempDir.resolve("all_ships.txt");
        Files.writeString(mapFile, mapContent);

        // When
        char[][] result = MapReader.parseMapFile(mapFile.toString());

        // Then
        assertEquals('#', result[0][0]);
        assertEquals('#', result[5][5]);
        assertEquals('#', result[9][9]);
    }

    @Test
    void only_dots_returns_all_dot_symbols() throws Exception {
        // Given
        String mapContent = """
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                ..........
                """;
        Path mapFile = tempDir.resolve("all_dots.txt");
        Files.writeString(mapFile, mapContent);

        // When
        char[][] result = MapReader.parseMapFile(mapFile.toString());

        // Then
        assertEquals('.', result[0][0]);
        assertEquals('.', result[5][5]);
        assertEquals('.', result[9][9]);
    }
}
