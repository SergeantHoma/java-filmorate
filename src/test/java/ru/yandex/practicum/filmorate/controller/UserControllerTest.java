/*
package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private User userTest;

    @BeforeEach
    void setUp() {
        userTest = userTest.builder()
                .email("Gaf@gaf.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Test
    void createCorrectUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(userTest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithNullName() throws Exception {
        userTest.setName(null);
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(userTest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithIncorrectEmail() throws Exception {
        userTest.setEmail("a-ds?dsdsyandex.ru@");
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userTest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithNullEmail() throws Exception {
        userTest.setEmail(null);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userTest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithIncorrectLogin() throws Exception {
        userTest.setLogin("test user");
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userTest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithNullBirthDate() throws Exception {
        userTest.setBirthday(null);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userTest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithFutureBirthDate() throws Exception {
        userTest.setBirthday(LocalDate.of(2025, 1, 1));
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userTest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}*/
