package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

/**
 * 최초 UserDAO는 DB 연결 방법을 확장하려면 DAO내부를 변경해야하는 불편한 상황 이었다. UserDAO는 개방폐쇄의 원칙에 맞춰 수정되었다.
 * <p>
 * 개방 폐쇄의 원칙 : UserDao는 DB연결방법이라는 기능을 확장하는데 열려있다. UserDao에 전혀 영향을 주지 않고도, ConnectionMaker Interface와 그 구현체를 수정하는 것만으로
 * 기능을 확장할 수 있다.
 */
public class UserDao {

    private DataSource dataSource;
    private Connection c;
    private User user;

    //의존관계 주입을 생성자에서 setter 방식으로 변경
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
        StatementStrategy strategy = new AddStatement(user);
        jdbcContextWithStatementStrategy(strategy);
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


    public void deleteAll() throws SQLException {
       StatementStrategy strategy = new DeleteAllStatement();
       jdbcContextWithStatementStrategy(strategy);
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
