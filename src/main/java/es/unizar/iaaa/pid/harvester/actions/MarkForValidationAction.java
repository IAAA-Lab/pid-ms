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

import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.LAPSED;
import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.PENDING_VALIDATION;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_HARVEST;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_PREPARING_HARVEST;

@Component
public class MarkForValidationAction extends AbstractAction {

    @Autowired
    public MarkForValidationAction(ApplicationContext context, @Qualifier(value = "taskExecutor") TaskExecutor executor, NamespaceService namespaceService) {
        super(context, executor, namespaceService);
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        Instant now = Instant.now();
        doFirstTask(getPendingScope(), PENDING_HARVEST, PENDING_VALIDATION, now);
        doFirstTask(getLapsedScope(now), PENDING_PREPARING_HARVEST, LAPSED, now);
    }

    private List<Namespace> getPendingScope() {
        return namespaceService.findPendingValidation();
    }

    private List<Namespace> getLapsedScope(Instant now) {
        return namespaceService.findLapsed(now);
    }
}
