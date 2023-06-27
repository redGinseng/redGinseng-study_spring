package springbook.user.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserServiceTest.TestUserService.TestUserServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    private List<User> users;
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
            new User("ginseng", "홍상원", "red", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            new User("moni", "cat", "meow", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("gu", "신지유", "meow", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            new User("erwins", "신승한", "meow", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("madnite", "강명성", "meow", Level.SILVER, 100, Integer.MAX_VALUE)
        );
    }


    @Test
    public void bean() {
        assertThat(this.userService, notNullValue());
    }

    @Test
    public void upgradeLevel() throws SQLException {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

    }


    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), equalTo(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), equalTo(user.getLevel()));
        }

    }

    @Test
    public void upgradeAllOrNoting() {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao); // userDAO를 수동 DI 해준다
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException | SQLException e) {

        }

        checkLevelUpgraded(users.get(1), false);

    }


    static class TestUserService extends UserService {

        private String id;

        private TestUserService(String id) {
            this.id = id;
        }


        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

        static class TestUserServiceException extends RuntimeException {

        }
    }


}