package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the PersistentIdentifier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersistentIdentifierRepository extends JpaRepository<PersistentIdentifier,Long> {

    // The id was UUID
    Long deleteById(Long uuid);

    Iterable<PersistentIdentifier> findByIdentifierNamespaceAndRegistrationProcessStatus(String namespace, ProcessStatus processStatus);

    @Modifying
    @Query("update PersistentIdentifier pid set pid.registration.processStatus = ?1 where pid.identifier.namespace = ?2")
    int prepareForVerification(ProcessStatus status, String namespace);

    @Query("select p from PersistentIdentifier p where p.identifier.namespace = ?1 and p.registration.itemStatus in ?2")
    List<PersistentIdentifier> findByNamespaceValidated(String namespace, List<ItemStatus> statusList);

    @Query("select p from PersistentIdentifier p where p.identifier.namespace = ?1 and p.registration.itemStatus in ?2")
    Page<PersistentIdentifier> findByNamespaceValidatedOrderById(String namespace, List<ItemStatus> statusList, Pageable pageable);

    Page<PersistentIdentifier> findByIdentifierNamespace(String namespace, Pageable pageable);

    Long countByIdentifierNamespace(String namespace);

}
