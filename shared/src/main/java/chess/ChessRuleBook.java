package chess;

import java.util.Collection;

public interface ChessRuleBook {

    default Collection<ChessMove> validMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    default Boolean isBoardValid(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    default Boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    default Boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    default Boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

}
