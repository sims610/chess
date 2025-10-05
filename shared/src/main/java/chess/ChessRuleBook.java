package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessRuleBook {

    Collection<ChessMove> validMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }

    Boolean isBoardValid(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    ChessPosition findKing(ChessBoard board, ChessGame.TeamColor color) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (piece.getTeamColor() == color) {
                        System.out.println(new ChessPosition(i, j));
                        return new ChessPosition(i, j);
                    }
                }
            }
        }
        return null;
    }


    Boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        // find the King of the team we are checking
        ChessPosition kingPosition = findKing(board, color);
        //search through the board and check each position.
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() != color) {
                    System.out.println(piece);
                    Collection<ChessMove> potentialMoves = piece.pieceMoves(board, new ChessPosition(i, j));
                    for (ChessMove move : potentialMoves) {
                        System.out.println(move);
                        System.out.println("Compare");
                        System.out.println(move.getEndPosition());
                        System.out.println(kingPosition);
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }

            }

        }
        // for each piece on the other team, check if the King's position is in that piece's valid moves.
        // if it is, return True.
        return false;
    }

    Boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    Boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

}
