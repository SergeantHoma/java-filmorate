package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAllGenres() {
        String findAllQuery = "SELECT * FROM genres";
        return findMany(findAllQuery);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        String findByGenreIdQuery = "SELECT * FROM genres WHERE id = ?";
        return findOne(findByGenreIdQuery, genreId);
    }

    public Collection<Genre> getGenresByFilmId(Long filmId) {
        String findByFilmIdQuery = "SELECT g.* FROM genres g JOIN film_genre fg " +
                "ON g.id = fg.genre_id WHERE film_id = ? ORDER BY g.id";
        return findMany(findByFilmIdQuery, filmId);
    }

    public Map<Long, List<Genre>> getGenresByFilmIds(Collection<Long> filmIds) {
        String query = "SELECT fg.film_id, g.id AS genre_id, g.name AS genre_name " +
                "FROM film_genre fg " +
                "JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id IN (" + String.join(",", Collections.nCopies(filmIds.size(), "?")) + ")";

        return jdbc.query(query, (rs, rowNum) -> {
                    Long filmId = rs.getLong("film_id");
                    Integer genreId = rs.getInt("genre_id");
                    String genreName = rs.getString("genre_name");

                    Genre genre = new Genre();
                    genre.setId(genreId);
                    genre.setName(genreName);

                    return new AbstractMap.SimpleEntry<>(filmId, genre);
                }, filmIds.toArray())
                .stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }
}
