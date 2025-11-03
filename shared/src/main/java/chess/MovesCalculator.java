package chess;

import java.util.Collection;
import java.util.List;

public interface MovesCalculator {

    Collection<ChessMove> pieceMoves();

    default boolean canCapture(int i, int j, ChessBoard board, ChessPosition myPosition) {
        ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j));
        ChessPiece myPiece = board.getPiece(myPosition);
        return myPiece.getTeamColor() != otherPiece.getTeamColor();
    }

    default boolean isEmpty(int i, int j, ChessBoard board) {
        return board.getPiece(new ChessPosition(i, j)) == null;
    }


    default boolean inBounds(int i, int j) {
        if (i > 0 && i < 9) {
            return j > 0 && j < 9;
        }
        return false;
    }
}
