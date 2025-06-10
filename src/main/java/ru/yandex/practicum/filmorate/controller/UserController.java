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
    // Константы для путей
    private static final String ID_PATH = "/{id}";
    private static final String FRIENDS_PATH = ID_PATH + "/friends";
    private static final String COMMON_FRIENDS_PATH = FRIENDS_PATH + "/common/{otherId}";
    private static final String FRIEND_ID_PATH = FRIENDS_PATH + "/{friendId}";

    // Константы для логов
    private static final String GET_USER_LOG = "Получен GET-запрос на получение пользователя по id {}.";
    private static final String GET_ALL_USERS_LOG = "Получен GET-запрос на получение всех пользователей.";
    private static final String GET_FRIENDS_LOG = "Получен GET-запрос на получение всех друзей пользователя {}.";
    private static final String GET_COMMON_FRIENDS_LOG =
            "Получен GET-запрос на получение общих друзей пользователя {} с пользователем {}.";

    private final UserService userService;

    @GetMapping(ID_PATH)
    public User findUserById(@PathVariable Long id) {
        log.info(GET_USER_LOG, id);
        return userService.findUserById(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info(GET_ALL_USERS_LOG);
        return userService.findAll();
    }

    @GetMapping(FRIENDS_PATH)
    public Collection<User> getFriends(@PathVariable Long id) {
        log.info(GET_FRIENDS_LOG, id);
        return userService.getFriends(id);
    }

    @GetMapping(COMMON_FRIENDS_PATH)
    public Collection<User> getCommonFriends(
            @PathVariable("id") Long userId,
            @PathVariable Long otherId
    ) {
        log.info(GET_COMMON_FRIENDS_LOG, userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен POST-запрос на создание пользователя: {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя: {}", user);
        return userService.update(user);
    }

    @PutMapping(FRIEND_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putFriend(
            @PathVariable("id") Long userId,
            @PathVariable Long friendId
    ) {
        log.info("Получен PUT-запрос на добавление в друзья пользователя {} пользователю {}", friendId, userId);
        userService.putFriend(userId, friendId);
    }

    @DeleteMapping(FRIEND_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(
            @PathVariable("id") Long userId,
            @PathVariable Long friendId
    ) {
        log.info("Получен DELETE-запрос на удаление друга {} у пользователя {}", friendId, userId);
        userService.deleteFriend(userId, friendId);
    }
}