package org.example.sessionservice.repository;

import org.example.sessionservice.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    // pronalazak igre po imenu(provera da li igra vec postoji)
    Optional<Game> findByName(String name);

    // provera postojanja igre sa datim imenom(validacija duplikata)
    boolean existsByName(String name);
}