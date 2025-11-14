import chess.*;
import client.PreloginClient;
import client.ServerFacade;

import static ui.EscapeSequences.*;

public class Main {
    public static ServerFacade serverFacade;

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        serverFacade = new ServerFacade("localhost", 8080);
        new PreloginClient(serverFacade).run();
    }
}