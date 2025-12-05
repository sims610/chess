package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import dataaccess.DataAccessException;
import model.GameData;
import model.requestresult.JoinRequest;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import client.websocket.WebSocketFacade;

import static ui.EscapeSequences.*;

public class GameplayClient {
    private int gameID;
    private ServerFacade serverFacade;
    private String username;
    private GameData gameData;
    private String background;
    private String teamColor;
    private final WebSocketFacade ws;

    public GameplayClient(ServerFacade serverFacade, GameData game, String username, String teamColor) throws DataAccessException {
        this.serverFacade = serverFacade;
        ws = new WebSocketFacade(this.serverFacade.serverUrl, this);
        this.username = username;
        this.gameData = game;
        this.gameID = game.gameID();
        background = SET_BG_COLOR_BLACK;
        this.teamColor = teamColor;
        ws.connect();
    }

    public void run() {
        printBoard();

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

    private void connect() {
        ws.connect();
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
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String redraw() {
        printBoard();
        return "";
    }

    private String help() {
        return """
                redraw - the board
                leave - the game
                move <start> <end>
                resign - forfeit the game
                highlight <chess position> - highlight legal moves
                help - with possible commands
                """;
    }

    private void printBoard() {
        if (Objects.equals(teamColor, "BLACK")) {
            printBlackBoard();
        } else {
            printWhiteBoard();
        }
    }

    private void printWhiteBoard() {
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        System.out.print(setHeader());
        String[] sides = {" 8 ", " 7 ", " 6 " , " 5 " ," 4 " ," 3 " ," 2 ", " 1 "};
        for (int i = 1; i < 9; i++) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i-1]);
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                System.out.print(setBackground());
                System.out.print(addPiece(piece));
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i-1]);
            System.out.print(RESET_BG_COLOR + "\n");
            setBackground();
        }
        System.out.print(setHeader());
    }

    private void printBlackBoard() {
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        System.out.print(setHeader());
        String[] sides = {"\u20038 ", "\u20037 ", "\u20036 " ,"\u20035 " ,"\u20034 " ,"\u20033 " ,"\u20032 ", "\u20031 "};
        for (int i = 8; i > 0; i--) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i - 1]);
            for (int j = 8; j > 0; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                System.out.print(setBackground());
                System.out.print(addPiece(piece));
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i - 1]);
            System.out.print(RESET_BG_COLOR + "\n");
            setBackground();
        }
        System.out.print(setHeader());
    }

    private String setBackground() {
        if (Objects.equals(background, SET_BG_COLOR_BLACK)) {
            background = SET_BG_COLOR_WHITE;
        } else {
            background = SET_BG_COLOR_BLACK;
        }
        return background;
    }

    private String setHeader() {
        String header = "";
        header += SET_BG_COLOR_LIGHT_GREY;
        header += EMPTY;
        if (Objects.equals(teamColor, "BLACK")) {
            String[] headerValues = {" h  ",  " g  ", " f  ", " e  ", " d   ", " c  ", "  b ", "  a "};
            for (int i = 0; i < 8; i++) {
                header += headerValues[i];
            }
        } else {
            String[] headerValues = {" a  ", " b  ", " c  ", " d  ", " e  ", " f  ", "  g ", "  h "};
            for (int i = 0; i < 8; i++) {
                header += headerValues[i];
            }
        }
        header += EMPTY;
        header += RESET_BG_COLOR;
        header += "\n";
        return header;
    }

    private String addPiece(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }
    }
}
