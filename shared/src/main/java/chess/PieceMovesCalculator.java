package chess;

import java.util.List;

public interface PieceMovesCalculator {
    List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition position);
}
