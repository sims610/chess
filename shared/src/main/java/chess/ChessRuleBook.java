package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessRuleBook {

    ChessBoard getTrialBoard(ChessBoard board, ChessMove move) {
        ChessBoard newBoard = new ChessBoard();
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece originalPiece = board.getPiece(startPosition);
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (new ChessPosition(i, j).equals(startPosition)) {
                    newBoard.addPiece(startPosition, null);
                } else if (new ChessPosition(i, j).equals(endPosition)) {
                    if (promotionPiece != null) {
                        newBoard.addPiece(endPosition, new ChessPiece(originalPiece.getTeamColor(), promotionPiece));
                    } else {
                        newBoard.addPiece(endPosition, originalPiece);
                    }
                } else {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    newBoard.addPiece(new ChessPosition(i, j), piece);
                }
            }
        }
        return newBoard;
    }

    Collection<ChessMove> validMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = myPiece.getTeamColor();
        ArrayList<ChessMove> newMoves = new ArrayList<>();
        Collection<ChessMove> allMoves = myPiece.pieceMoves(board, myPosition);
        for (ChessMove move : allMoves) {
            ChessBoard newBoard = getTrialBoard(board, move);
            if (isBoardValid(newBoard, color)){
                newMoves.add(move);
            }
        }
        return newMoves;
    }

    // This function will return false if the move just made will
    // put the team that is going to make that move into check.
    Boolean isBoardValid(ChessBoard board, ChessGame.TeamColor color) {
        if (isInCheck(board, color)) {
            return false;
        } else {
            return true;
        }
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
                    Collection<ChessMove> potentialMoves = piece.pieceMoves(board, new ChessPosition(i, j));
                    for (ChessMove move : potentialMoves) {
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

    Boolean noValidMoves(ChessBoard board, ChessGame.TeamColor color) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() == color) {
                    Collection<ChessMove> potentialMoves = validMoves(board, new ChessPosition(i, j));
                    System.out.println("Valid Moves");
                    System.out.println(potentialMoves);
                    if (!potentialMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    Boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        if (isInCheck(board, color) && noValidMoves(board, color)) {
            return true;
        }
        return false;
    }

    Boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        if (!isInCheck(board, color) && noValidMoves(board, color)) {
            return true;
        }
        return false;
    }

}
