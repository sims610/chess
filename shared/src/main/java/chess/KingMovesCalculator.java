package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final int row;
    private final int col;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        row = myPosition.getRow();
        col = myPosition.getColumn();
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> kingMoves = new ArrayList<>();
        int i = row + 1;
        for (int j = col - 1; j < col + 2; j++) {
            if (inBounds(i, j)) {
                if (isEmpty(i, j)) {
                    kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                } else if (canCapture(i, j)) {
                    kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                }
            }
        }
        i = row - 1;
        for (int j = col - 1; j < col + 2; j++) {
            if (inBounds(i, j)) {
                if (isEmpty(i, j)) {
                    kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                } else if (canCapture(i, j)) {
                    kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                }
            }
        }
        i = row;
        int j = col - 1;
        if (inBounds(i, j)) {
            if (isEmpty(i, j)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            }
        }
        i = row;
        j = col + 1;
        if (inBounds(i, j)) {
            if (isEmpty(i, j)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            }
        }
        return kingMoves;
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
