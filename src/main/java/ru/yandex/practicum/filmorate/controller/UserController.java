package ru.yandex.practicum.filmorate.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User created = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}