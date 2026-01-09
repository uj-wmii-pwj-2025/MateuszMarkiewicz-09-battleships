package org.example.players;

import org.example.HitState;

import static java.lang.Integer.parseInt;

public class FirstPlayerMap {

    char[][] map;
    private static final char SHIP = '#';
    private static final char MISS = '~';
    private static final char HIT = '@';

    public FirstPlayerMap(char[][] map) {
        this.map = map;
    }

    public HitState hit(String attackCoordinates) {
        int[] coordinates = getCoordinates(attackCoordinates);
        int row = coordinates[0];
        int col = coordinates[1];

        if (map[row][col] == SHIP) {
            map[row][col] = HIT;

            boolean isShipSunk = isShipSunk(row, col);

            if (isShipSunk) {
                markSunkShip(row, col);

                if (areShipsSunk()) {
                    return HitState.LAST_SUNK;
                }
                return HitState.HIT_SUNK;
            }
            return HitState.HIT;
        } else if (map[row][col] == HIT) {
            if (isShipSunk(row, col)) {
                return HitState.HIT_SUNK;
            }
            return HitState.HIT;
        } else {
            map[row][col] = MISS;
            return HitState.MISS;
        }
    }

    private boolean isShipSunk(int row, int col) {
        boolean[][] visited = new boolean[10][10];
        return checkIfShipFullySunk(row, col, visited);
    }

    private boolean checkIfShipFullySunk(int row, int col, boolean[][] visited) {
        if (!inRange(row, col) || visited[row][col]) {
            return true;
        }

        visited[row][col] = true;

        if (map[row][col] == HIT || map[row][col] == SHIP) {
            if (map[row][col] == SHIP) {
                return false;
            }

            boolean up = checkIfShipFullySunk(row - 1, col, visited);
            boolean down = checkIfShipFullySunk(row + 1, col, visited);
            boolean left = checkIfShipFullySunk(row, col - 1, visited);
            boolean right = checkIfShipFullySunk(row, col + 1, visited);

            return up && down && left && right;
        }

        return true;
    }

    private void markSunkShip(int row, int col) {
        boolean[][] visited = new boolean[10][10];
        markShipCells(row, col, visited);
    }

    private void markShipCells(int row, int col, boolean[][] visited) {
        if (!inRange(row, col) || visited[row][col]) {
            return;
        }

        visited[row][col] = true;

        if (map[row][col] == HIT) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int newRow = row + dr;
                    int newCol = col + dc;

                    if (inRange(newRow, newCol) && map[newRow][newCol] == '.') {
                        map[newRow][newCol] = MISS;
                    }
                }
            }

            markShipCells(row - 1, col, visited);
            markShipCells(row + 1, col, visited);
            markShipCells(row, col - 1, visited);
            markShipCells(row, col + 1, visited);
        }
    }

    private boolean areShipsSunk() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (map[row][col] == SHIP) {
                    return false;
                }
            }
        }
        return true;
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

    public boolean inRange(int row, int col) {
        return row >= 0 && row < 10 &&
                col >= 0 && col < 10;
    }

    public void displayMap() {
        System.out.println("Your map:");
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
}