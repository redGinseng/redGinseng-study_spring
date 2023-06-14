package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * NUserDao의 관심사
 * - DB 연결은 어떻게 할 것인가?
 *
 */
public class NUserDao extends UserDao {
    public Connection getConnection() throws ClassNotFoundException,
            SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8",
                "spring", "book");
        return c;
    }
}
