package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Film testFilm;

    @BeforeEach
    public void setup() {
        testFilm = Film.builder()
                .name("Test Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2000, 1, 13))
                .duration(120)
                .build();
    }

    @Test
    void createCorrectFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(testFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createIncorrectNameFilm() throws Exception {
        testFilm.setName("");
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(testFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createIncorrectDescriptionFilm() throws Exception {
        testFilm.setDescription("1".repeat(201));
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(testFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createIncorrectReleaseDateFilm() throws Exception {
        testFilm.setReleaseDate(LocalDate.of(1890, 3, 25));
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(testFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createIncorrectDurationFilm() throws Exception {
        testFilm.setDuration(-1);
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(testFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}