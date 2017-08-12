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


    public Task firstTask(Namespace namespace, ProcessStatus status, ItemStatus itemStatus, Instant now) {
        Registration registration = namespace.getRegistration();
        registration.setItemStatus(itemStatus);
        registration.setProcessStatus(status);
        registration.setLastChangeDate(now);
        namespaceRepository.save(namespace);
        return taskService.createOrUpdateTask(namespace, ProcessStatus.VALIDATION_BEGIN, now);
    }

    public Task nextTask(Namespace namespace, ProcessStatus status, Instant now) {
        Registration registration = namespace.getRegistration();
        registration.setProcessStatus(status);
        registration.setLastChangeDate(now);
        namespaceRepository.save(namespace);
        return taskService.createOrUpdateTask(namespace, status, now);
    }

    public void doneTaskAndSetNextStep(Task task, Instant now, ProcessStatus nextStep) {
        Namespace namespace = task.getNamespace();
        Registration registration = namespace.getRegistration();
        registration.setProcessStatus(nextStep);
        registration.setLastChangeDate(now);
        namespaceRepository.save(namespace);
        taskService.done(task, now);
    }


    public void updateToDone(Task task, Instant now, Instant next) {
        Namespace namespace = task.getNamespace();
        Registration registration = namespace.getRegistration();
        registration.setItemStatus(ItemStatus.ISSUED);
        registration.setProcessStatus(ProcessStatus.NONE);
        registration.setLastRevisionDate(now);
        registration.setLastChangeDate(now);
        registration.setNextRenewalDate(next);
        namespaceRepository.save(namespace);
        taskService.done(task, now);
    }
}
