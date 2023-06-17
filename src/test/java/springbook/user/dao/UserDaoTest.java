package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    /*
     기존에 내가 이해하던 JAVA는 main 에서 사용할 object를 결정하고, 생성하고, 메소드가 호출되고..
     제어의 역전:
     오브젝트가 자신이 사용할 오브젝트를 스스로 선택하지 않는다. 생성하지도 않는다. 자신도 어디서 만들어지고 사용될지 알 수 없다.
     프로그램의 엔드포인트 정도만 제외하면, 모든 오브젝트는 위임받은 제어권한을 갖는 특별한 오브젝트에 의해 결정되고 만들어진다.
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao dao = new DaoFactory().userDao();

        User user = new User();
        user.setId("ginseng");
        user.setName("홍상원");
        user.setPassword("red");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }


}