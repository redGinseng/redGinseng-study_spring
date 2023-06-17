package springbook.user.dao;

/**
 * DaoFactory는 오브젝트를 구성하고 관계를 정의하는 책임
 * 새로운 ConnectionMaker 구현 클래스로 변경이 필요하면
 * DaoFactory를 수정해서 변경된 클래스를 생성해 설정하도록 코드를 수정
 */
public class DaoFactory {
    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }
}
