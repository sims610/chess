package client;

import model.GameData;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayClient {
    private int gameID;
    private ServerFacade serverFacade;
    private String username;
    private GameData gameData;

    public GameplayClient(ServerFacade serverFacade, int gameID, String username) {
        this.serverFacade = serverFacade;
        this.gameID = gameID;
        this.username = username;

    }

    public void run() {
        printBoard();
        printPrompt();

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + "[GAME] >>> ");
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String help() {
        throw new RuntimeException("Not Implemented");
    }

    private void printBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(SET_BG_COLOR_BLACK + WHITE_KING);
                System.out.print(SET_BG_COLOR_WHITE + EMPTY);
            }
            System.out.print(RESET_BG_COLOR + "\n");
            for (int j = 0; j < 4; j++) {
                System.out.print(SET_BG_COLOR_WHITE + EMPTY);
                System.out.print(SET_BG_COLOR_BLACK + EMPTY);
            }
            System.out.print(RESET_BG_COLOR + "\n");
        }
    }
}
