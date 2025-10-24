package chess;

import java.util.ArrayList;
import java.util.Collection;

class RookMovesCalculator extends MovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public RookMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
        this.board = board;
        this.myPosition = myPosition;
    }

    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> rookMoves = new ArrayList<>();
        for (int i = myPosition.getRow(); i < 8;) {
            i++;
            if (board.getPiece(new ChessPosition(i, myPosition.getColumn())) != null) {
                ChessPiece otherPiece= board.getPiece(new ChessPosition(i, myPosition.getColumn()));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
                }
                break;
            }
            rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
        }
        for (int i = myPosition.getColumn(); i < 8;) {
            i++;
            if (board.getPiece(new ChessPosition(myPosition.getRow(), i)) != null) {
                ChessPiece otherPiece= board.getPiece(new ChessPosition(myPosition.getRow(), i));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    rookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
                }
                break;
            }
            rookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
        }
        for (int i = myPosition.getRow(); i > 1;) {
            i--;
            if (board.getPiece(new ChessPosition(i, myPosition.getColumn())) != null) {
                ChessPiece otherPiece= board.getPiece(new ChessPosition(i, myPosition.getColumn()));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
                }
                break;
            }
            rookMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
        }
        for (int i = myPosition.getColumn(); i > 1;) {
            i--;
            if (board.getPiece(new ChessPosition(myPosition.getRow(), i)) != null) {
                ChessPiece otherPiece= board.getPiece(new ChessPosition(myPosition.getRow(), i));
                ChessPiece myPiece = board.getPiece(myPosition);
                if (otherPiece.getTeamColor() != myPiece.getTeamColor()) {
                    rookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
                }
                break;
            }
            rookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
        }
        return rookMoves;
    }
}
