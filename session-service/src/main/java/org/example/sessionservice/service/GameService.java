package org.example.sessionservice.service;

import org.example.sessionservice.domain.Game;

import java.util.List;

public interface GameService {

    List<Game> getAllGames();

    Game getGameById(Long id);

    Game getGameByName(String name);

    Game createGame(Game game);

    Game updateGame(Long id, Game game);

    void deleteGame(Long id);
}