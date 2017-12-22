package es.unizar.iaaa.pid.harvester.tasks;

import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.ISSUED;
import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.LAPSED;
import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.NEW;
import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.PENDING_VALIDATION;
import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.VALIDATED;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_VALIDATION_BY_ID;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_VALIDATION_END;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.Registration;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.service.ChangeService;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;


@Component
@Scope("prototype")
public class TransferringHarvestTask extends AbstractTaskRunner {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ChangeService changeService;

    private PersistentIdentifierService persistentIdentifierService;

    @Autowired
    public TransferringHarvestTask(NamespaceService namespaceService, TaskService taskService,
                                   PersistentIdentifierService persistentIdentifierService,
                                   ChangeService changeService) {
        super(namespaceService, taskService);
        this.persistentIdentifierService = persistentIdentifierService;
        this.changeService = changeService;
    }

    @Override
    protected void doTask() {
        Namespace namespace = task.getNamespace();
        Task previousTask = taskService.findMostRecentHarvestTask(namespace);
        log("previousTask=\"{}:{}\"", previousTask.getType(), previousTask.getId());


        Pageable pageable = new PageRequest(0,25000);
        Page<Change> page = changeService.findByTaskOrderById(previousTask, pageable);

        try{
        	boolean resolverProxyMode = namespace.getSource().isResolverProxyMode();
            log("Processing page {} of {}", page.getNumber(), page.getTotalPages());
            if (page.hasContent()) {
                processPage(page, resolverProxyMode);
            }
	        while(page.hasNext()){
                pageable = page.nextPageable();
                page = changeService.findByTaskOrderById(previousTask, pageable);
                log("Processing page {} of {}", page.getNumber(), page.getTotalPages());
				if (page.hasContent()) {
					processPage(page, resolverProxyMode);
				}
	        }
        }catch(Exception e){
        	log("Transferring Harvest Task", e);
        }
    }

	private void processPage(Page<Change> page, boolean resolverProxyMode) {
		for(Change change:page){

            Identifier id = change.getIdentifier();
            log("{}", change);
            UUID uuid = PersistentIdentifier.computeSurrogateFromIdentifier(id);
            PersistentIdentifier pid = persistentIdentifierService.findByUUID(uuid);

            if (pid == null) {
                Registration registration = new Registration();
                registration.setItemStatus(NEW);
                registration.setLastRevisionDate(change.getChangeTimestamp());
                registration.setRegistrationDate(change.getChangeTimestamp());
                registration.setLastChangeDate(change.getChangeTimestamp());
                registration.setProcessStatus(PENDING_VALIDATION_END);
                pid = new PersistentIdentifier()
                    .identifier(id)
                    .resource(change.getResource())
                    .registration(registration)
                    .resolverProxyMode(resolverProxyMode)
                    .feature(change.getFeature());
                persistentIdentifierService.save(pid);
            } else if (canUpdateExtingingPid(pid, change.getChangeTimestamp())) {
                Registration registration = pid.getRegistration();
                registration.setItemStatus(VALIDATED);
                registration.setLastRevisionDate(change.getChangeTimestamp());
                registration.setLastChangeDate(change.getChangeTimestamp());
                registration.setProcessStatus(PENDING_VALIDATION_END);
                persistentIdentifierService.save(pid);
            } else {
                log.warn("{} is not modified", pid.getExternalUrn());
            }
        }
	}

	private boolean canUpdateExtingingPid(PersistentIdentifier pid, Instant timeStamp) {
        return (pid.getRegistration().getItemStatus() == VALIDATED) || 
        		pid.getRegistration().getItemStatus() == ISSUED ||
                (pid.getRegistration().getItemStatus() == LAPSED &&
                        timeStamp.isAfter(pid.getRegistration().getLastRevisionDate()));
    }

    @Override
    protected ProcessStatus getNextStep() {
        if (task.getNamespace().getRegistration().getItemStatus() == PENDING_VALIDATION)
            return PENDING_VALIDATION_END;
        else
            // LAPSED
            return PENDING_VALIDATION_BY_ID;
    }
}
