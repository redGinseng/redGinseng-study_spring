package springbook.user.dao;

/**
 * 원래 ConnectionMaker의 구현 클래스를 결정하고, 오브젝트를 만드는 제어권은 UserDao에게 있었다.
 *  예전 UserDao.java
 *   ... ConnectionMaker cMaker = new DConnectionMaker();
 *
 * 제어권을 DaoFactory로 넘기고 (역전), UserDao는 수동적인 존재가 되었따.
 *
 */
public class DaoFactory {
    public UserDao userDao() {
        UserDao userDao = new UserDao(connectionMaker());
        return userDao;
    }

    public AccountDao accountDao(){
        return new AccountDao(connectionMaker());
    }

    public ConnectionMaker connectionMaker(){
        ConnectionMaker connectionMaker = new DConnectionMaker();

        return connectionMaker;
    }
}
