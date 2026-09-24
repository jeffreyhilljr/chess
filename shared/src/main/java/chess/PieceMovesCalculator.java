package chess;

import java.util.List;

public interface PieceMovesCalculator {
    List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition myPosition);

    default boolean inBounds(int row, int col) {
        return row > 0 && row < 9 && col > 0 && col < 9;
    }

    default void addMoveIfValid(ChessBoard board, ChessPosition myPosition,
                                ChessPosition newPosition, List<ChessMove> moves) {
        if (board.getPiece(newPosition) == null ||
                board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moves.add(newMove);
        }
    }
}
