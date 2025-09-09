package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUserStorage implements UserStorage {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        // Создаем дружбу в обе стороны (взаимная дружба)
        Friendship friendship1 = new Friendship(user, friend, FriendshipStatus.CONFIRMED);
        Friendship friendship2 = new Friendship(friend, user, FriendshipStatus.CONFIRMED);

        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        friendshipRepository.deleteFriendship(userId, friendId);

        // Также удаляем обратную дружбу, если она есть
        friendshipRepository.findByUserIdAndFriendId(friendId, userId)
                .ifPresent(friendshipRepository::delete);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return userRepository.findFriendsByUserId(userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        List<User> userFriends = userRepository.findFriendsByUserId(userId);
        List<User> otherFriends = userRepository.findFriendsByUserId(otherId);

        userFriends.retainAll(otherFriends);
        return userFriends;
    }
}