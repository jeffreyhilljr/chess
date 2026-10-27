package model;

import chess.ChessGame;

public record GameDataSummary(int gameID, String whiteUsername, String blackUsername, String gameName) {
}
