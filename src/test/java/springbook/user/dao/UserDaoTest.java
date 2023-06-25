package springbook.user.dao;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
class UserDaoTest {

    /*
        main으로 실행되는 테스트 코드는, 그 역시도 제어권을 스스로가 갖고 있었다.
        이제 테스트도 프레임워크로 동작하도록 JUnit으로 옮겨보자

     */
    @Autowired
    private ApplicationContext applicationContext;


    // 테스트를 수행하는데 필요한 정보나 오브젝트를 픽스쳐라고 한다.
    // UserDao 와 같은 픽스쳐는 모든 테스트에서 쓰이니, 밖으로 추출한다.
    @Autowired
    private UserDao userDao;

    User user1;
    User user2;

    @BeforeEach
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource(
            "jdbc:mysql://localhost/testdb", "spring", "book", true
        );

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

//    @Test
//    public void getUserFailure() throws SQLException, ClassNotFoundException {
//        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
//
//            userDao.deleteAll();
//            assertThat(userDao.getCount(), equalTo(0));
//
//            userDao.get("unknown");
//
//        });
//
//    }

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

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId(), equalTo(user2.getId()));
        assertThat(user1.getName(), equalTo(user2.getName()));
        assertThat(user1.getPassword(), equalTo(user2.getPassword()));

    }
}