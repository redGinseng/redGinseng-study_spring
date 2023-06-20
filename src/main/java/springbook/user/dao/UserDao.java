package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;


public class UserDao {

    private DataSource dataSource;
    private Connection c;
    private User user;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        // strategy 가 늘어남에따라 Class 파일도 늘어나는게 부담스럽다면 UserDao 메서드 안에 내부 클래스로 박아버리자
        class AddStatement implements StatementStrategy {

            User user;

            public AddStatement(User user) {
                this.user = user;
            }

            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps =
                    c.prepareStatement("insert into  users (id, name, password) values (?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        }

        StatementStrategy strategy = new AddStatement(user);
        jdbcContextWithStatementStrategy(strategy);
    }

    public void deleteAll() throws SQLException {
        jdbcContextWithStatementStrategy(
            // 익명 내부 클래스를 사용해보자. 클래스 선언과 오브젝트 생성이 결합한 형태.
            // 상속할 클래스나 구현할 인터페이스를, 생성자 대신 사용
            // 클래스를 재사용할 필요가 없고 구현한 인터페이스 타입으로만 사용할 경우
            new StatementStrategy() {
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {

                    return c.prepareStatement("delete from users");
                }
            }
        );
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();
//        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;

        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }


    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = -1;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            return count;
        } catch (Exception e) {
            throw e;
        } finally {
            if (c != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }
        }
    }

}
