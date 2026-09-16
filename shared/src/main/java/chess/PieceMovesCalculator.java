package chess;

import java.util.List;

public interface PieceMovesCalculator {
    List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
