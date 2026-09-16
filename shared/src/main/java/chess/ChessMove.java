package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChessMove)) {
            return false;
        }
        ChessMove other = (ChessMove) obj;
        return Objects.equals(this.startPosition, other.startPosition) && Objects.equals(this.endPosition, other.endPosition) && Objects.equals(this.promotionPiece, other.promotionPiece);
    }

    @Override
    public String toString() {
        return String.format("Move: %s, %s, %s", startPosition, endPosition, promotionPiece);
    }

//    public static void main(String[] args) {
//        ChessMove move = new ChessMove(new ChessPosition(1, 1), new ChessPosition(1,2), null);
//        System.out.println(move);
//
//    }
}
