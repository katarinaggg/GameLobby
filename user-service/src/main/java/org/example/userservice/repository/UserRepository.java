package org.example.userservice.repository;

import org.example.userservice.domain.Role;
import org.example.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // za login
    Optional<User> findByEmailAndPassword(String email, String password);

    // za registraciju
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findAllByRoleNot(Role role);


    Optional<User> findByUsername(String username);

    Optional<User> findByActivationToken(String activationToken);
}
