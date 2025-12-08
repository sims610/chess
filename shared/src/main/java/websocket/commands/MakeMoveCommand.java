package websocket.commands;

import chess.ChessMove;

public record MakeMoveCommand(UserGameCommand.CommandType commandType, String authToken, int gameID, ChessMove move) {
}
