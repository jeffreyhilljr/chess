package chess;

import java.util.ArrayList;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] combinations = {{row + 1, col + 2},
                                {row - 1, col + 2},
                                {row + 1, col - 2},
                                {row - 1, col - 2},
                                {row + 2, col + 1},
                                {row + 2, col - 1},
                                {row - 2, col + 1},
                                {row - 2, col - 1}};

        for (int[] combination : combinations) {
            int r = combination[0];
            int c = combination[1];
            if (inBounds(r, c)) {
                ChessPosition newPosition = new ChessPosition(r, c);
                addMoveIfValid(board, myPosition, newPosition, moves);
            }
        }
        return moves;
    }
}
