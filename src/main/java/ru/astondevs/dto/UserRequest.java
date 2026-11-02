package ru.astondevs.dto;

import jakarta.validation.constraints.*;

public class UserRequest {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Неправильный формат почты")
    @Size(max = 50)
    private String email;

    @Min(0)
    @Max(150)
    private int age;

    public UserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}