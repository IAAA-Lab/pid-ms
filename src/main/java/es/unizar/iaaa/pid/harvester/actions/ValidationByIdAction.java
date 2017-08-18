package es.unizar.iaaa.pid.harvester.actions;

import es.unizar.iaaa.pid.config.harvester.Events;
import es.unizar.iaaa.pid.config.harvester.States;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.service.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_VALIDATION_BY_ID;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.VALIDATION_BY_ID;

@Component
public class ValidationByIdAction extends AbstractAction {

    @Autowired
    public ValidationByIdAction(ApplicationContext context, @Qualifier(value = "taskExecutor") TaskExecutor executor, NamespaceService namespaceService) {
        super(context, executor, namespaceService);
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        doNextTask(getScope(), VALIDATION_BY_ID, "validationByIdTask", Instant.now());
    }

    private List<Namespace> getScope() {
        return namespaceService.findByRegistrationProcessStatus(PENDING_VALIDATION_BY_ID);
    }
}
