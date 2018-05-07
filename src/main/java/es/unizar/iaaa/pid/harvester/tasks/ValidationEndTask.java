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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.ISSUED;
import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.NONE;
import static java.time.Instant.now;

@Component
@Scope("prototype")
public class ValidationEndTask extends AbstractTaskRunner {

    private PersistentIdentifierService persistentIdentifierService;
    private Instant now;
    private Instant renewal;

    @Autowired
    public ValidationEndTask(NamespaceService namespaceService, TaskService taskService,
                             PersistentIdentifierService persistentIdentifierService) {
        super(namespaceService, taskService);
        this.persistentIdentifierService = persistentIdentifierService;
    }

    @Override
    protected void doTask() {
        Namespace namespace = task.getNamespace();
        now = now();
        switch(namespace.getRenewalPolicy()) {
            case YEARLY:
                renewal = now.plus(365, ChronoUnit.DAYS);
                break;
            case MONTHLY:
                renewal = now.plus(30, ChronoUnit.DAYS);
                break;
            case DAILY:
                renewal = now.plus(1, ChronoUnit.DAYS);
                break;
            case ONE_MINUTE:
                renewal = now.plus(1, ChronoUnit.MINUTES);
                break;
            case CONTINUOUS:
            default:
                renewal = null;
        }
        namespace.getRegistration().setLastRevisionDate(now);
        namespace.getRegistration().setNextRenewalDate(renewal);

        Pageable pageable = new PageRequest(0,25000);
        Page<PersistentIdentifier> page = persistentIdentifierService.findByNamespaceValidatedOrderById(namespace, pageable);

        try{
        	int currentPage = page.getNumber();
        	int totalPages = page.getTotalPages();
        	log("Processing page {} of {}", page.getNumber(), page.getTotalPages());
            if (page.hasContent()) {
                processPage(page);
            }
            while(currentPage < totalPages){
            	currentPage +=1;
            	log("Processing page {} of {}", currentPage, totalPages);
            	page = persistentIdentifierService.findByNamespaceValidatedOrderById(namespace, pageable);
            	if (page.hasContent()) {
					processPage(page);
				}
            }
        }
	    catch(Exception e){
	    	log("Transferring Harvest Task", e);
	    }
    }

    private void processPage(Page<PersistentIdentifier> page) {
    	for(PersistentIdentifier pid:page){
    		Registration registration = pid.getRegistration();
            log("{} from {} to {}", pid.getExternalUrn(), registration.getItemStatus(), ISSUED);
            registration.setItemStatus(ISSUED);
            registration.setProcessStatus(NONE);
            registration.setLastChangeDate(now);
            registration.setNextRenewalDate(renewal);
            persistentIdentifierService.save(pid);
    	}
    }

    protected void doEnd() {
        try {
            namespaceService.updateToDone(task, now, renewal);
            log("done");
        } catch (Exception e) {
            error(e);
        }
    }

    @Override
    protected ProcessStatus getNextStep() {
        return NONE;
    }
}

