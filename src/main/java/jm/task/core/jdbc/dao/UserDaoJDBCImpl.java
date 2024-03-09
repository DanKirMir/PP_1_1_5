package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection;
    Util util;

    public UserDaoJDBCImpl() {
        util = new Util();
        connection = util.getConnection();
    }

    public void createUsersTable() {
        String create = "CREATE TABLE `mydatabase_v1`.`users` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  `lastName` VARCHAR(45) NOT NULL,\n" +
                "  `age` INT(3) NOT NULL,\n" +
                "  PRIMARY KEY (`id`))";
        try (Statement statement = connection.createStatement()) {
            statement.execute(create);
            System.out.println("Была создана таблица базы данных \"users\" ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void dropUsersTable() {
        String drop = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(drop);
            System.out.println("Таблица базы данных \"users\" была удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String save = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(save)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("Пользователь с именем:" + name + "; с фамилией:" + lastName + "; с возрастом:" + age + " был сохранен");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeUserById(long id) {
        String remove = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(remove)) {
            preparedStatement.setLong(1, id);
            System.out.println((preparedStatement.executeUpdate() > 0) ? "Пользователь с id: " + id + " был удален"
                    : "Пользователь с id: " + id + " не был удален. Неверный id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        String getAll = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User tempUser = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));
                tempUser.setId(resultSet.getLong("id"));
                result.add(tempUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Все пользователи таблицы \"users\" были получены");
        return result;
    }


    public void cleanUsersTable() {
        String clean = "TRUNCATE TABLE users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(clean);
            System.out.println("Таблица \"users\" была очищена");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
