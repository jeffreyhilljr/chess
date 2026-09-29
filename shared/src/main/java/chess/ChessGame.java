package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    public ChessGame(ChessGame other) {
        this.board = new ChessBoard(other.board);
        this.teamTurn = other.teamTurn;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        } else {
            Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
            Collection<ChessMove> validMoves = new ArrayList<>();
            for (ChessMove move : moves) {
                ChessPosition newPosition = move.getEndPosition();
                ChessGame testMoveGame = new ChessGame(this);
                testMoveGame.board.addPiece(newPosition, piece);
                testMoveGame.board.removePiece(startPosition);
                if (!testMoveGame.isInCheck(piece.getTeamColor())) {
                    validMoves.add(move);
                }
            }
            return validMoves;
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        if (board.getPiece(startPosition) == null) {
            throw new InvalidMoveException();
        }
        if (board.getPiece(startPosition).getTeamColor() != teamTurn) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (validMoves.contains(move)) {
            ChessPiece piece = board.getPiece(startPosition);
            if (promotionPiece == null) {
                board.addPiece(endPosition, piece);
                board.removePiece(startPosition);
            } else {
                board.addPiece(endPosition, new ChessPiece(piece.getTeamColor(), promotionPiece));
                board.removePiece(startPosition);
            }

            if (teamTurn == TeamColor.WHITE) {
                teamTurn = TeamColor.BLACK;
            } else {
                teamTurn = TeamColor.WHITE;
            }

        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allOppTeamMoves = new ArrayList<>();
        ChessPosition kingPosition = null;
        for (int r = 1; r < 9; r++ ) {
            for (int c = 1; c < 9; c++ ) {
                ChessPosition myPosition = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(myPosition);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    allOppTeamMoves.addAll(piece.pieceMoves(board, myPosition));
                } else if (piece != null && piece.getTeamColor() == teamColor &&
                        piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = myPosition;
                }
            }
        }
        Collection<ChessPosition> allOppReachablePositions = new ArrayList<>();
        for (ChessMove move : allOppTeamMoves) {
            ChessPosition endPosition = move.getEndPosition();
            allOppReachablePositions.add(endPosition);
        }
        return allOppReachablePositions.contains(kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        } else {
            for (int r = 1; r < 9; r++ ) {
                for (int c = 1; c < 9; c++) {
                    ChessPosition myPosition = new ChessPosition(r, c);
                    ChessPiece piece = board.getPiece(myPosition);
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> validMoves = validMoves(myPosition);
                        for (ChessMove validMove : validMoves) {
                            ChessPosition newPosition = validMove.getEndPosition();
                            ChessGame testMoveGame = new ChessGame(this);
                            testMoveGame.board.addPiece(newPosition, piece);
                            testMoveGame.board.removePiece(new ChessPosition(r, c));
                            if (!testMoveGame.isInCheck(teamColor)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        } else {
            for (int r = 1; r < 9; r++ ) {
                for (int c = 1; c < 9; c++) {
                    ChessPosition myPosition = new ChessPosition(r, c);
                    ChessPiece piece = board.getPiece(myPosition);
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> validMoves = validMoves(myPosition);
                        if (!(validMoves.isEmpty())) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

//    public static void main(String[] args) {
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
//        board.addPiece(new ChessPosition(3, 7), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
//        //board.removePiece(new ChessPosition(2,6));
//        ChessGame game = new ChessGame();
//        game.setBoard(board);
//        game.setTeamTurn(TeamColor.BLACK);
//        System.out.println(game.isInCheck(TeamColor.WHITE));
//    }
}
