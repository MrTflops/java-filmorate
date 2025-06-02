package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        log.info(String.format("Получен GET-запрос на получение пользователя по id %d.", id));
        return userService.findUserById(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен GET-запрос на получение всех пользователей.");
        return userService.findAll();
    }

    @GetMapping("/{id}/friends")
    Collection<User> getFriends(@PathVariable Long id) {
        log.info(String.format("Получен GET-запрос на получение всех друзей пользователя %d.", id));
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{other-id}")
    Collection<User> getCommonFriends(
            @PathVariable("id") Long userId,
            @PathVariable Long otherId
    ) {
        log.info(String.format("Получен GET-запрос на получение общих друзей пользователя %d с пользователем %d.", userId, otherId));
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info(String.format("Получен POST-запрос на создание пользователя: %s", user));
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info(String.format("Получен PUT-запрос на обновление пользователя: %s", user));
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friend-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putFriend(
            @PathVariable("id") Long userId,
            @PathVariable Long friendId
    ) {
        log.info(String.format("Получен PUT-запрос на добавление в друзья пользователя %d пользователю %d", userId, friendId));
        userService.putFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friend-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(
            @PathVariable("id") Long userId,
            @PathVariable Long friendId
    ) {
        log.info(String.format("Получен DELETE-запрос на удаление пользователя %d из друзей пользователя %d", friendId, userId));
        userService.deleteFriend(userId, friendId);
    }
}