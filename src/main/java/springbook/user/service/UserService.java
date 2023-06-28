package springbook.user.service;

import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;

    public static final int MIN_RECOMMEND_FOR_GOLD = 100;

    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void upgradeLevels() throws SQLException {
        //어차피 Transaction을 다루는 기술도 다 공통적인 측면이 있으니, 추상화 할 수 있지 않을까?
        //스프링에서 제공하는 방식(JTA)으로 트랜잭션을 제어하는 트랜잭션 경계설정을 써보자
        TransactionStatus status=
            this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.commit(status);
            throw e;
        }
    }

    protected void upgradeLevel(User user) {
        userLevelUpgradePolicy.upgradeLevel(user);
    }

    private boolean canUpgradeLevel(User user) {
        return userLevelUpgradePolicy.canUpgradeLevel(user);
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

}
