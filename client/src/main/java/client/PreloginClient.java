package client;

import jdk.jshell.spi.ExecutionControl;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.WHITE_QUEEN;

public class PreloginClient {

    public void run() {
        System.out.println(WHITE_QUEEN + "Welcome to Chess. Type help to get started." + WHITE_QUEEN);

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
        System.out.print("\n" + "[LOGGED_OUT] >>> ");
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String login(String... params) {
        throw new RuntimeException("Not Implemented");
    }

    private String register(String... params) {
        throw new RuntimeException("Not Implemented");
    }

    private String help() {
        throw new RuntimeException("Not Implemented");
    }

}
