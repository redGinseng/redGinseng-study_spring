package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.DuplicateUserIdException;
import springbook.user.domain.Level;
import springbook.user.domain.User;


// JDBC를 활용하는 경우 아래 DAO를 구현해서 쓰도록
public class UserDaoJdbc implements UserDao {

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
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));

                return user;
            }
        };

    // jdbcTemplate을 사용해서 간단하게
    public void add(User user) throws DuplicateUserIdException {

        this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) value (?,?,?,?,?,?)",
            user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());

    }

    // jdbcContext 클래스의 콜백을 사용하지말고, jdbcTemplate을 사용해서 내장콜백으로 만들자
    public void deleteAll() {
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
