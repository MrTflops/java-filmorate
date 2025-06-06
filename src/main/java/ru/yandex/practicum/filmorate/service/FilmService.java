package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService { //логика обработки запросов

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film findFilmById(Long id) {
        log.info("Обработка GET-запроса на получение фильма по айди.");
        if (!filmStorage.checkId(id)) {
            log.warn("Фильм с id = {}, не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return filmStorage.findFilmById(id);
    }

    public Collection<Film> findAll() {
        log.info("Обработка GET-запроса на получение всех фильмов.");
        return filmStorage.findAll();
    }

    public Collection<Film> findPopular(Long count) {
        log.info("Обработка GET-запроса на получение популярных фильмов.");
        return filmStorage.findPopular(count);
    }

    public Film create(Film film) {
        log.info("Обработка POST-запроса на добавление фильма: {}", film);
        check(film);
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        log.info("Обработка PUT-запрос на обновление фильма: {}", newFilm);
        if (newFilm.getId() == null) {
            log.warn("Обновление отклонено — ID не указан");
            throw new ConditionsNotMetException("Id не указан");
        }
        if (!filmStorage.checkId(newFilm.getId())) {
            log.warn("Фильм с id = {}, не найден", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        Film film = filmStorage.findFilmById(newFilm.getId());

        if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
            film.setName(newFilm.getName());
        }

        if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()) {
            film.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null &&
                !newFilm.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) &&
                !newFilm.getReleaseDate().isAfter(LocalDate.now())) {
            film.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getDuration() != null && newFilm.getDuration() > 0) {
            film.setDuration(newFilm.getDuration());
        }
        return filmStorage.update(film);
    }

    public void putLike(Long filmId, Long userId) {
        if (!filmStorage.checkId(filmId)) {
            log.warn("Фильм с id = {}, не найден", filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (!userStorage.checkId(userId)) {
            log.warn("Пользователь с id = {}, не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Film film = filmStorage.findFilmById(filmId);

        if (film.getLikes().contains(userId)) {
            log.warn("Фильм с id = {}, лайк уже поставлен", filmId);
            throw new NotFoundException("Фильму с id = " + filmId + " лайк уже поставлен.");
        }
        log.info("Лайк поставлен фильму с id {}.", filmId);
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!filmStorage.checkId(filmId)) {
            log.warn("Фильм с id = {}, не найден", filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (!userStorage.checkId(userId)) {
            log.warn("Пользователь с id = {}, не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Film film = filmStorage.findFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            log.warn("Фильм с id = {}, лайк не поставлен", filmId);
            throw new NotFoundException("Фильму с id = " + filmId + " лайк не поставлен.");
        }
        log.info("Лайк фильму с id {}, удален.", filmId);
        film.getLikes().remove(userId);
    }

    private void check(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Валидация не пройдена — имя фильма отсутствует");
            throw new ValidationException("Имя указано неверно");
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.warn("Валидация не пройдена — описание превышает 200 символов");
            throw new ValidationException("Количество символов превышает допустимое количество");
        } else if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) ||
                film.getReleaseDate().isAfter(LocalDate.now())) {
            log.warn("Валидация не пройдена — слишком старая дата релиза: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза указана неверно");
        } else if (film.getDuration() == null || film.getDuration() <= 0) {
            log.warn("Валидация не пройдена — неверная продолжительность: {}", film.getDuration());
            throw new ValidationException("Продолжительность указана неверно");
        }
    }
}