package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class MpaDbStorage extends BaseDbStorage<Mpa> {
    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> getAllMpa() {
        String findAllMpa = "SELECT * FROM mpa";
        return findMany(findAllMpa);
    }

    public Optional<Mpa> getMpaById(Integer mpaId) {
        String findMpaById = "SELECT * FROM mpa WHERE id = ?";
        return findOne(findMpaById, mpaId);
    }

    public Map<Long, Mpa> getMpasByFilmIds(List<Long> filmIds) {
        String query = "SELECT f.id AS film_id, m.id AS mpa_id, m.name AS mpa_name " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.id " +
                "WHERE f.id IN (" + String.join(",", Collections.nCopies(filmIds.size(), "?")) + ")";

        return jdbc.query(query, (rs, rowNum) -> {
                    Long filmId = rs.getLong("film_id");
                    Integer mpaId = rs.getInt("mpa_id");
                    String mpaName = rs.getString("mpa_name");

                    Mpa mpa = new Mpa();
                    mpa.setId(mpaId);
                    mpa.setName(mpaName);

                    return new AbstractMap.SimpleEntry<>(filmId, mpa);
                }, filmIds.toArray())
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}
