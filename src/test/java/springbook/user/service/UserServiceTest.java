package springbook.user.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

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
            new User("madnite", "강명성", "meow", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }


    @Test
    public void bean() {
        assertThat(this.userService, notNullValue());
    }

    @Test
    public void upgradeLevel() {
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


}