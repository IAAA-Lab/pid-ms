package es.unizar.iaaa.pid.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.repository.PersistentIdentifierRepository;

/**
 * Service class for managing Persistent Identifiers.
 */
@Service
@Transactional
public class PersistentIdentifierService {

    private final Logger log = LoggerFactory.getLogger(PersistentIdentifierService.class);

    final PersistentIdentifierRepository persistentIdentifierRepository;

    public PersistentIdentifierService(PersistentIdentifierRepository persistentIdentifierRepository) {
        this.persistentIdentifierRepository = persistentIdentifierRepository;
    }

    public PersistentIdentifier findByUUID(UUID uuid) {
        return persistentIdentifierRepository.findOne(uuid);
    }

    public void deleteAll() {
        persistentIdentifierRepository.deleteAll();
    }

    public void save(PersistentIdentifier pid) {
        pid.autoId();
        persistentIdentifierRepository.save(pid);
    }

    public void delete(UUID uuid) {
        persistentIdentifierRepository.delete(uuid);
    }

    public Iterable<PersistentIdentifier> findByNamespaceInReviewProcess(String namespace) {
        return persistentIdentifierRepository.findByIdentifierNamespaceAndRegistrationProcessStatus(namespace, ProcessStatus.PENDING_VALIDATION_BY_ID);
    }

    public void prepareForVerification(Namespace namespace) {
        int num = persistentIdentifierRepository.prepareForVerification(ProcessStatus.NONE, namespace.getNamespace());
        log.info("Marked {} Persistent Identifiers of Namespace {} for verification", num, namespace.getNamespace());
    }

    public List<PersistentIdentifier> findByNamespaceValidated(Namespace namespace) {
        return persistentIdentifierRepository.findByNamespaceValidated(namespace.getNamespace(), Arrays.asList(ItemStatus.NEW, ItemStatus.VALIDATED));
    }

    public Page<PersistentIdentifier> findByNamespaceValidatedOrderById(Namespace namespace, Pageable pageable) {
        return persistentIdentifierRepository.findByNamespaceValidatedOrderById(namespace.getNamespace(), Arrays.asList(ItemStatus.NEW, ItemStatus.VALIDATED),pageable);
    }

    public List<PersistentIdentifier> findByNamespaceIssued(Namespace namespace) {
        return persistentIdentifierRepository.findByNamespaceValidated(namespace.getNamespace(), Arrays.asList(ItemStatus.ISSUED));
    }

    public List<PersistentIdentifier> findByNamespaceLapsed(Namespace namespace) {
        return persistentIdentifierRepository.findByNamespaceValidated(namespace.getNamespace(), Arrays.asList(ItemStatus.LAPSED));
    }
    
    public List<PersistentIdentifier> findByFeatureAndNamespaceLapsed(Feature feature, Namespace namespace){
    	return persistentIdentifierRepository.findByFeatureAndNamespaceLapsed(feature, namespace.getNamespace(), Arrays.asList(ItemStatus.LAPSED));
    }
}
