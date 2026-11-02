
package ru.astondevs.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.astondevs.dto.UserRequest;
import ru.astondevs.dto.UserResponse;
import ru.astondevs.service.UserService;
import ru.astondevs.exception.UserNotFoundException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnUsers() throws Exception {

        UserResponse user = new UserResponse(
                1L,
                "Test",
                "test@example.com",
                25,
                null
        );

        when(userService.getAllUsers())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].age").value(25));

        verify(userService, times(1)).getAllUsers();
    }


    @Test
    void getAllUsers_shouldReturnEmptyList() throws Exception {

        when(userService.getAllUsers())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {

        UserResponse user = new UserResponse(
                1L,
                "Test",
                "test@example.com",
                25,
                null
        );

        when(userService.getUserById(1L))
                .thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService, times(1)).getUserById(1L);
    }


    @Test
    void getUserById_shouldReturn404_whenUserNotFound() throws Exception {

        when(userService.getUserById(999L))
                .thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(get("/api/users/999"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(999L);
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {

        UserResponse response = new UserResponse(
                1L,
                "Test",
                "test@example.com",
                25,
                null
        );

        when(userService.createUser(any(UserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Test",
                                  "email": "test@example.com",
                                  "age": 25
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService, times(1))
                .createUser(any(UserRequest.class));
    }


    @Test
    void createUser_shouldReturn400_whenNameIsBlank() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "",
                                  "email": "test@example.com",
                                  "age": 25
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }


    @Test
    void createUser_shouldReturn400_whenEmailIsInvalid() throws Exception {

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Test",
                                  "email": "invalid-email",
                                  "age": 25
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {

        UserResponse response = new UserResponse(
                1L,
                "Updated",
                "updated@example.com",
                30,
                null
        );

        when(userService.updateUser(
                eq(1L),
                any(UserRequest.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Updated",
                                  "email": "updated@example.com",
                                  "age": 30
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.age").value(30));

        verify(userService, times(1))
                .updateUser(eq(1L), any(UserRequest.class));
    }

    @Test
    void updateUser_shouldReturn404_whenUserNotFound() throws Exception {

        when(userService.updateUser(
                eq(999L),
                any(UserRequest.class)
        )).thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(put("/api/users/999")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Updated",
                                  "email": "updated@example.com",
                                  "age": 30
                                }
                                """))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, times(1))
                .updateUser(eq(999L), any(UserRequest.class));
    }

    @Test
    void deleteUser_shouldReturn200() throws Exception {

        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1))
                .deleteUser(1L);
    }


    @Test
    void deleteUser_shouldReturn404_whenUserNotFound() throws Exception {

        doThrow(new UserNotFoundException(999L))
                .when(userService).deleteUser(999L);

        mockMvc.perform(delete("/api/users/999"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, times(1))
                .deleteUser(999L);
    }

}

