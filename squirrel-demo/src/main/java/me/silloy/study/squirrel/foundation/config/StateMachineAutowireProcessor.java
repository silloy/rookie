package me.silloy.study.squirrel.foundation.config;

import com.google.common.base.Preconditions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.squirrelframework.foundation.component.SquirrelPostProcessor;
import org.squirrelframework.foundation.component.SquirrelPostProcessorProvider;
import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * @author shaohuasu
 * @since 1.8
 */
@Component
public class StateMachineAutowireProcessor implements SquirrelPostProcessor<StateMachine>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public StateMachineAutowireProcessor() {
        // register StateMachineAutowireProcessor as state machine post processor
        SquirrelPostProcessorProvider.getInstance().register(StateMachine.class, this);
    }

    @Override
    public void postProcess(StateMachine stateMachine) {
        Preconditions.checkNotNull(stateMachine);
        // after state machine instance created,
        // autoware @Autowired/@Value dependencies and properties within state machine class
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.autowireBean(stateMachine);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
