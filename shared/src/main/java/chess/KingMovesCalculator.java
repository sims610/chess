package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    private ChessBoard board;
    private ChessPosition myPosition;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> kingMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row != 1) {
            if(isValidMove(kingMoves, row-1, col)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
            }
            if (col != 1 && isValidMove(kingMoves, row-1, col-1)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(row-1, col-1), null));
            }
            if (col != 8 && isValidMove(kingMoves, row-1, col+1)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(row-1, col+1), null));
            }
        }
        if (row != 8) {
            if (isValidMove(kingMoves, row + 1, col)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
            }
            if (col != 1 && isValidMove(kingMoves, row+1, col-1)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(row+1, col-1), null));
            }
            if (col != 8 && isValidMove(kingMoves, row+1, col+1)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(row+1, col+1), null));
            }
        }
        if (col != 1 && isValidMove(kingMoves, row, col-1)) {
            kingMoves.add(new ChessMove(myPosition, new ChessPosition(row, col-1), null));
        }
        if (col != 8 && isValidMove(kingMoves, row, col+1)) {
            kingMoves.add(new ChessMove(myPosition, new ChessPosition(row, col+1), null));
        }
        return kingMoves;
    }

    Boolean isValidMove(Collection<ChessMove> kingMoves, int i, int j) {
        if (board.getPiece(new ChessPosition(i, j)) != null) {
            ChessPiece otherPiece= board.getPiece(new ChessPosition(i, j));
            ChessPiece myPiece = board.getPiece(myPosition);
            if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                return false;
            }
            return false;
        } else {
            return true;
        }
    }
}
