package chess;

import java.util.Collection;
import java.util.List;

public class MovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;

    public MovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    Collection<ChessMove> pieceMoves() {
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.getPieceType()) {
            case KING -> {
                KingMovesCalculator kingMoves = new KingMovesCalculator(board, myPosition);
                return kingMoves.pieceMoves();
            }
            case QUEEN -> {
                QueenMovesCalculator queenMoves = new QueenMovesCalculator(board, myPosition);
                return queenMoves.pieceMoves();
            }
            case BISHOP -> {
                BishopMovesCalculator bishopMoves = new BishopMovesCalculator(board, myPosition);
                return bishopMoves.pieceMoves();
            }
            case KNIGHT -> {
                KnightMovesCalculator knightMoves = new KnightMovesCalculator(board, myPosition);
                return knightMoves.pieceMoves();
            }
            case ROOK -> {
                RookMovesCalculator rookMoves = new RookMovesCalculator(board, myPosition);
                return rookMoves.pieceMoves();
            }
            case PAWN -> {
                PawnMovesCalculator pawnMoves = new PawnMovesCalculator(board, myPosition);
                return pawnMoves.pieceMoves();
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return List.of(new ChessMove(new ChessPosition(5, 4), new ChessPosition(1, 8), null));
        }
        return List.of();
    }
}
