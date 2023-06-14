package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

/**
 * UserDao의 관심사
 * 1. DB와 연결을 위한 커넥션을 어떻게 가져올까
 * - 어떤 DB를 쓸 것인가
 * - 어떤 드라이버를 쓸 것인가
 * - 어떤 로그인 정보를 쓰는데, 그 커넥션을 생성하는 방법은 또 어떤 것인가..
 * 2. 사용자 등록을 위해 DB에 보낼 SQL 문장을 담을 Statement를 만들고 실행하는 것
 * - 파라미터로 넘어온 사용자 정보를 Statement에 바인딩 시키고, Statement 에 담긴 SQL을 DB를 통해 실행시키는 방법
 * 3. 작업이 끝나면 사용한 리소스를 닫아서 시스템에 돌려주는 것
 */
public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public User get(String id) throws ClassNotFoundException, SQLException {

        Connection c = getConnection();
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

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook?characterEncoding=UTF-8", "spring", "book");

        return c;
    }

}
