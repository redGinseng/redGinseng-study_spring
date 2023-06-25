package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.DuplicateUserIdException;
import springbook.user.domain.User;


public class UserDao {

    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    private RowMapper<User> userMapper =
        new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum)
                throws SQLException {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        };

    // jdbcTemplate을 사용해서 간단하게
//    public void add(User user) throws DuplicateUserIdException {
//        try {
//            this.jdbcTemplate.update("insert into users(id, name, password) value (?,?,?)",
//                user.getId(), user.getName(), user.getPassword());
//        } catch (SQLException e) {// 이미 근데 jdbcTemplate에서 DataAccessException (런타임 예외)으로 전환해주고 있다.
//            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
//                // 밖에서 받을 때 컨트롤 할 수 없거나, 아무 의미 없는 SQLException(checked Exception) 이라면, unchecked Exception 으로 예외전환하자
//                throw new DuplicateUserIdException(e);
//            } else {
//                throw new RuntimeException(e);
//            }
//        }
//    }


    // jdbcTemplate을 사용해서 간단하게
    public void add(User user) throws DuplicateUserIdException {

        this.jdbcTemplate.update("insert into users(id, name, password) value (?,?,?)",
            user.getId(), user.getName(), user.getPassword());

    }

    // jdbcContext 클래스의 콜백을 사용하지말고, jdbcTemplate을 사용해서 내장콜백으로 만들자
    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("delete from users");
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id= ?",
            new Object[]{id},
            this.userMapper);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id",
            this.userMapper);
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.TYPE);
    }


}
