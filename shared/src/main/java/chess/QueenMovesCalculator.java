package chess;

import java.util.ArrayList;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>(new RookMovesCalculator().pieceMovesGetter(board, position));
        moves.addAll(new BishopMovesCalculator().pieceMovesGetter(board, position));
        return moves;
    }
}
