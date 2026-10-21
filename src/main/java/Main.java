import config.HibernateUtil;
import dao.UserDAO;
import dao.UserDAOImpl;
import entity.User;
import validation.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final UserDAO USER_DAO = new UserDAOImpl();
    private static final UserValidator USER_VALIDATOR = new UserValidator();
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            boolean running = true;

            LOGGER.info("Приложение запущено");

            while (running) {
                printMenu();

                try {
                    switch (readInt("Выберите действие: ")) {
                        case 1 -> createUser();
                        case 2 -> findUserById();
                        case 3 -> showAllUsers();
                        case 4 -> updateUser();
                        case 5 -> deleteUser();
                        case 0 -> running = false;
                        default -> System.out.println("Неизвестная команда.");
                    }
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Ошибка ввода: {}", e.getMessage());
                    System.out.println("Ошибка ввода: " + e.getMessage());
                } catch (RuntimeException e) {
                    LOGGER.error("Ошибка при выполнении операции", e);
                    System.out.println("Не удалось выполнить операцию. Проверьте данные и повторите попытку.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
            SCANNER.close();
        }
    }

    private static void printMenu() {
        System.out.println("""
                
                ===== USER SERVICE =====
                1. Создать пользователя
                2. Найти пользователя по ID
                3. Показать всех пользователей
                4. Обновить пользователя
                5. Удалить пользователя
                0. Выход
                """);
    }

    private static void createUser() {
        String name = readText("Имя: ");
        String email = readText("Email: ");
        int age = readInt("Возраст: ");

        User user = new User(name, email, age);

        USER_VALIDATOR.validate(user);
        User createdUser = USER_DAO.create(user);

        System.out.println("Создан пользователь: " + createdUser);
        LOGGER.info("Создан пользователь с ID {}", createdUser.getId());
    }

    private static void findUserById() {
        long id = readLong("ID пользователя: ");

        Optional<User> user = USER_DAO.findById(id);

        if (user.isPresent()) {
            System.out.println(user.get());
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private static void showAllUsers() {
        List<User> users = USER_DAO.findAll();

        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
            return;
        }

        users.forEach(System.out::println);
    }

    private static void updateUser() {
        long id = readLong("ID пользователя для изменения: ");

        Optional<User> optionalUser = USER_DAO.findById(id);

        if (optionalUser.isEmpty()) {
            System.out.println("Пользователь не найден.");
            return;
        }

        User user = optionalUser.get();

        System.out.println("Текущие данные: " + user);

        String name = readText("Новое имя: ");
        String email = readText("Новый email: ");
        int age = readInt("Новый возраст: ");

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        USER_VALIDATOR.validate(user);
        System.out.println("Обновлён пользователь: " + USER_DAO.update(user));
        LOGGER.info("Обновлён пользователь с ID {}", id);
    }

    private static void deleteUser() {
        long id = readLong("ID пользователя для удаления: ");

        if (USER_DAO.deleteById(id)) {
            System.out.println("Пользователь удалён.");
            LOGGER.info("Удалён пользователь с ID {}", id);
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private static long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Введите корректный ID.");
            }
        }
    }
}
