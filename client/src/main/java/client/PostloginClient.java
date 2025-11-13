package client;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.WHITE_QUEEN;

public class PostloginClient {
    public void run() {
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
        System.out.print("\n" + "[LOGGED_IN] >>> ");
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String create(String... params) {
        throw new RuntimeException("Not Implemented");
    }

    private String list() {
        throw new RuntimeException("Not Implemented");
    }

    private String join(String... params) {
        throw new RuntimeException("Not Implemented");
    }

    private String observe(String... params) {
        throw new RuntimeException("Not Implemented");
    }

    private String logout() {
        throw new RuntimeException("Not Implemented");
    }

    private String help() {
        throw new RuntimeException("Not Implemented");
    }
}
