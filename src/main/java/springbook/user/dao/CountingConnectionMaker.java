package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

    private int counter = 0;
    private ConnectionMaker realConncetionMaker;

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.realConncetionMaker = connectionMaker;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return realConncetionMaker.makeConnection();
    }

    public int getCounter() {
        return this.counter;
    }


}
