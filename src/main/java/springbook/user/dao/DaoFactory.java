package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application Context 를 적용한다.
 *
 */

@Configuration  //Application Context가 사용할 설정정보라는 표시, 사실 XML과 같은 설정정보라 봐도 무방한 클래스
public class DaoFactory {

    @Bean   // UserDao 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
    public UserDao userDao() {
        UserDao userDao = new UserDao(connectionMaker());
        return userDao;
    }

    @Bean // ConncetionMaker 타입의 오브젝트를 생성해주는 메소드이므로 @Bean을 붙여준다
    public ConnectionMaker connectionMaker(){
        ConnectionMaker connectionMaker = new DConnectionMaker();

        return connectionMaker;
    }
}
