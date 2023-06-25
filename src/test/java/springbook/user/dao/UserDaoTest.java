package springbook.user.dao;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
class UserDaoTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;

    User user1;
    User user2;

    @BeforeEach
    public void setUp() {

        user1 = new User("ginseng", "홍상원", "red");
        user2 = new User("moni", "cat", "meow");

    }

    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), equalTo(0));

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), equalTo(2));

        User userGet1 = userDao.get(user1.getId());
        User userGet2 = userDao.get(user2.getId());

        assertThat(user1.getName(), equalTo(userGet1.getName()));
        assertThat(user1.getPassword(), equalTo(userGet1.getPassword()));
        assertThat(user2.getName(), equalTo(userGet2.getName()));
        assertThat(user2.getPassword(), equalTo(userGet2.getPassword()));
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

    }

    @Test
    public void duplicateKey() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDao.deleteAll();
            userDao.add(user1);
            userDao.add(user1);
        });
    }


}