package jm.task.core.jdbc.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД

    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/mydatabase_v1";
    private final String USERNAME = "root";
    private final String PASSWORD = "Napoli4644718";


    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Соединение установлено и не закрыто");
                return connection;
            } else {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Новое соодинение установлено");
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


}


















