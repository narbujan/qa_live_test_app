package com.mintos.qa_live_test_app.controller;

import com.mintos.qa_live_test_app.model.User;
import com.mintos.qa_live_test_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void registerUser_ShouldReturnSavedUser() throws Exception {
        String userJson = """
                {
                    "email": "test@example.com",
                    "name": "John",
                    "surname": "Doe",
                    "personId": 12345,
                    "mintosEmployee": false
                }
                """;
        User savedUser = new User(1L, "test@example.com", "John", "Doe", 12345, false);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.personId").value(12345));
    }

    @Test
    void registerUser_ShouldReturn400_WhenPersonIdExists() throws Exception {
        String userJson = """
                {
                    "email": "test@example.com",
                    "name": "John",
                    "surname": "Doe",
                    "personId": 12345,
                    "mintosEmployee": false
                }
                """;
        User existingUser = new User(1L, "existing@example.com", "Old", "User", 12345, false);

        when(userRepository.getUserByPersonId(12345)).thenReturn(List.of(existingUser));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Person with person Id already registered"));
    }

    @Test
    void getUsers_ShouldReturnListOfUsers() throws Exception {
        User user = new User(1L, "test@example.com", "John", "Doe", 12345, false);
        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void getUserByIdentificationNumber_ShouldReturnUser() throws Exception {
        User user = new User(1L, "test@example.com", "John", "Doe", 12345, false);
        when(userRepository.getUserByPersonId(12345)).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(12345))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

	@Test
	void getUser_ShouldReturn404_WhenPersonIdNotExists() throws Exception {
		when(userRepository.getUserByPersonId(12345)).thenReturn(new ArrayList<>());

		mockMvc.perform(get("/api/users/12345"))
				.andExpect(status().isNotFound())
				.andExpect(status().reason("Person with person Id not found"));
	}
}
