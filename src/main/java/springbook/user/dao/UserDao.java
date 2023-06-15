package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDao의 관심사
 * - 데이터를 어떻게 등록하고 불러올 것인가
 *
 */
public class UserDao {

    private ConnectionMaker connectionMaker;

    // DB Connection 관심사를 다루는 interface를 생성한 이유는, 인터페이스의 사용자(DUserDao)는 DB Connection 에 대해서는 신경을 끌 수 있게 해주기 위함이다.
    // 인터페이스를 만들었다고는 하지만, UserDao를 사용하기 위해서는 여전히 conncetionMaker 구현체의 타입(DConnectionMaker)을 알아야 한다.
    // 아직도 UserDao만 전해주고, 마음껏 DB Connect 을 구현하라고 할 수 없다.
    public UserDao(){
        connectionMaker = new DConnectionMaker();
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public User get(String id) throws ClassNotFoundException, SQLException {

        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao dao = new UserDao();

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
