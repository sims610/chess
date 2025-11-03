package chess;

import java.util.ArrayList;
import java.util.Collection;

class KingMovesCalculator implements MovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final int row;
    private final int col;
    ArrayList<ChessMove> kingMoves;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        row = myPosition.getRow();
        col = myPosition.getColumn();
        kingMoves = new ArrayList<>();
    }

    public Collection<ChessMove> pieceMoves() {

        int i = row + 1;
        for (int j = col - 1; j < col + 2; j++) {
            tryAndAddMove(i, j);
        }
        i = row - 1;
        for (int j = col - 1; j < col + 2; j++) {
            tryAndAddMove(i, j);
        }
        i = row;
        int j = col - 1;
        if (inBounds(i, j)) {
            tryAndAddMove(i, j);
        }
        i = row;
        j = col + 1;
        if (inBounds(i, j)) {
            tryAndAddMove(i, j);
        }
        return kingMoves;
    }

    private void tryAndAddMove(int i, int j) {
        if (inBounds(i, j)) {
            if (isEmpty(i, j, board)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
                kingMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            }
        }
    }
}
