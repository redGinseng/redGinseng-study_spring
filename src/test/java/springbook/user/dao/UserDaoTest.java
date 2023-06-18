package springbook.user.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class UserDaoTest {

    /*
        main으로 실행되는 테스트 코드는, 그 역시도 제어권을 스스로가 갖고 있었다.
        이제 테스트도 프레임워크로 동작하도록 JUnit으로 옮겨보자

     */


    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), equalTo(0));

        User user = new User("ginseng", "홍상원", "red");
        User user2 = new User("moni", "cat", "meow");

        dao.add(user);
        dao.add(user2);
        assertThat(dao.getCount(), equalTo(1));

        User userGet1 = dao.get(user.getId());
        User userGet2 = dao.get(user2.getId());

        assertThat(user.getName(), equalTo(userGet1.getName()));
        assertThat(user.getPassword(), equalTo(userGet1.getPassword()));
        assertThat(user2.getName(), equalTo(userGet2.getName()));
        assertThat(user2.getPassword(), equalTo(userGet2.getPassword()));
    }


    @Test
    public void count() throws ClassNotFoundException, SQLException {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);


        User user = new User("ginseng", "홍상원", "red");
        User user2 = new User("moni", "cat", "meow");


        dao.deleteAll();
        assertThat(dao.getCount(), equalTo(0));


        dao.add(user);
        assertThat(dao.getCount(), equalTo(1));

        dao.add(user2);
        assertThat(dao.getCount(), equalTo(2));
    }


}