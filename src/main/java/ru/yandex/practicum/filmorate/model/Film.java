package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashSet;

@Data
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    final Set<Long> likes = new HashSet<>();
    Set<Genre> genres = new LinkedHashSet<>();
    Mpa mpa;
}