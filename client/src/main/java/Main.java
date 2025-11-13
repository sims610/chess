import chess.*;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.print(SET_TEXT_BOLD);
        System.out.print(EMPTY);
        System.out.println("Hello");
        System.out.print(BLACK_ROOK);
        System.out.println("\n");
        System.out.print(WHITE_ROOK);
        System.out.print(WHITE_QUEEN);
    }
}