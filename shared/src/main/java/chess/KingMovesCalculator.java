package chess;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor team = board.getPiece(position).getTeamColor();
        ChessPiece.PieceType type = board.getPiece(position).getPieceType();

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r > 0 && r < 9 && c > 0 && c < 9) {
                    ChessPosition newPosition = new ChessPosition(r, c);
                    if (board.getPiece(newPosition) == null) {
                        ChessMove newMove = new ChessMove(position, newPosition, null);
                        moves.add(newMove);
                    } else if (board.getPiece(newPosition).getTeamColor() != team) {
                        ChessMove newMove = new ChessMove(position, newPosition, null);
                        moves.add(newMove);
                    }
                }
            }
        }
        return moves;
    }
}
