package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friendships")
@Data
@NoArgsConstructor
public class Friendship {

    @Id
    @EmbeddedId
    private FriendshipKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public Friendship(User user, User friend, FriendshipStatus status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
        this.id = new FriendshipKey(user.getId(), friend.getId());
    }
}