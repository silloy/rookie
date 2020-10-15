package me.silloy.study.squirrel.foundation.config;

import me.silloy.study.squirrel.foundation.enums.OrderContext;
import me.silloy.study.squirrel.foundation.enums.OrderEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.GenericTypeResolver;
import org.springframework.scheduling.Trigger;
import org.squirrelframework.foundation.fsm.*;
import org.squirrelframework.foundation.fsm.annotation.State;

/**
 * @author shaohuasu
 * @since 1.8
 */
public abstract class AbstractStateMachineEngine<T extends UntypedStateMachine> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    protected UntypedStateMachineBuilder stateMachineBuilder = null;
    @SuppressWarnings("unchecked")
    public AbstractStateMachineEngine() {
        //识别泛型参数
        Class<T> genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(),
                AbstractStateMachineEngine.class);
        stateMachineBuilder = StateMachineBuilderFactory.create(genericType, ApplicationContext.class);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }



//    //delegate fire
//    public void fire(int rmaId, State initialState, Trigger trigger, StateMachineContext context) {
//        T stateMachine = stateMachineBuilder.newUntypedStateMachine(
//                initialState,
//                //暂时开启debug进行日志trace
//                StateMachineConfiguration.create().enableDebugMode(true).enableAutoStart(true),
//                //注入applicationContext
//                applicationContext);
//        stateMachine.fire(trigger, context);
//    }




    public void fire(OrderEvent event, OrderContext context) {
        T stateMachine = stateMachineBuilder.newUntypedStateMachine(
                context.orderDTO.getState(),
                StateMachineConfiguration.create().enableDebugMode(true).enableAutoStart(true),
                applicationContext);
        stateMachine.fire(event, context);
    }


//    public void fire(int rmaId, State initialState, Trigger trigger, StateMachineContext context) {
//        JedisLock jedisLock = jedisLockFactory.buildLock(rmaId);
//        //争用分布式锁
//        if (jedisLock.tryLock()) {
//            try {
//                T stateMachine = stateMachineBuilder.newUntypedStateMachine(
//                        initialState
//                        //暂时开启debug进行日志trace
//                        StateMachineConfiguration.create().enableDebugMode(true).enableAutoStart(true),
//                        //注入applicationContext
//                        applicationContext);
//                DataSourceTransactionManager transactionManager = applicationContext.get("transactionManager")
//                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//                TransactionStatus status = transactionManager.getTransaction(def);
//                try {
//                    stateMachine.fire(trigger, context)
//                    transactionManager.commit(status);
//                } catch (Exception ex) {
//                    transactionManager.rollback(status);
//                    throw ex;
//                }
//            } finally {
//                jedisLock.release();
//            }
//        }
//    }
}


