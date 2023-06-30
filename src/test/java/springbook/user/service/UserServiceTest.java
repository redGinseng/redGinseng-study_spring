package springbook.user.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;
import static springbook.user.service.CommonUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.CommonUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
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

    @Autowired
    MailSender mailSender;

    @Autowired
    PlatformTransactionManager transactionManager;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
            new User("ginseng", "홍상원", "red", "hsw0130@naver.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
            new User("moni", "cat", "meow", "hsw0130@naver.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
            new User("gu", "신지유", "meow", "hsw0130@naver.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            new User("erwins", "신승한", "meow", "hsw0130@naver.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("madnite", "강명성", "meow", "hsw0130@naver.com", Level.SILVER, 100, Integer.MAX_VALUE)
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
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        try {
            testUserService.upgradeLevels();
            testUserService.setMailSender(mailSender);
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException | SQLException e) {

        }

        checkLevelUpgraded(users.get(1), false);

    }

    @Test
    @DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려준다.
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        //userService에 dummyMailSender 대신 mockMailSender를 DI
        userService.setMailSender(mockMailSender);

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), equalTo(5));
        //레벨 업그레이드에 성공하면, 메일이 전송되었을 것이다. 메일을 보냈어야할 1,3번째 request에 담긴 이메일 주소 검증
        assertThat(request.get(0), equalTo(users.get(1).getEmail()));
        assertThat(request.get(1), equalTo(users.get(3).getEmail()));
    }


    //  목 오브젝트를 만들어보자
    // 메일을 실제 보내지 않아도, 메일이 전송되는 지는 확인해볼 필요가 있다.
    static class MockMailSender implements MailSender {

        private List<String> requests = new ArrayList<>();


        public List<String> getRequests() {
            return requests;
        }

        // UserService 가 send를 통해 이 클래스를 불러서 메일 전송 요청을 보냈을 때, 관련 정보를 저장해두는 기능
        // 첫 번째 수신자 메일 주소를 꺼내와 저장
        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
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