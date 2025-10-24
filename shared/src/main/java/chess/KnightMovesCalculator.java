package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final int row;
    private final int col;
    ArrayList<ChessMove> knightMoves = new ArrayList<>();

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        row = myPosition.getRow();
        col = myPosition.getColumn();
    }

    public Collection<ChessMove> pieceMoves() {
        tryAndAddMove(row + 2, col - 1);
        tryAndAddMove(row + 2, col + 1);
        tryAndAddMove(row + 1, col - 2);
        tryAndAddMove(row + 1, col + 2);
        tryAndAddMove(row -1, col - 2);
        tryAndAddMove(row -1, col + 2);
        tryAndAddMove(row - 2, col - 1);
        tryAndAddMove(row - 2, col + 1);
        return knightMoves;
    }

    private void tryAndAddMove(int i, int j) {
        if (inBounds(i, j)) {
            if (isEmpty(i, j)) {
                knightMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j)) {
                knightMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            }
        }
    }

    private boolean canCapture(int i, int j) {
        ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j));
        ChessPiece myPiece = board.getPiece(myPosition);
        if (myPiece.getTeamColor() != otherPiece.getTeamColor()) {
            return true;
        }
        return false;
    }

    private boolean isEmpty(int i, int j) {
        if (board.getPiece(new ChessPosition(i, j)) == null) {
            return true;
        }
        return false;
    }


    private boolean inBounds(int i, int j) {
        if (i > 0 && i < 9) {
            if (j > 0 && j < 9) {
                return true;
            }
        }
        return false;
    }
}
