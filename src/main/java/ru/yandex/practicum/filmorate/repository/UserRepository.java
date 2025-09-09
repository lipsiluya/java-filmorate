package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.login = :login")
    Optional<User> findByLogin(@Param("login") String login);

    @Query("SELECT f.friend FROM Friendship f WHERE f.user.id = :userId AND f.status = 'CONFIRMED'")
    List<User> findFriendsByUserId(@Param("userId") Long userId);

    @Query("SELECT f.friend FROM Friendship f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    Optional<User> findFriend(@Param("userId") Long userId, @Param("friendId") Long friendId);
}