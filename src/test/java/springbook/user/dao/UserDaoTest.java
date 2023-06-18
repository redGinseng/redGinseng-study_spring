package springbook.user.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import springbook.user.domain.User;


import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


class UserDaoTest {

    /*
        main으로 실행되는 테스트 코드는, 그 역시도 제어권을 스스로가 갖고 있었다.
        이제 테스트도 프레임워크로 동작하도록 JUnit으로 옮겨보자

     */

    @Test
    public void addAndGet() throws ClassNotFoundException,SQLException{
        ApplicationContext context = new
                ClassPathXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("ginseng");
        user.setName("홍상원");
        user.setPassword("red");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), equalTo(user.getName()));
        assertThat(user2.getPassword(), equalTo(user.getPassword()));
    }


}