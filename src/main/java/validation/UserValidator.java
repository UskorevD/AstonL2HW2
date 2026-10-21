package validation;

import entity.User;

import java.util.regex.Pattern;

public class UserValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public void validate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }

        if (user.getName().length() > 100) {
            throw new IllegalArgumentException("Имя не должно превышать 100 символов.");
        }

        if (user.getEmail() == null ||
                !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Укажите корректный email.");
        }

        if (user.getEmail().length() > 255) {
            throw new IllegalArgumentException("Email не должен превышать 255 символов.");
        }

        if (user.getAge() < 0 || user.getAge() > 150) {
            throw new IllegalArgumentException("Возраст должен быть от 0 до 150 лет.");
        }
    }
}
