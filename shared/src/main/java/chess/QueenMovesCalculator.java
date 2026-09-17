package chess;

import java.util.ArrayList;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition position) {
        PieceMovesCalculator op = new RookMovesCalculator();
        List<ChessMove> rookMoves = op.pieceMovesGetter(board, position);
        PieceMovesCalculator op2 = new BishopMovesCalculator();
        List<ChessMove> bishopMoves = op2.pieceMovesGetter(board, position);
        List<ChessMove> moves = new ArrayList<>(rookMoves);
        moves.addAll(bishopMoves);
        return moves;
    }
}
