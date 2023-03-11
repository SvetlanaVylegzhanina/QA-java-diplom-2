package apilogic;

import model.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    // Создание пользователя со всеми полями
    public static User getRandomUserWithAllFields() {
        String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);

        return new User(name, email, password);
    }

    // Создание пользователя без пароля
    public static User getRandomUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(8) + "@yandex.ru";
        String password = "";
        String name = RandomStringUtils.randomAlphabetic(8);

        return new User(name, email, password);
    }

    // Создание пользователя без email
    public static User getRandomUserWithoutEmail() {
        String email = "";
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);

        return new User(name, email, password);
    }

}
