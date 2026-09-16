package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.KING) {
            PieceMovesCalculator op = new KingMovesCalculator();
            return op.pieceMovesGetter(board, myPosition);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChessPiece)) {
            return false;
        }
        ChessPiece other = (ChessPiece) obj;
        return this.pieceColor == other.pieceColor && this.type == other.type;
    }

    @Override
    public String toString() {
        char c;
        if (type == PieceType.KING) {
            c = 'k';
        } else if (type == PieceType.QUEEN) {
            c = 'q';
        } else if (type == PieceType.BISHOP) {
            c = 'b';
        } else if (type == PieceType.ROOK) {
            c = 'r';
        } else if (type == PieceType.KNIGHT) {
            c = 'n';
        } else {
            c = 'p';
        }

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            c = Character.toUpperCase(c);
        } else {
            c = Character.toLowerCase(c);
        }

        return String.format("%c", c);
    }

//    public static void main(String[] args) {
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
//        ChessPiece whiteKing = board.getPiece(new ChessPosition(1, 5));
//
//        ChessPiece newKing = new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KING);
//        board.addPiece(new ChessPosition(4,4), newKing);
//        Collection<ChessMove> moves = whiteKing.pieceMoves(board, new ChessPosition(4, 4));
//
//
//        System.out.println(moves);
//
//    }
}
