package springbook.user.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionHandler implements InvocationHandler {

    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;


    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 요청을 넘겨줄 타겟을, Object로 선언한 덕에
        // 핵심 로직을 처리하는 타겟에게 바로 안 넘기고, 다른 프록시에 넘겨도 되겠구나

        //pattern을 "get"으로 주면 "get"으로 시작하는 모든 메소드에 트랜잭션이 적용된다.
        //
        if (method.getName().startsWith(pattern)) {
            return invokeInTransaction(method, args);
        } else {
            return method.invoke(target, args);
        }
    }


    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status =
            this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = method.invoke(target, args);
            this.transactionManager.commit(status);

            return ret;
        } catch (InvocationTargetException e) {
            this.transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }

}
