package org.example.sessionservice.service.impl;

import org.example.sessionservice.domain.Game;
import org.example.sessionservice.exception.BadRequestException;
import org.example.sessionservice.exception.NotFoundException;
import org.example.sessionservice.repository.GameRepository;
import org.example.sessionservice.service.GameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.sessionservice.security.CheckSecurity;

import java.util.List;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Igra sa id " + id + " ne postoji"));
    }

    @Override
    public Game getGameByName(String name) {
        return gameRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Igra sa imenom '" + name + "' ne postoji"));
    }

    @Override
    public Game createGame(Game game) {
        if (gameRepository.existsByName(game.getName())) {
            throw new BadRequestException("Igra sa imenom '" + game.getName() + "' već postoji");
        }
        return gameRepository.save(game);
    }

    @Override
    public Game updateGame(Long id, Game updatedGame) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Igra sa id " + id + " ne postoji"));

        if (updatedGame.getName() != null) {
            game.setName(updatedGame.getName());
        }
        if (updatedGame.getDescription() != null) {
            game.setDescription(updatedGame.getDescription());
        }

        return gameRepository.save(game);
    }

    @Override
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new NotFoundException("Igra sa id " + id + " ne postoji");
        }
        gameRepository.deleteById(id);
    }
}