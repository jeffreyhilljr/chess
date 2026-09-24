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

    default void moveUntilStopped(ChessBoard board, ChessPosition myPosition,
                                  int rowDirection, int colDirection, List<ChessMove> moves) {
        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        while (inBounds(r + rowDirection, c + colDirection)) {
            r += rowDirection;
            c += colDirection;
            ChessPosition newPosition = new ChessPosition(r, c);
            addMoveIfValid(board, myPosition, newPosition, moves);
            if (board.getPiece(newPosition) != null) {
                break;
            }
        }
    }
}
