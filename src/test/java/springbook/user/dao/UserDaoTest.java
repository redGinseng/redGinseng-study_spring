package springbook.user.dao;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
class UserDaoTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;

    User user1;
    User user2;

    List<User> users;

    @BeforeEach
    public void setUp() {
        user1 = new User("ginseng", "홍상원", "red", Level.BASIC, 49, 0);
        user2 = new User("moni", "cat", "meow", Level.BASIC, 50, 1);

        users = Arrays.asList(
            new User("ginseng", "홍상원", "red", Level.BASIC, 49, 0),
            new User("moni", "cat", "meow", Level.BASIC, 50, 0),
            new User("gu", "신지유", "meow", Level.SILVER, 60, 29),
            new User("erwins", "신승한", "meow", Level.SILVER, 60, 30),
            new User("madnite", "강명성", "meow", Level.GOLD, 100, 100)
        );

    }

    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), equalTo(0));

        userDao.add(user1);
        userDao.add(user2);

        User userGet1 = userDao.get(user1.getId());
        checkSameUser(userGet1, user1);
        User userGet2 = userDao.get(user2.getId());
        checkSameUser(userGet2, user2);

    }

    @Test
    public void count() throws ClassNotFoundException, SQLException {

        userDao.deleteAll();
        assertThat(userDao.getCount(), equalTo(0));

        userDao.add(user1);
        assertThat(userDao.getCount(), equalTo(1));

        userDao.add(user2);
        assertThat(userDao.getCount(), equalTo(2));
    }

    @Test
    public void TestAll() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();

        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size(), equalTo(1));
        checkSameUser(user1, users1.get(0));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size(), equalTo(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), equalTo(user2.getId()));
        assertThat(user1.getName(), equalTo(user2.getName()));
        assertThat(user1.getPassword(), equalTo(user2.getPassword()));
        assertThat(user1.getLevel(), equalTo(user2.getLevel()));
        assertThat(user1.getLogin(), equalTo(user2.getLogin()));
        assertThat(user1.getRecommend(), equalTo(user2.getRecommend()));
    }

    @Test
    public void duplicateKey() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDao.deleteAll();
            userDao.add(user1);
            userDao.add(user1);
        });
    }

    @Test
    public void update() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("홍상원");
        user1.setPassword("ginseng");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDao.update(user1);

        User user1update = userDao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2update = userDao.get(user2.getId());
        checkSameUser(user2, user2update);

    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.BASIC);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);

    }

    private void checkLevel(User user, Level level) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), equalTo(level));

    }

}