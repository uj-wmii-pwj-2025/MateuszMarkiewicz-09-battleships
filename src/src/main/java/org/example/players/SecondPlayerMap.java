package org.example.players;

import static java.lang.Integer.parseInt;

public class SecondPlayerMap {

    final char[][] map;

    public SecondPlayerMap() {
        map = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j] = '?';
            }
        }
    }

    public void updateMap(String coordinate, String result) {

        String coord = coordinate;
        if (coord.contains(";")) {
            String[] parts = coord.split(";");
            coord = parts.length > 1 ? parts[1] : parts[0];
        }

        int[] coords = getCoordinates(coord);
        int row = coords[0];
        int col = coords[1];

        switch (result) {
            case "pudło":
                map[row][col] = '.';
                break;
            case "trafiony":
                map[row][col] = '#';
                break;
            case "trafiony zatopiony":
            case "ostatni zatopiony":
                map[row][col] = '#';
                markSunkShipArea(row, col);
                break;
        }
    }

    private void markSunkShipArea(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newCol = col + dc;

                if (inRange(newRow, newCol) && map[newRow][newCol] == '?') {
                    map[newRow][newCol] = '.';
                }
            }
        }

        boolean[][] visited = new boolean[10][10];
        findAndMarkRestOfShip(row, col, visited);
    }

    private void findAndMarkRestOfShip(int row, int col, boolean[][] visited) {
        if (!inRange(row, col) || visited[row][col]) {
            return;
        }

        visited[row][col] = true;

        if (map[row][col] == '#') {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int newRow = row + dr;
                    int newCol = col + dc;

                    if (inRange(newRow, newCol) && map[newRow][newCol] == '?') {
                        map[newRow][newCol] = '.';
                    }
                }
            }

            findAndMarkRestOfShip(row - 1, col, visited);
            findAndMarkRestOfShip(row + 1, col, visited);
            findAndMarkRestOfShip(row, col - 1, visited);
            findAndMarkRestOfShip(row, col + 1, visited);
        }
    }

    private int[] getCoordinates(String attackCoordinates) {
        if (attackCoordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }

        attackCoordinates = attackCoordinates.trim().toUpperCase();
        if (attackCoordinates.length() < 2) {
            throw new IllegalArgumentException("Invalid format: " + attackCoordinates);
        }
        try {
            int row = attackCoordinates.charAt(0) - 'A';
            int col = parseInt(attackCoordinates.substring(1)) - 1;
            if (row < 0 || row >= 10 || col < 0 || col >= 10) {
                throw new IllegalArgumentException("Out of bounds: " + attackCoordinates);
            }

            return new int[]{row, col};

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in: " + attackCoordinates);
        }
    }

    private boolean inRange(int row, int col) {
        return row >= 0 && row < 10 &&
                col >= 0 && col < 10;
    }

    public void displayMap() {
        System.out.println("Your opponents map:");
        System.out.print("  ");
        for (int col = 1; col <= 10; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < 10; row++) {
            char rowLabel = (char) ('A' + row);
            System.out.print(rowLabel + " ");
            for (int col = 0; col < 10; col++) {
                System.out.print(map[row][col] + " ");
            }
            System.out.println();
        }
    }

    public void displayAfterWin() {
        System.out.println("Your opponents map:");
        System.out.print("  ");
        for (int col = 1; col <= 10; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < 10; row++) {
            char rowLabel = (char) ('A' + row);
            System.out.print(rowLabel + " ");
            for (int col = 0; col < 10; col++) {
                if (map[row][col] == '?') {
                    System.out.print('.' + " ");
                } else {
                    System.out.print(map[row][col] + " ");
                }
            }
            System.out.println();
        }
    }
}