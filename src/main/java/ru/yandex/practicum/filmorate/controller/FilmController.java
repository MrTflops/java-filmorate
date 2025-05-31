package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController { //работа с запросами

    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film findFilmById(
            @PathVariable Long id
    ) {
        log.info(String.format("Получен GET-запрос на получение фильма с id = %d", id));
        return filmService.findFilmById(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен GET-запрос на получение всех фильмов.");
        return filmService.findAll();
    }

    @GetMapping("/popular")
    public Collection<Film> findPopular(@RequestParam(required = false, defaultValue = "10") Long count) {
        log.info(String.format("Получен GET-запрос на получение %d популярных фильмов.", count));
        return filmService.findPopular(count);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info(String.format("Получен POST-запрос на добавление фильма: %s", film));
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info(String.format("Получен PUT-запрос на обновление фильма: %s", film));
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putLike(
            @PathVariable("id") Long filmId,
            @PathVariable Long userId
    ) {
        log.info(String.format("Получен PUT-запрос на постановку лайка фильму id = %d от пользователя id = %d", filmId, userId));
        filmService.putLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(
            @PathVariable("id") Long filmId,
            @PathVariable Long userId
    ) {
        log.info(String.format("Получен DELETE-запрос на удаление лайка фильму id = %d от пользователя id = %d", filmId, userId));
        filmService.deleteLike(filmId, userId);
    }
}