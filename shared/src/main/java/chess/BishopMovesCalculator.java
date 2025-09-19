package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> bishopMoves = new ArrayList<>();
        int i = myPosition.getRow();
        int j = myPosition.getColumn();
        while (isValidMove(i+1, j+1)) {
            i++; j++;
            if (board.getPiece(new ChessPosition(i, j)) != null) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                }
                break;
            }
            bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
        }
        i = myPosition.getRow();
        j = myPosition.getColumn();
        while (isValidMove(i-1, j+1)) {
            i--; j++;
            if (board.getPiece(new ChessPosition(i, j)) != null) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                }
                break;
            }
            bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
        }
        i = myPosition.getRow();
        j = myPosition.getColumn();
        while (isValidMove(i+1, j-1)) {
            i++; j--;
            if (board.getPiece(new ChessPosition(i, j)) != null) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                }
                break;
            }
            bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
        }
        i = myPosition.getRow();
        j = myPosition.getColumn();
        while (isValidMove(i-1, j-1)) {
            i--; j--;
            if (board.getPiece(new ChessPosition(i, j)) != null) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                }
                break;
            }
            bishopMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
        }
        return bishopMoves;
    }
    Boolean isValidMove(int row, int col) {
        if (row < 1 || row > 8) {
            return false;
        }
        if (col < 1 || col > 8) {
            return false;
        }
        return true;
    }
}
