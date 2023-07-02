package springbook.user.service;

import java.sql.SQLException;
import java.util.List;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {


    UserDao userDao;
    private MailSender mailSender;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

//    // 트랜잭션과 비지니스 로직이 완벽히 분리된, 서로 관계 없는 코드의 혼재
//    public void upgradeLevels() throws SQLException {
//        // 트랜잭션 시작
//        TransactionStatus status =
//            this.transactionManager.getTransaction(new DefaultTransactionDefinition());
//        // 트랜잭션 경계
//        // 비지니스 로직 시작
//        try {
//            List<User> users = userDao.getAll();
//            for (User user : users) {
//                if (canUpgradeLevel(user)) {
//                    upgradeLevel(user);
//                }
//            }
//        // 비지니스 로직 끝
//        // 트랜잭션 경계
//            this.transactionManager.commit(status);
//
//        } catch (Exception e) {
//            transactionManager.commit(status);
//            throw e;
//        }
//        // 트랜잭션 끝
//    }

    public void upgradeLevels() throws SQLException {
        TransactionStatus status =
            this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            upgradeLevelsInternal();
            this.transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.commit(status);
            throw e;
        }
    }


    protected void upgradeLevel(User user) {
        if (user.getLevel() == Level.BASIC) {
            user.setLevel(Level.SILVER);
        } else if (user.getLevel() == Level.SILVER) {
            user.setLevel(Level.GOLD);
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return (user.getLogin() >= 50);
            case SILVER:
                return (user.getRecommend() >= 30);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("unknown level:" + currentLevel);
        }

        return false;
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    private void sendUpgradeEMail(User user) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자의 등급이 " + user.getLevel());

        mailSender.send(mailMessage);

    }

    private void upgradeLevelsInternal(){
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
        // 비지니스 로직 끝
    }

}
