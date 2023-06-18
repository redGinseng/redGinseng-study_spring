package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 최초 UserDAO는 DB 연결 방법을 확장하려면 DAO내부를 변경해야하는 불편한 상황 이었다.
 * UserDAO는 개방폐쇄의 원칙에 맞춰 수정되었다.
 * <p>
 * 개방 폐쇄의 원칙 :
 * UserDao는 DB연결방법이라는 기능을 확장하는데 열려있다.
 * UserDao에 전혀 영향을 주지 않고도, ConnectionMaker Interface와 그 구현체를 수정하는 것만으로 기능을 확장할 수 있다.
 */
public class UserDao {


    // 읽기전용 정보.
    // DaoFactory에서 ConnectionMaker Bean을 붙여 생성 될 때 딱 한번 싱글톤 오브젝트로 생성된다.
    // 스프링의 싱글톤 레지스트리에서 관리되어, 신경쓸 필요가 없다.
    // 스프링 빈의 기본스코프는 싱글톤이다. 싱글톤 스코프는 컨테이너 내에 한 개의 오브젝트만 만들어져서,
    // 강제로 제거하지 않는 한 스프링 컨테이너가 존재하는 동안 계속 유지한다.
    private DataSource dataSource;

    // 매번 새로운 값으로 바뀌는 정보를 담은 인스턴스 변수.
    // 이런 오브젝트는 싱글톤 스코프로 관리할 수 없겠다.
    private Connection c;
    private User user;


    //의존관계 주입을 생성자에서 setter 방식으로 변경
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();
//        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;

        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user==null) throw new EmptyResultDataAccessException(1);

        return user;
    }


    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        System.out.println(count);

        rs.close();
        ps.close();
        c.close();

        return count;
    }

}
