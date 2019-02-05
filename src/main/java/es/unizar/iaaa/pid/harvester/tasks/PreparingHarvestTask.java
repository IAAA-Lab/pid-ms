package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.Registration;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.LAPSED;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.*;

@Component
@Scope("prototype")
public class PreparingHarvestTask extends AbstractTaskRunner {

    private PersistentIdentifierService persistentIdentifierService;

    @Autowired
    public PreparingHarvestTask(NamespaceService namespaceService, TaskService taskService,
                                PersistentIdentifierService persistentIdentifierService) {
        super(namespaceService, taskService);
        this.persistentIdentifierService = persistentIdentifierService;
    }

    @Override
    protected void runTask() {
        Namespace namespace = task.getNamespace();
        Instant now = Instant.now();
        for(PersistentIdentifier pid: persistentIdentifierService.findByNamespaceIssued(namespace)) {
            Registration registration = pid.getRegistration();
            log("{} from {} to {}", pid.getExternalUrn(), registration.getItemStatus(), LAPSED);
            registration.setItemStatus(LAPSED);
            registration.setProcessStatus(PENDING_VALIDATION_END);
            registration.setLastChangeDate(now);
            persistentIdentifierService.save(pid);
        }

    }

    @Override
    protected ProcessStatus getNextStep() {
        return PENDING_HARVEST;
    }

    @Override
    protected List<ProcessStatus> getPossibleCurrentSteps() {
        return Collections.singletonList(PREPARING_HARVEST);
    }
}
