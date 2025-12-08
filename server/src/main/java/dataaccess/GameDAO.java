package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.requestresult.JoinRequest;
import model.requestresult.JoinResult;

import java.util.ArrayList;

public interface GameDAO {

    void clear() throws DataAccessException;

    GameData create(String gameName) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    JoinResult joinGame(JoinRequest joinRequest, String username) throws DataAccessException;

    GameData read(Integer gameID) throws DataAccessException;

    GameData makeMove(int gameID, ChessGame game) throws DataAccessException;
}
