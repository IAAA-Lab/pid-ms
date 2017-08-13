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

import java.util.List;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_TRANSFERRING_HARVEST;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.TRANSFERRING_HARVEST;
import static java.time.Instant.now;

@Component
public class TransferringHarvestAction extends AbstractAction {

    @Autowired
    public TransferringHarvestAction(ApplicationContext context, @Qualifier(value = "taskExecutor") TaskExecutor executor, NamespaceService namespaceService) {
        super(context, executor, namespaceService);
    }

    @Override
    public void execute(StateContext<States, Events> context) {
        doNextTask(getScope(), TRANSFERRING_HARVEST, "transferringHarvestTask", now());
    }

    private List<Namespace> getScope() {
        return namespaceService.findByRegistrationProcessStatus(PENDING_TRANSFERRING_HARVEST);
    }
}
