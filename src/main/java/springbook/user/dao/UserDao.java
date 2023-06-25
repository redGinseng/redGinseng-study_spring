package springbook.user.dao;

import java.util.List;
import springbook.user.domain.User;


//jdbc 외에도 jpa, hibrernate, orm 등 다른 DAO 를 사용하기 위해서 DAO를 인터페이스로 분리해두자
public interface UserDao {

    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

}
