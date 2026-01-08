package org.example;

import org.example.Battle.Battle;
import org.example.players.FirstPlayerMap;
import org.example.players.SecondPlayerMap;

import java.util.HashMap;
import java.util.Map;

import static org.example.board.BattleshipGenerator.defaultInstance;
import static org.example.board.MapReader.parseMapFile;

public class Main {
    public static void main(String[] args) {
        try {
            Map <String, String> arguments = parseArgs(args);
            validateArgs(arguments);

            String mode = arguments.get("mode");
            int port = Integer.parseInt(arguments.get("port"));
            String host = arguments.get("host");
            String mapFile = arguments.get("map");

            char[][] initMap;
            if (mapFile != null) {
                initMap = parseMapFile(mapFile);
            } else {
                initMap = defaultInstance().generateMap();
            }

            FirstPlayerMap firstPlayerMap = new FirstPlayerMap(initMap);
            firstPlayerMap.displayMap();

            SecondPlayerMap secondPlayerMap = new SecondPlayerMap();

            Battle battle = new Battle(firstPlayerMap, secondPlayerMap);

            if (mode.equals("server")) {
                battle.battleAsServer(port);
            } else {
                battle.battleAsClient(port, host);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map <String, String> arguments = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                String key = args[i].substring(1);

                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    arguments.put(key, args[i + 1]);
                    i += 1;
                } else {
                    System.out.println("Missing argument for option " + key);
                    System.exit(1);
                }
            }
        }
        return arguments;
    }

    private static void validateArgs(Map <String, String> arguments) {
        if (!arguments.containsKey("mode")) {
            System.out.println("Missing required argument: mode");
            System.exit(1);
        }

        if (!arguments.containsKey("port")) {
            System.out.println("Missing required argument: port");
            System.exit(1);
        }

        if (!arguments.get("mode").equals("server") && !arguments.get("mode").equals("client")) {
            System.out.println("Invalid mode. Must be 'server' or 'client'");
            System.exit(1);
        }

        if (Integer.parseInt(arguments.get("port")) < 1 || Integer.parseInt(arguments.get("port")) > 65535) {
            System.out.println("Invalid port number. Must be between 1 and 65535");
            System.exit(1);
        }

        if (arguments.get("mode").equals("client") && !arguments.containsKey("host")) {
            System.out.println("Missing required argument for client mode: host");
            System.exit(1);
        }
    }
}