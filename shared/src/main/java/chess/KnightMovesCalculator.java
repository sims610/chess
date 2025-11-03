package chess;

import java.util.Collection;
import java.util.ArrayList;

class KnightMovesCalculator implements MovesCalculator {
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
            if (isEmpty(i, j, board)) {
                knightMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            } else if (canCapture(i, j, board, myPosition)) {
                knightMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            }
        }
    }
}
