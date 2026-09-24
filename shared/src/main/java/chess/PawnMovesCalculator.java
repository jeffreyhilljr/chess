package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            pawnLogic(board, myPosition, 1, moves);
        } else {
            pawnLogic(board, myPosition, -1, moves);
        }

        return moves;
    }

    void promotePawn(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition, List<ChessMove> moves) {
        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
    }

    void pawnLogic(ChessBoard board, ChessPosition myPosition, int moveDirection, List<ChessMove> moves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int r = row + moveDirection;

        for (int c : new int[]{col - 1, col + 1}) {
            if (inBounds(r, c) && board.getPiece(new ChessPosition(r, c)) != null &&
                    board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(r, c)).getTeamColor()) {
                if (r == 1 || r == 8) {
                    promotePawn(board, myPosition, new ChessPosition(r, c), moves);
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                }
            }
        }

        if (inBounds(r, col) && board.getPiece(new ChessPosition(r, col)) == null) {
            if (r == 1 || r == 8) {
                promotePawn(board, myPosition, new ChessPosition(r, col), moves);
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(r, col), null));
                if (10*row + 25 * moveDirection == 45 &&
                        board.getPiece(new ChessPosition(r + moveDirection, col)) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(r + moveDirection, col), null));
                }
            }
        }
    }
}
