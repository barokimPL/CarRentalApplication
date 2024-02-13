package pl.sda.carrental.model.repository.userRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sda.carrental.model.entity.userEntities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    Optional<T> findByUsernameOrEmail(String username, String email);

    @Query("SELECT u.email FROM User u")
    List<String> getAllEmails();

    @Query("SELECT u.username FROM User u")
    List<String> getAllUsernames();
}
