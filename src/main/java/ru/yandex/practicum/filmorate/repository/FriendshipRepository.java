package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipKey;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipKey> {

    Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);

    @Modifying
    @Query("DELETE FROM Friendship f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    void deleteFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    boolean existsFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Modifying
    @Query("UPDATE Friendship f SET f.status = :status WHERE f.user.id = :userId AND f.friend.id = :friendId")
    void updateFriendshipStatus(@Param("userId") Long userId, @Param("friendId") Long friendId,
                                @Param("status") FriendshipStatus status);
}