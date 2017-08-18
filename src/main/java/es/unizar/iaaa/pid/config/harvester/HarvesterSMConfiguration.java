package es.unizar.iaaa.pid.config.harvester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class HarvesterSMConfiguration extends StateMachineConfigurerAdapter<States, Events> {

    @Autowired
    private Action<States,Events> preparingSystemAction;

    @Autowired
    private Action<States,Events> markForValidationAction;

    @Autowired
    private Action<States,Events> harvestAction;

    @Autowired
    private Action<States,Events> transferringHarvestAction;

    @Autowired
    private Action<States,Events> validationEndAction;

    @Autowired
    private Action<States,Events> preparingHarvestAction;

    @Autowired
    private Action<States,Events> validationByIdAction;

    @Autowired
    private Action<States,Events> transferringValidationByIdAction;

    private static final Logger LOGGER = LoggerFactory.getLogger(HarvesterSMConfiguration.class);

    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true);
    }


    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
      states
        .withStates()
          .initial(States.STARTUP, preparingSystemAction)
          .state(States.RUNNING, running(), null)
          .end(States.END)
          .and()
          .withStates()
              .parent(States.RUNNING)
              .initial(States.MARKED_FOR_VALIDATION, markForValidationAction)
              .state(States.PREPARING_HARVEST_LAUNCHED, preparingHarvestAction, null)
              .state(States.HARVEST_LAUNCHED, harvestAction, null)
              .state(States.TRANSFERRING_HARVEST_LAUNCHED, transferringHarvestAction, null)
              .state(States.VALIDATION_BY_ID_LAUNCHED, validationByIdAction, null)
              .state(States.TRANSFERRING_VALIDATION_BY_ID_LAUNCHED, transferringValidationByIdAction, null)
              .state(States.FINISH_VALIDATION_LAUNCHED, validationEndAction, null);

    }

    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
      transitions
        .withExternal()
          .source(States.STARTUP)
          .target(States.RUNNING)
          .and()
        .withExternal()
          .source(States.RUNNING)
          .target(States.END)
          .event(Events.STOP)
          .action(end())
          .and()
        .withExternal()
          .source(States.MARKED_FOR_VALIDATION)
          .target(States.PREPARING_HARVEST_LAUNCHED)
          .timerOnce(1000)
          .and()
        .withExternal()
          .source(States.PREPARING_HARVEST_LAUNCHED)
          .target(States.HARVEST_LAUNCHED)
          .timerOnce(1000)
          .and()
        .withExternal()
          .source(States.HARVEST_LAUNCHED)
          .target(States.TRANSFERRING_HARVEST_LAUNCHED)
          .timerOnce(1000)
          .and()
      .withExternal()
          .source(States.TRANSFERRING_HARVEST_LAUNCHED)
          .target(States.VALIDATION_BY_ID_LAUNCHED)
          .timerOnce(1000)
          .and()
      .withExternal()
          .source(States.VALIDATION_BY_ID_LAUNCHED)
          .target(States.TRANSFERRING_VALIDATION_BY_ID_LAUNCHED)
          .timerOnce(1000)
          .and()
      .withExternal()
          .source(States.TRANSFERRING_VALIDATION_BY_ID_LAUNCHED)
          .target(States.FINISH_VALIDATION_LAUNCHED)
          .timerOnce(1000)
          .and()
      .withExternal()
          .source(States.FINISH_VALIDATION_LAUNCHED)
          .target(States.MARKED_FOR_VALIDATION)
          .action(markForValidationAction)
          .timerOnce(1000);
    }


    @Bean
    public Action<States,Events> running() {
        return stateContext -> LOGGER.info("Begin state machine");
    }

    @Bean
    public Action<States,Events> end() {
        return stateContext -> LOGGER.info("End state machine");
    }
}
