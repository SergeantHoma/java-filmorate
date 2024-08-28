package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

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

    public Map<Long, Mpa> getMpaByFilmIds(List<Long> filmIds) {
        String sql = "SELECT f.id AS film_id, m.id AS mpa_id, m.name FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.id " +
                "WHERE f.id IN (:filmIds)";

        Map<Long, Mpa> mpaByFilmId = new HashMap<>();

        jdbc.query(sql, (PreparedStatementSetter) Map.of("filmIds", filmIds), rs -> {
            Long filmId = rs.getLong("film_id");

            // Создаем объект Mpa и устанавливаем значения через сеттеры
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("name"));

            mpaByFilmId.put(filmId, mpa);
        });

        return mpaByFilmId;
    }
}
