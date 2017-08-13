package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.service.ChangeService;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.VALIDATED;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_VALIDATION_END;

@Component
@Scope("prototype")
public class TransferringValidationByIdTask extends AbstractTaskRunner {
    private ChangeService changeService;

    private PersistentIdentifierService persistentIdentifierService;

    @Autowired
    public TransferringValidationByIdTask(NamespaceService namespaceService, TaskService taskService,
                                          PersistentIdentifierService persistentIdentifierService,
                                          ChangeService changeService) {
        super(namespaceService, taskService);
        this.persistentIdentifierService = persistentIdentifierService;
        this.changeService = changeService;
    }

    @Override
    protected void doTask() {
        Namespace namespace = task.getNamespace();
        Task previousTask = taskService.findMostRecentValidationByIdTask(namespace);
        log("previousTask=\"{}:{}\"", previousTask.getType(), previousTask.getId());

        for (Change change : changeService.findByTask(previousTask)) {
            Identifier id = change.getIdentifier();
            UUID uuid = PersistentIdentifier.computeSurrogateFromIdentifier(id);
            PersistentIdentifier pid = persistentIdentifierService.findByUUID(uuid);

            switch (change.getAction()) {
                case UNCHANGED:
                    Registration registration = pid.getRegistration();
                    registration.setItemStatus(VALIDATED);
                    registration.setLastRevisionDate(change.getChangeTimestamp());
                    registration.setLastChangeDate(change.getChangeTimestamp());
                    registration.setProcessStatus(PENDING_VALIDATION_END);
                    persistentIdentifierService.save(pid);
                    log("{} is VALIDATED", pid.getExternalUrn());
            }
        }
    }

    @Override
    protected ProcessStatus getNextStep() {
        return PENDING_VALIDATION_END;
    }
}
