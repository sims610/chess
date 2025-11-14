package client;

import model.GameData;
import model.requestresult.*;

import java.util.Arrays;
import java.util.Scanner;

public class PostloginClient {
    private ServerFacade serverFacade;

    public PostloginClient(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
                if (result.startsWith("logged out")) {
                    return;
                }
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
        if (params.length >= 1) {
            CreateRequest createRequest = new CreateRequest(params[0]);
            CreateResult result = serverFacade.create(createRequest);
            return String.format("created game: %s", params[0]);
        }
        throw new RuntimeException("couldn't create game");
    }

    private String list() {
        ListResult result = serverFacade.listGames(new ListRequest());
        StringBuilder total = new StringBuilder();
        for (int i = 0; i < result.games().size(); i++) {
            GameData game = result.games().get(i);
            total.append(String.format("%d: %s White: %s, Black: %s\n", i + 1, game.gameName(), game.whiteUsername(), game.blackUsername()));
        }
        return total.toString();
    }

    private String join(String... params) {
        throw new RuntimeException("Not Implemented");
    }

    private String observe(String... params) {
        throw new RuntimeException("Not Implemented");
    }

    private String logout() {
        serverFacade.logout(new LogoutRequest(null));
        return "logged out";
    }

    private String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }
}
