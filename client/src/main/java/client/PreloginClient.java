package client;

import jdk.jshell.spi.ExecutionControl;
import model.requestresult.LoginRequest;
import model.requestresult.LoginResult;
import model.requestresult.RegisterRequest;
import model.requestresult.RegisterResult;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.WHITE_QUEEN;

public class PreloginClient {
    ServerFacade serverFacade;
    String username;

    public PreloginClient(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        username = null;
    }

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
                if (result.startsWith("Logged in")) {
                    new PostloginClient(serverFacade, username).run();
                }
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
        if (params.length >= 2) {
            String username = params[0];
            String password = params[1];
            LoginRequest loginUser = new LoginRequest(username, password);
            LoginResult result = serverFacade.login(loginUser);
            this.username = result.username();
            return String.format("Logged in as %s", result.username());
        }
        throw new RuntimeException("Couldn't login client");
    }

    private String register(String... params) {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest newUser = new RegisterRequest(username, password, email);
            RegisterResult result = serverFacade.register(newUser);
            this.username = result.username();
            return String.format("Logged in as %s", result.username());
        }
        throw new RuntimeException("Couldn't register user");
    }

    private String help() {
        return "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n" +
                "login <USERNAME> <PASSWORD> - to play chess\n" +
                "quit - playing chess\n" +
                "help - with possible commands\n";
    }

}
