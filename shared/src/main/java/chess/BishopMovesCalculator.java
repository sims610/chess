package chess;

import java.util.ArrayList;
import java.util.Collection;

class BishopMovesCalculator implements MovesCalculator {
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
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
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
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
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
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
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
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            i--; j--;
        }
        return rookMoves;
    }
}
