package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    public static void main(String[] args) {
        ConnectionMaker connectionMaker = new DConnectionMaker();

        // UserDao 생성
        // 사용할 ConnectionMaker 타입의 오브젝트 제공
        // 두 오브젝트(UserDao, DConnectionMaker) 사이의 의존관계를 설정하는 효과
        // 런타임에서 UserDaoTest 가 UserDao를 생성할 때 의존관계가 생성된다.
        // 이렇게 UserDao에 있어서는 안됐던 관심사를 클라이언트로 떠넘기는 작업이 끝났다.
        // 클라이언트 UserDaoTest 가 UserDao<->DConncetionMaker 간의 관계를 매핑해주는 덕분에
        // D사는 자신들을 위한 DB접속클래스를 만들어서 UserDao를 사용할 수 있게 되었다.
        UserDao dao = new UserDao(connectionMaker);

    }
}