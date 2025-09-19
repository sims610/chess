package chess;
import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;
import static chess.ChessPiece.PieceType.BISHOP;

public class PawnMovesCalculator {
    private ChessBoard board;
    private ChessPosition myPosition;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this. myPosition = myPosition;
    }

    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> pawnMoves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        // For normal one at a time moves
        if (normalMove(row, col, myColor)) {
            if (myColor == BLACK) {
                if (row-1 == 1) {
                    promotePiece(row-1, col, myPosition, pawnMoves);
                } else {
                    pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                }
            } else {
                if (row+1 == 8) {
                    promotePiece(row+1, col, myPosition, pawnMoves);
                } else {
                    pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                }
            }
        }
        if (startMove(row, col, myColor)) {
            if (myColor == BLACK) {
                pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row-2, col), null));
            } else {
                pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row+2, col), null));
            }
        }
        canCapture(row, col, myColor, pawnMoves);
        return pawnMoves;
    }

    //used for a normal move
    boolean normalMove(int row, int col, ChessGame.TeamColor myColor) {
        if (myColor == BLACK && isValid(row-1, col)) {
            return true;
        } else if (myColor == WHITE && isValid(row+1, col)){
            return true;
        } else {
            return false;
        }
    }

    boolean startMove(int row, int col, ChessGame.TeamColor myColor) {
        if (myColor == BLACK && myPosition.getRow() == 7 && isValid(row-2, col) && normalMove(row, col, myColor)) {
            return true;
        } else if (myColor == WHITE && myPosition.getRow() == 2 && isValid(row+2, col) && normalMove(row, col, myColor)){
            return true;
        } else {
            return false;
        }
    }

    void canCapture(int row, int col, ChessGame.TeamColor myColor, Collection<ChessMove> pawnMoves) {
        if (myColor == BLACK) {
            ChessPiece leftPiece = null;
            ChessPiece rightPiece = null;
            if (inBounds(row-1, col-1)) {
                leftPiece = board.getPiece(new ChessPosition(row-1, col-1));
            }
            if (inBounds(row-1, col+1)) {
                rightPiece = board.getPiece(new ChessPosition(row-1, col+1));
            }
            if (leftPiece != null && leftPiece.getTeamColor() == WHITE && inBounds(row-1, col-1)) {
                if (row-1 == 1) {
                    promotePiece(row-1, col-1, myPosition, pawnMoves);
                } else {
                    pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), null));
                }
            } else if (rightPiece != null && rightPiece.getTeamColor() == WHITE && inBounds(row-1, col+1)) {
                if (row-1 == 1) {
                    promotePiece(row-1, col+1, myPosition, pawnMoves);
                } else {
                    pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), null));
                }
            }
        } else {
            ChessPiece leftPiece = null;
            ChessPiece rightPiece = null;
            if (inBounds(row+1, col-1)) {
                leftPiece = board.getPiece(new ChessPosition(row+1, col-1));
            }
            if (inBounds(row+1, col+1)) {
                rightPiece = board.getPiece(new ChessPosition(row+1, col+1));
            }
            if (leftPiece != null && leftPiece.getTeamColor() == BLACK && inBounds(row+1, col-1)) {
                if (row+1 == 8) {
                    promotePiece(row+1, col-1, myPosition, pawnMoves);
                } else {
                    pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), null));
                }
            } else if (rightPiece != null && rightPiece.getTeamColor() == BLACK && inBounds(row+1, col+1)) {
                if (row+1 == 8) {
                    promotePiece(row+1, col+1, myPosition, pawnMoves);
                } else {
                    pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), null));
                }
            }
        }
    }

    void promotePiece(int row, int col, ChessPosition myPosition, Collection<ChessMove> pawnMoves) {
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row, col), QUEEN));
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row, col), KNIGHT));
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row, col), ROOK));
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(row, col), BISHOP));
    }

    boolean isValid(int row, int col) {
        return board.getPiece(new ChessPosition(row, col)) == null;
    }

    boolean inBounds(int row, int col) {
        if (row < 9 && row > 0) {
            return col < 9 && col > 0;
        }
        return false;
    }
}
