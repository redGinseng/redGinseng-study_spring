package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


// Strategy Interface
// 메서드마다 다른 부분을 strategy로 분리시키고, 새로운 strategy가 필요할 때마다 이 인터페이스를 구현하도록 하여, 확장시킨다.
public interface StatementStrategy {

    PreparedStatement makePreparedStatement(Connection c) throws SQLException;

}
