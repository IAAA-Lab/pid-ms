package es.unizar.iaaa.pid.service;


import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Registration;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.repository.NamespaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing namespaces.
 */
@Service
@Transactional
public class NamespaceService {

    private final Logger log = LoggerFactory.getLogger(NamespaceService.class);

    private final NamespaceRepository namespaceRepository;

    private final TaskService taskService;

    public NamespaceService(NamespaceRepository namespaceRepository, TaskService taskService) {
        this.namespaceRepository = namespaceRepository;
        this.taskService = taskService;
    }

    public Namespace findPendingProcess() {
        return namespaceRepository.findFirstByRegistrationProcessStatus(ProcessStatus.PENDING_HARVEST);
    }

    public void createOrUpdateNamespace(Namespace namespace) {
        namespaceRepository.save(namespace);
        log.debug("Created Information for Namespace: {}", namespace);
    }

    public void deleteAll() {
        namespaceRepository.deleteAll();
    }

    public List<Namespace> findAll() {
        return namespaceRepository.findAll();
    }

    public Optional<Namespace> prepareForVerification(Instant now) {
        return namespaceRepository
            .findFirstByRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ProcessStatus.NONE, now)
            .map(namespace -> {
                    namespace.getRegistration().setProcessStatus(ProcessStatus.PENDING_HARVEST);
                    return namespace;
                }
                );
    }

    public List<Namespace> findPendingValidation() {
        return namespaceRepository
            .findByRegistrationItemStatusAndRegistrationProcessStatus(ItemStatus.PENDING_VALIDATION, ProcessStatus.NONE);
    }

    public List<Namespace> findLapsed(Instant now) {
        return namespaceRepository
            .findByRegistrationItemStatusAndRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ItemStatus.ISSUED, ProcessStatus.NONE, now);
    }

    public List<Namespace> findByRegistrationProcessStatus(ProcessStatus status) {
        return namespaceRepository.findByRegistrationProcessStatus(status);
    }


    public Task firstTask(Long id, ProcessStatus processStatus, ItemStatus itemStatus, Instant now) {
        Namespace namespace = namespaceRepository.getOne(id);
        Registration registration = namespace.getRegistration();
        registration.setItemStatus(itemStatus);
        registration.setProcessStatus(processStatus);
        registration.setLastChangeDate(now);
        namespaceRepository.save(namespace);
        log.debug("Namespace {} updated ProcessStatus to {} and ItemStatus to {} at {}", id, processStatus, itemStatus, now);
        Task task = taskService.createTask(namespace, ProcessStatus.VALIDATION_BEGIN, now);
        return task;
    }

    public Task nextTask(Long id, ProcessStatus status, Instant now) {
        Namespace namespace = namespaceRepository.getOne(id);
        Registration registration = namespace.getRegistration();
        registration.setProcessStatus(status);
        registration.setLastChangeDate(now);
        namespaceRepository.save(namespace);
        log.debug("Namespace {} updated ProcessStatus to {} at {}", id, status, now);
        Task task = taskService.createTask(namespace, status, now);
        return task;
    }

    public void doneTaskAndSetNextStep(Task task, ProcessStatus status, Instant now) {
        Namespace namespace = namespaceRepository.getOne(task.getNamespace().getId());
        Registration registration = namespace.getRegistration();
        registration.setProcessStatus(status);
        registration.setLastChangeDate(now);
        namespaceRepository.save(namespace);
        log.debug("Namespace {} updated ProcessStatus to {} at {}", namespace.getId(), status, now);
        taskService.done(task, now);
    }


    public void updateToDone(Task task, Instant now, Instant next) {
        Namespace namespace = namespaceRepository.getOne(task.getNamespace().getId());
        Registration registration = namespace.getRegistration();
        registration.setItemStatus(ItemStatus.ISSUED);
        registration.setProcessStatus(ProcessStatus.NONE);
        registration.setLastRevisionDate(now);
        registration.setLastChangeDate(now);
        registration.setNextRenewalDate(next);
        namespaceRepository.save(namespace);
        log.debug("Namespace {} updated ItemStatus to {}, ProcessStatus to {}, " +
            "LastRevisionDate to {}, LastChangeDate to {} and NextRenewalDate to {}",
            namespace.getId(), ItemStatus.ISSUED, ProcessStatus.NONE, now, now, next);
        taskService.done(task, now);
    }
    
    public Namespace findById(Long namespaceId){
    	return namespaceRepository.findOne(namespaceId);
    }
}
