package es.unizar.iaaa.pid.harvester.actions;

import es.unizar.iaaa.pid.config.harvester.Events;
import es.unizar.iaaa.pid.config.harvester.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class PreparingSystemAction implements Action<States,Events> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreparingSystemAction.class);

    public void execute(StateContext<States, Events> context) {
        LOGGER.info("[Preparing System Action]");
    }
}
