package chess;

import java.util.ArrayList;
import java.util.Collection;

class RookMovesCalculator implements MovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final int row;
    private final int col;

    public RookMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        row = myPosition.getRow();
        col = myPosition.getColumn();
    }

    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> rookMoves = new ArrayList<>();
        int i = row;
        int j = col;
        i++;
        while (inBounds(i, j)) {
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            i++;
        }
        i = row;
        j = col;
        i--;
        while (inBounds(i, j)) {
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            i--;
        }
        i = row;
        j = col;
        j++;
        while (inBounds(i, j)) {
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            j++;
        }
        i = row;
        j = col;
        j--;
        while (inBounds(i, j)) {
            if (isEmpty(i, j, board)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
                rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            } else {
                break;
            }
            j--;
        }
        return rookMoves;
    }
}
