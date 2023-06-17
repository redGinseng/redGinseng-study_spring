package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 인터페이스를 생성하여, 구현부를 숨긴다.
 * 인터페이스를 통해 접근하는 쪽에서는, 오브젝트를 만들때 사용할 클래스가 무엇인지 몰라도 된다.
 * 구현부가 바뀌어도, 가져다 쓰는 쪽에서는 몰라도 된다
 */
public interface ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException;

}
