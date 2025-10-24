package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final int row;
    private final int col;

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        row = myPosition.getRow();
        col = myPosition.getColumn();
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> rookMoves = new ArrayList<>();
        int i = row;
        int j = col;
        i++; j++;
        while (inBounds(i, j)) {
            if (isEmpty(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            i++; j++;
        }
        i = row;
        j = col;
        i++; j--;
        while (inBounds(i, j)) {
            if (isEmpty(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            i++; j--;
        }
        i = row;
        j = col;
        i--; j++;
        while (inBounds(i, j)) {
            if (isEmpty(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            i--; j++;
        }
        i = row;
        j = col;
        i--; j--;
        while (inBounds(i, j)) {
            if (isEmpty(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            i--; j--;
        }
        return rookMoves;
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
