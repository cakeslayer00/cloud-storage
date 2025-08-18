package dev.sv.cloud_file_storage.repository;

import dev.sv.cloud_file_storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean getUserByUsername(String username);

    boolean existsByUsername(String username);
}
