package springbook.user.dao;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @BeforeEach
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/testdb", "spring", "book", true
        );
    }

    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), equalTo(0));

        User user1 = new User("ginseng", "홍상원", "red");
        User user2 = new User("moni", "cat", "meow");

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
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {

            userDao.deleteAll();
            assertThat(userDao.getCount(), equalTo(0));

            userDao.get("unknown");

        });

    }

    @Test
    public void count() throws ClassNotFoundException, SQLException {
        User user = new User("ginseng", "홍상원", "red");
        User user2 = new User("moni", "cat", "meow");

        userDao.deleteAll();
        assertThat(userDao.getCount(), equalTo(0));

        userDao.add(user);
        assertThat(userDao.getCount(), equalTo(1));

        userDao.add(user2);
        assertThat(userDao.getCount(), equalTo(2));
    }


}