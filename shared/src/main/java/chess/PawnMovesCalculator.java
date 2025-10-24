package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

public class PawnMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final int row;
    private final int col;
    private final ChessGame.TeamColor myColor;
    ArrayList<ChessMove> pawnMoves = new ArrayList<>();

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        row = myPosition.getRow();
        col = myPosition.getColumn();
        myColor = board.getPiece(myPosition).getTeamColor();
    }

    public Collection<ChessMove> pieceMoves() {
        if (myColor == WHITE) {
            if (validMove(row + 1, col)) {
                addMove(row + 1, col);
                if (startMove() && validMove(row + 2, col)) {
                    addMove(row + 2, col);
                }
            }
            tryCaptureMoves(row + 1, col);
        } else {
            if (validMove(row - 1, col)) {
                addMove(row - 1, col);
                if (startMove() && validMove(row - 2, col)) {
                    addMove(row - 2, col);
                }
            }
            tryCaptureMoves(row - 1, col);
        }
        return pawnMoves;
    }

    private void tryCaptureMoves(int i, int j) {
        if (inBounds(i, j - 1) && !isEmpty(i, j - 1) && canCapture(i, j - 1)) {
            addMove(i, j - 1);
        }
        if (inBounds(i, j + 1) && !isEmpty(i, j + 1) && canCapture(i, j + 1)) {
            addMove(i, j + 1);
        }
    }

    private boolean startMove() {
        if (myColor == WHITE) {
            if (row == 2) {
                return true;
            }
        } else {
            if (row == 7) {
                return true;
            }
        }
        return false;
    }

    private boolean validMove(int i, int j) {
        if (inBounds(i, j) && isEmpty(i, j)) {
            return true;
        }
        return false;
    }

    private void addMove(int i, int j) {
        if (myColor == WHITE) {
            if (i == 8) {
                addPromotionMove(i, j);
            } else {
                pawnMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            }

        } else {
            if (i == 1) {
                addPromotionMove(i, j);
            } else {
                pawnMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            }
        }
    }

    private void addPromotionMove(int i, int j) {
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), ROOK));
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), KNIGHT));
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), BISHOP));
        pawnMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), QUEEN));
    }

    private boolean canCapture(int i, int j) {
        ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j));
        ChessPiece myPiece = board.getPiece(myPosition);
        if (myPiece.getTeamColor() != otherPiece.getTeamColor()) {
            return true;
        }
        return false;
    }

    private boolean isEmpty(int i, int j) {
        if (board.getPiece(new ChessPosition(i, j)) == null) {
            return true;
        }
        return false;
    }


    private boolean inBounds(int i, int j) {
        if (i > 0 && i < 9) {
            if (j > 0 && j < 9) {
                return true;
            }
        }
        return false;
    }
}