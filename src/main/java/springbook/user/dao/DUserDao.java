package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * 템플릿 메소드 패턴 (template method pattern):
 * 슈퍼클래스(UserDao)에서 기본적인 로직의 흐름을 만들고,
 * 그 기능의 일부를 추상 메소드나 오버라이딩이 가능한 protected 메소드 등으로 만든 뒤,
 * 서브클래스(DUserDao, NUserDao)에서 메소드를 필요에 맞게 구현해서 사용하도록 하는 방법
 *
 */
public class DUserDao extends UserDao {
    public Connection getConnection() throws ClassNotFoundException,
            SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8",
                "spring", "book");
        return c;
    }
}
