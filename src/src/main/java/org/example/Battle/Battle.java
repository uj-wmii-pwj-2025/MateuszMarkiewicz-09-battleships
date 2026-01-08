package org.example.Battle;

import org.example.HitState;
import org.example.players.FirstPlayerMap;
import org.example.players.SecondPlayerMap;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.example.HitState.LAST_SUNK;

public class Battle {
    private FirstPlayerMap firstPlayerMap;
    private SecondPlayerMap secondPlayerMap;
    private Socket socket;
    private BufferedReader streamIn;
    private PrintWriter streamOut;
    private boolean ended = false;
    private boolean myTurn = false;
    private String lastAttackCoordinates = "";
    private String lastResult = "";
    private Scanner scanner;

    public Battle(FirstPlayerMap firstPlayerMap, SecondPlayerMap secondPlayerMap) {
        this.firstPlayerMap = firstPlayerMap;
        this.secondPlayerMap = secondPlayerMap;
        this.scanner = new Scanner(System.in);
    }

    public void battleAsServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started. Waiting for client to connect.");
        socket = serverSocket.accept();
        System.out.println("Connection with: " + socket.getInetAddress() + " successful.");
        myTurn = false;
        initStreams();
        startBattle();
        cleanup(serverSocket);
    }

    public void battleAsClient(int port, String host) throws IOException {
        socket = new Socket(host, port);
        System.out.println("Connected to server: " + socket.getInetAddress());
        myTurn = true;
        initStreams();
        startBattle();
        cleanup(null);
    }

    private void initStreams() throws IOException {
        streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
        streamOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8), true);
    }

    private void startBattle() {
        try {
            System.out.println();
            System.out.println("!!!Battle has started!!!\n");
            System.out.println();
            if (myTurn) {
                System.out.println("You start the game!");
            } else {
                System.out.println("Opponent starts the game.");
            }

            while (!ended) {
                if (myTurn) {
                    System.out.println("————————————————————————————————————————————————————");
                    secondPlayerMap.displayMap();
                    System.out.println("\nYour turn.");

                    if (!lastResult.isEmpty()) {
                        System.out.println("Result of your last attack: " + lastResult);
                    }

                    System.out.print("Enter coordinates to attack (e.g., A5): ");
                    String input = scanner.nextLine().trim().toUpperCase();

                    if (!isValidCoordinate(input)) {
                        System.out.println("Invalid coordinates. Please use format like A5 (A-J, 1-10).");
                        continue;
                    }

                    String message;
                    if (lastAttackCoordinates.isEmpty()) {
                        message = "start;" + input;
                    } else {
                        message = lastResult + ";" + input;
                    }

                    System.out.println("————————————————————————————————————————————————————");
                    System.out.println("Sending: " + message);
                    lastAttackCoordinates = input;
                    sendMessage(message);

                    String opponentResponse = streamIn.readLine();
                    if (opponentResponse == null) {
                        System.out.println("Connection lost!");
                        ended = true;
                        break;
                    }

                    System.out.println("Received: " + opponentResponse);

                    String[] parts = opponentResponse.split(";");
                    String result = parts[0];

                    if (result.equals("ostatni zatopiony")) {
                        System.out.println();
                        System.out.println("————————————————————————————————————————————————————");
                        System.out.println("Congratulations! You have won the battle!");
                        System.out.println("————————————————————————————————————————————————————");
                        secondPlayerMap.displayMap();
                        System.out.println("————————————————————————————————————————————————————");
                        firstPlayerMap.displayMap();
                        System.out.println("————————————————————————————————————————————————————");
                        ended = true;
                        break;
                    }
                    secondPlayerMap.updateMap(input, result);
                    lastResult = result;
                    myTurn = false;

                } else {
                    System.out.println("————————————————————————————————————————————————————");
                    System.out.println("Waiting for opponent's move...");

                    String opponentMessage = streamIn.readLine();
                    if (opponentMessage == null) {
                        System.out.println("Connection lost!");
                        ended = true;
                        break;
                    }

                    System.out.println("Received: " + opponentMessage);

                    String[] parts = opponentMessage.split(";");
                    String command = parts[0];
                    String attackCoordinates = parts.length > 1 ? parts[1] : "";

                    if (command.equals("start")) {
                        System.out.println("Opponent starts with attack at: " + attackCoordinates);
                    } else {
                        System.out.println("Opponent attacks at: " + attackCoordinates);
                    }

                    HitState hitState = firstPlayerMap.hit(attackCoordinates);
                    String result = hitState.getLabel();
                    System.out.println();
                    System.out.println("————————————————————————————————————————————————————");

                    System.out.println("\nYour map after opponent's attack:");
                    firstPlayerMap.displayMap();

                    String response;
                    if (hitState == LAST_SUNK) {
                        response = "ostatni zatopiony";
                        System.out.println();
                        System.out.println("————————————————————————————————————————————————————");
                        System.out.println("All your ships have been sunk. You lost the battle :(");
                        System.out.println("————————————————————————————————————————————————————");
                        secondPlayerMap.displayMap();
                        System.out.println("————————————————————————————————————————————————————");
                        firstPlayerMap.displayMap();
                        System.out.println("————————————————————————————————————————————————————");
                        ended = true;
                        break;
                    } else {
                        response = result + ";" + attackCoordinates;
                    }

                    System.out.println("Sending: " + response);
                    sendMessage(response);

                    myTurn = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error during battle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidCoordinate(String coord) {
        if (coord == null || coord.length() < 2 || coord.length() > 3) {
            return false;
        }

        char rowChar = coord.charAt(0);
        if (rowChar < 'A' || rowChar > 'J') {
            return false;
        }

        try {
            int col = Integer.parseInt(coord.substring(1));
            return col >= 1 && col <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void sendMessage(String message) {
        streamOut.println(message);
    }

    private void cleanup(ServerSocket serverSocket) {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (streamIn != null) {
                streamIn.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        } catch (IOException e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
    }
}