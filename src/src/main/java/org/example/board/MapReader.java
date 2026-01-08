package org.example.board;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.stream.Stream;

public class MapReader {

    public static char[][] parseMapFile(String filename) throws Exception {
        Path filePath = Path.of(filename);

        try (Stream<String> lines = Files.lines(filePath)) {
            String[] validLines = lines
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);

            validateRowCount(validLines.length);

            char[][] grid = new char[10][10];
            populateGrid(grid, validLines);

            return grid;
        }
    }

    private static void validateRowCount(int actualRows) {
        if (actualRows != 10) {
            throw new InvalidParameterException("Incorrect row count");
        }
    }

    private static void populateGrid(char[][] grid, String[] rows) {
        for (int row = 0; row < 10; row++) {
            String currentRow = rows[row];

            if (currentRow.length() != 10) {
                throw new InvalidParameterException("Incorrect row count");
            }

            for (int col = 0; col < 10; col++) {
                char symbol = currentRow.charAt(col);
                validateSymbol(symbol, row, col);
                grid[row][col] = symbol;
            }
        }
    }

    private static void validateSymbol(char symbol, int row, int col) {
        if (symbol != '#' && symbol != '.') {
            throw new InvalidParameterException("Incorrect symbol");
        }
    }
}
