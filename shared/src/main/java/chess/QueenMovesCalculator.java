package chess;

import java.util.Collection;
import java.util.ArrayList;

class QueenMovesCalculator implements MovesCalculator {
    private ChessBoard board;
    private ChessPosition myPosition;

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }


    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> queenMoves = new ArrayList<>();
        BishopMovesCalculator bishopCalc = new BishopMovesCalculator(board, myPosition);
        RookMovesCalculator rookCalc = new RookMovesCalculator(board, myPosition);
        Collection<ChessMove> bishopMoves = bishopCalc.pieceMoves();
        Collection<ChessMove> rookMoves = rookCalc.pieceMoves();
        queenMoves.addAll(bishopMoves);
        queenMoves.addAll(rookMoves);
        return queenMoves;
    }
}
