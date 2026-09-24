package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        moveUntilStopped(board, myPosition, 1, 1, moves);
        moveUntilStopped(board, myPosition, -1, 1, moves);
        moveUntilStopped(board, myPosition, 1, -1, moves);
        moveUntilStopped(board, myPosition, -1, -1, moves);

        return moves;
    }
}
