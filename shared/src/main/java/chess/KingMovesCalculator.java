package chess;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();


        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (inBounds(r, c)) {
                    ChessPosition newPosition = new ChessPosition(r, c);
                    addMoveIfValid(board, myPosition, newPosition, moves);
                }
            }
        }
        return moves;
    }
}
