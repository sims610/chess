package chess;

import java.util.Collection;
import java.util.ArrayList;

class QueenMovesCalculator extends MovesCalculator {
    private ChessBoard board;
    private ChessPosition myPosition;

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
        this.board = board;
        this.myPosition = myPosition;
    }

    public Collection<ChessMove> pieceMoves() {
        RookMovesCalculator rookMoves = new RookMovesCalculator(board, myPosition);
        BishopMovesCalculator bishopMoves = new BishopMovesCalculator(board, myPosition);
        Collection<ChessMove> queenMoves = new ArrayList<>();
        queenMoves.addAll(rookMoves.pieceMoves());
        queenMoves.addAll(bishopMoves.pieceMoves());
        return queenMoves;
    }
}
