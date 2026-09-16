package chess;

public interface PieceMovesCalculator {
    ChessMove[] pieceMoves(ChessBoard board, ChessPosition position);
}
