package chess;

import java.util.ArrayList;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor team = board.getPiece(position).getTeamColor();
        ChessPiece.PieceType type = board.getPiece(position).getPieceType();

        for (int i = 1; i < 9; i++) {
            ChessPosition newPosition = new ChessPosition(row, i);
            ChessPosition newPosition2 = new ChessPosition(i, col);

        }

        int c = col;
        while(true) {
            c++;
            if (c > 0 && c < 9) {
                ChessPosition newPosition = new ChessPosition(row, c);
                if (board.getPiece(newPosition) == null) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != team) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        c = col;
        while(true) {
            c--;
            if (c > 0 && c < 9) {
                ChessPosition newPosition = new ChessPosition(row, c);
                if (board.getPiece(newPosition) == null) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != team) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        int r = row;
        while(true) {
            r++;
            if (r > 0 && r < 9) {
                ChessPosition newPosition = new ChessPosition(r, col);
                if (board.getPiece(newPosition) == null) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != team) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        r = row;
        while(true) {
            r--;
            if (r > 0 && r < 9) {
                ChessPosition newPosition = new ChessPosition(r, col);
                if (board.getPiece(newPosition) == null) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != team) {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    moves.add(newMove);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }
}
