package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor team = board.getPiece(position).getTeamColor();
        ChessPiece.PieceType type = board.getPiece(position).getPieceType();

        if (team == ChessGame.TeamColor.WHITE) {
            int r = row + 1;
            int[] colOptions = {col - 1, col + 1};

            for (int c : colOptions) {
                if (c > 0 && c < 9) {
                    ChessPosition newPosition = new ChessPosition(r, c);
                    if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != team) {
                        if (r == 8) {
                            ChessMove newMove = new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN);
                            moves.add(newMove);
                            ChessMove newMove2 = new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP);
                            moves.add(newMove2);
                            ChessMove newMove3 = new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK);
                            moves.add(newMove3);
                            ChessMove newMove4 = new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT);
                            moves.add(newMove4);
                        } else {
                            ChessMove newMove = new ChessMove(position, newPosition, null);
                            moves.add(newMove);
                        }
                    }
                }
            }
            if (row == 2) {
                ChessPosition newPosition = new ChessPosition(3, col);
                if (board.getPiece(newPosition) == null) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                    ChessPosition newPosition2 = new ChessPosition(4, col);
                    if (board.getPiece(newPosition2) == null) {
                        ChessMove newMove2 = new ChessMove(position, newPosition2, null);
                        moves.add(newMove2);
                    }
                }
            } else {
                ChessPosition newPosition = new ChessPosition(row + 1, col);
                if (board.getPiece(newPosition) == null) {
                    if (r == 8) {
                        ChessMove newMove = new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN);
                        moves.add(newMove);
                        ChessMove newMove2 = new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP);
                        moves.add(newMove2);
                        ChessMove newMove3 = new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK);
                        moves.add(newMove3);
                        ChessMove newMove4 = new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT);
                        moves.add(newMove4);
                    } else {
                        ChessMove newMove = new ChessMove(position, newPosition, null);
                        moves.add(newMove);
                    }
                }
            }
        }

        if (team == ChessGame.TeamColor.BLACK) {
            int r = row - 1;
            int[] colOptions = {col - 1, col + 1};

            for (int c : colOptions) {
                if (c > 0 && c < 9) {
                    ChessPosition newPosition = new ChessPosition(r, c);
                    if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != team) {
                        if (r == 1) {
                            ChessMove newMove = new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN);
                            moves.add(newMove);
                            ChessMove newMove2 = new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP);
                            moves.add(newMove2);
                            ChessMove newMove3 = new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK);
                            moves.add(newMove3);
                            ChessMove newMove4 = new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT);
                            moves.add(newMove4);
                        } else {
                            ChessMove newMove = new ChessMove(position, newPosition, null);
                            moves.add(newMove);
                        }
                    }
                }
            }
            if (row == 7) {
                ChessPosition newPosition = new ChessPosition(6, col);
                if (board.getPiece(newPosition) == null) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                    ChessPosition newPosition2 = new ChessPosition(5, col);
                    if (board.getPiece(newPosition2) == null) {
                        ChessMove newMove2 = new ChessMove(position, newPosition2, null);
                        moves.add(newMove2);
                    }
                }
            } else {
                ChessPosition newPosition = new ChessPosition(row - 1, col);
                if (board.getPiece(newPosition) == null) {
                    if (r == 1) {
                        ChessMove newMove = new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN);
                        moves.add(newMove);
                        ChessMove newMove2 = new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP);
                        moves.add(newMove2);
                        ChessMove newMove3 = new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK);
                        moves.add(newMove3);
                        ChessMove newMove4 = new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT);
                        moves.add(newMove4);
                    } else {
                        ChessMove newMove = new ChessMove(position, newPosition, null);
                        moves.add(newMove);
                    }
                }
            }
        }

        return moves;
    }
}
