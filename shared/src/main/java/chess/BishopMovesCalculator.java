package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public List<ChessMove> pieceMovesGetter(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor team = board.getPiece(position).getTeamColor();
        ChessPiece.PieceType type = board.getPiece(position).getPieceType();

        int r = row;
        int c = col;
        while(true) {
            r++;
            c++;
            if (r > 0 && r < 9 && c > 0 && c < 9) {
                ChessPosition newPosition = new ChessPosition(r, c);
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
        c = col;
        while(true) {
            r++;
            c--;
            if (r > 0 && r < 9 && c > 0 && c < 9) {
                ChessPosition newPosition = new ChessPosition(r, c);
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
        c = col;
        while(true) {
            r--;
            c++;
            if (r > 0 && r < 9 && c > 0 && c < 9) {
                ChessPosition newPosition = new ChessPosition(r, c);
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
        c = col;
        while(true) {
            r--;
            c--;
            if (r > 0 && r < 9 && c > 0 && c < 9) {
                ChessPosition newPosition = new ChessPosition(r, c);
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
