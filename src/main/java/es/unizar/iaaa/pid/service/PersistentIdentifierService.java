package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.repository.PersistentIdentifierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Service class for managing Persistent Identifiers.
 */
@Service
@Transactional
public class PersistentIdentifierService {

    private final Logger log = LoggerFactory.getLogger(NamespaceService.class);

    final PersistentIdentifierRepository pidRepository;

    public PersistentIdentifierService(PersistentIdentifierRepository pidRepository) {
        this.pidRepository = pidRepository;
    }

    // public PersistentIdentifier findByUUID(UUID uuid) {
    //    return pidRepository.findOne(uuid);
    //}

    public void deleteAll() {
        pidRepository.deleteAll();
    }

    public void save(PersistentIdentifier pid) {
        pidRepository.save(pid);
    }

    // public void delete(UUID uuid) {
    //    pidRepository.deleteById(uuid);
    //}

    public Iterable<PersistentIdentifier> findByNamespaceInReviewProcess(String namespace) {
        return pidRepository.findByIdentifierNamespaceAndRegistrationProcessStatus(namespace, ProcessStatus.PENDING_VALIDATION_BY_ID);
    }

    public void prepareForVerification(Namespace namespace) {
        int num = pidRepository.prepareForVerification(ProcessStatus.NONE, namespace.getNamespace());
        log.info("Marked {} Persistent Identifiers of Namespace {} for verification", num, namespace.getNamespace());
    }

    public List<PersistentIdentifier> findByNamespaceValidated(Namespace namespace) {
        return pidRepository.findByNamespaceValidated(namespace.getNamespace(), Arrays.asList(ItemStatus.NEW, ItemStatus.VALIDATED));
    }

    public Page<PersistentIdentifier> findByNamespaceValidatedOrderById(Namespace namespace, Pageable pageable) {
        return pidRepository.findByNamespaceValidatedOrderById(namespace.getNamespace(), Arrays.asList(ItemStatus.NEW, ItemStatus.VALIDATED),pageable);
    }

    public List<PersistentIdentifier> findByNamespaceIssued(Namespace namespace) {
        return pidRepository.findByNamespaceValidated(namespace.getNamespace(), Arrays.asList(ItemStatus.ISSUED));
    }

    public List<PersistentIdentifier> findByNamespaceLapsed(Namespace namespace) {
        return pidRepository.findByNamespaceValidated(namespace.getNamespace(), Arrays.asList(ItemStatus.LAPSED));
    }
}
