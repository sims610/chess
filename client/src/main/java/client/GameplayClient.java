package client;

import chess.*;
import client.websocket.NotificationHandler;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.*;

import client.websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class GameplayClient implements NotificationHandler {
    private int gameID;
    private ServerFacade serverFacade;
    private String username;
    private GameData gameData;
    private ChessGame game;
    private String background;
    private String teamColor;
    private final WebSocketFacade ws;

    public GameplayClient(ServerFacade serverFacade, GameData game, String username, String teamColor) throws DataAccessException {
        this.serverFacade = serverFacade;
        ws = new WebSocketFacade(this.serverFacade.serverUrl, this);
        this.username = username;
        this.gameData = game;
        this.game = gameData.game();
        this.gameID = game.gameID();
        background = SET_BG_COLOR_DARK_BROWN;
        this.teamColor = teamColor;
        connect();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("leave game")) {
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
        ws.connect(serverFacade.authToken, gameID);
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

    private String highlight(String [] params) {
        if (params.length == 1) {
            String piece = params[0];
            String pieceRow = piece.split("")[1];
            String pieceCol = piece.split("")[0];
            int pRow = numToInt(pieceRow);
            int pCol = letterToInt(pieceCol);
            Collection<ChessMove> moves = game.validMoves(new ChessPosition(pRow, pCol));
            printBoard(moves);
            return "";
        } else {
            throw new RuntimeException("Invalid input.");
        }
    }

    private String resign() {
        ws.resign(serverFacade.authToken, gameID);
        return "resign";
    }

    private String move(String... params) throws DataAccessException {
        if (params.length == 2 || params.length == 3) {
            String start = params[0];
            String end = params[1];
            String startRow = start.split("")[1];
            String startCol = start.split("")[0];
            String endRow = end.split("")[1];
            String endCol = end.split("")[0];
            int sRow = numToInt(startRow);
            int sCol = letterToInt(startCol);
            int eRow = numToInt(endRow);
            int eCol = letterToInt(endCol);
            ChessMove move;
            if (params.length == 3) {
                ChessPiece.PieceType promotionPiece = getPromotionPiece(params[2]);
                move = new ChessMove(new ChessPosition(sRow, sCol), new ChessPosition(eRow, eCol), promotionPiece);
            } else {
                move = new ChessMove(new ChessPosition(sRow, sCol), new ChessPosition(eRow, eCol), null);
            }
            ws.move(serverFacade.authToken, gameID, move);
            return "";
        } else {
            throw new RuntimeException("Invalid input");
        }
    }

    private ChessPiece.PieceType getPromotionPiece(String param) {
        return switch (param) {
            case "queen" -> QUEEN;
            case "bishop" -> BISHOP;
            case "rook" -> ROOK;
            case "knight" -> KNIGHT;
            default -> throw new IllegalStateException("Error: Unexpected value: " + param);
        };
    }

    private int numToInt(String startCol) {
        return switch (startCol) {
            case "1" -> 1;
            case "2" -> 2;
            case "3" -> 3;
            case "4" -> 4;
            case "5" -> 5;
            case "6" -> 6;
            case "7" -> 7;
            case "8" -> 8;
            default -> throw new IllegalStateException("Error: Unexpected value: " + startCol);
        };
    }

    private int letterToInt(String startRow) {
        return switch (startRow) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new IllegalStateException("Error: Unexpected value: " + startRow);
        };
    }

    private String leave() {
        ws.leave(serverFacade.authToken, gameID);
        return String.format("leave game");
    }

    private String redraw() {
        printBoard();
        return "";
    }

    private String help() {
        return """
                redraw - the board
                leave - the game
                move <start> <end> <Queen|Knight|Rook|Bishop> (if pawn makes it to the end)
                resign - forfeit the game
                highlight <chess position> - highlight legal moves
                help - with possible commands
                """;
    }

    private void printBoard() {
        printBoard(null);
    }

    private void printBoard(Collection<ChessMove> validMoves) {
        if (Objects.equals(teamColor, "BLACK")) {
            printBlackBoard(validMoves);
        } else {
            printWhiteBoard(validMoves);
        }
    }

    private void printWhiteBoard(Collection<ChessMove> validMoves) {
        ArrayList<ChessPosition> validPlaces = new ArrayList<>();
        if (validMoves != null) {
            for (ChessMove validMove : validMoves) {
                validPlaces.add(validMove.getEndPosition());
                validPlaces.add(validMove.getStartPosition());
            }
        }
        ChessBoard board = game.getBoard();
        System.out.print(setHeader());
        String[] sides = {" 1 ", " 2 ", " 3 " , " 4 " ," 5 " ," 6 " ," 7 ", " 8 "};
        for (int i = 8; i > 0; i--) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i-1]);
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                boolean highlight = false;
                if (validMoves != null) {
                    if (validPlaces.contains(new ChessPosition(i, j)))
                        highlight = true;
                }
                System.out.print(setBackground(highlight));
                System.out.print(addPiece(piece));
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i-1]);
            System.out.print(RESET_BG_COLOR + "\n");
            setBackground(false);
        }
        System.out.print(setHeader());
    }

    private void printBlackBoard(Collection<ChessMove> validMoves) {
        ArrayList<ChessPosition> validPlaces = new ArrayList<>();
        if (validMoves != null) {
            for (ChessMove validMove : validMoves) {
                validPlaces.add(validMove.getEndPosition());
                validPlaces.add(validMove.getStartPosition());
            }
        }
        ChessBoard board = game.getBoard();
        System.out.print(setHeader());
        String[] sides = {" 1 ", " 2 ", " 3 " , " 4 " ," 5 " ," 6 " ," 7 ", " 8 "};
        for (int i = 1; i < 9; i++) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i - 1]);
            for (int j = 8; j > 0; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                boolean highlight = false;
                if (validMoves != null) {
                    if (validPlaces.contains(new ChessPosition(i, j)))
                        highlight = true;
                }
                System.out.print(setBackground(highlight));
                System.out.print(addPiece(piece));
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY + sides[i - 1]);
            System.out.print(RESET_BG_COLOR + "\n");
            setBackground(false);
        }
        System.out.print(setHeader());
    }

    private String setBackground(boolean highlight) {
        if (Objects.equals(background, SET_BG_COLOR_DARK_BROWN)) {
            background = SET_BG_COLOR_LIGHT_BROWN;
            if (highlight) {
                return SET_BG_COLOR_GREEN;
            }
        } else {
            background = SET_BG_COLOR_DARK_BROWN;
            if (highlight) {
                return SET_BG_COLOR_DARK_GREEN;
            }
        }

        return background;
    }

    private String setHeader() {
        String header = "";
        header += SET_BG_COLOR_LIGHT_GREY;
        header += EMPTY;
        if (Objects.equals(teamColor, "BLACK")) {
            String[] headerValues = {" h  ",  " g  ", " f  ", " e  ", "  d  ", "  c ", "  b ", "  a "};
            for (int i = 0; i < 8; i++) {
                header += headerValues[i];
            }
        } else {
            String[] headerValues = {" a  ", " b  ", " c  ", " d  ", "  e  ", "  f ", "  g ", "  h "};
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
            return SET_TEXT_COLOR_WHITE + switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            } + RESET_TEXT_COLOR;
        } else {
            return SET_TEXT_COLOR_BLACK + switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            } + RESET_TEXT_COLOR;
        }
    }

    @Override
    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case LOAD_GAME -> {
                System.out.println("\n");
                this.game = ((LoadGameMessage) notification).game;
                printBoard();
            }
            case ERROR -> {
                System.out.println("\n");
                System.out.println(((ErrorMessage) notification).errorMessage);
            }
            case NOTIFICATION -> {
                System.out.println("\n");
                System.out.println(((NotificationMessage) notification).message);
            }
        }
        printPrompt();
    }
}
