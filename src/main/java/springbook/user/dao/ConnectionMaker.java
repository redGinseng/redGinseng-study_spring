package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 인터페이스를 생성하여, 구현부를 숨긴다.
 * 인터페이스를 통해 접근하는 쪽에서는, 오브젝트를 만들때 사용할 클래스가 무엇인지 몰라도 된다.
 * 구현부가 바뀌어도, 가져다 쓰는 쪽에서는 몰라도 된다.
 *
 * 반대로 생각해서, 실제 구현부(subClass)는, 자신이 언제 실행되는지 알 수 없다.
 * 구현부는 상위 템플릿 메소드에 제어권을 넘기고, 자신은 필요할때 호출되어 사용되도록 한다.
 */
public interface ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException;

}
