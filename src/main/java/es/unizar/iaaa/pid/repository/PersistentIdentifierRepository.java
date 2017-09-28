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
import java.util.UUID;


/**
 * Spring Data JPA repository for the PersistentIdentifier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersistentIdentifierRepository extends JpaRepository<PersistentIdentifier,UUID> {

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

    @Query("select p from PersistentIdentifier p where p.identifier.namespace in (select ns.namespace from Namespace ns where ns.publicNamespace = true or ns.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username}))")
    Page<PersistentIdentifier> findAllPublicOrInPrincipalOrganizations(Pageable pageable);
    
    @Query("select p from PersistentIdentifier p where p.identifier.namespace in (select ns.namespace from Namespace ns where ns.publicNamespace = true)")
    Page<PersistentIdentifier> findAllPublic(Pageable pageable);

    @Query("select p from PersistentIdentifier p where p.id = ?1 and p.identifier.namespace in (select ns.namespace from Namespace ns where ns.publicNamespace = true or ns.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username}))")
    PersistentIdentifier findOnePublicOrInPrincipalOrganizations(UUID id);
    
    @Query("select p from PersistentIdentifier p where p.id = ?1 and p.identifier.namespace in (select ns.namespace from Namespace ns where ns.publicNamespace = true)")
    PersistentIdentifier findOnePublic(UUID id);
    
    @Modifying
    @Query("delete from PersistentIdentifier p where p.identifier.namespace in (select ns.namespace from Namespace ns where ns.id = ?1)")
    void deleteAllByNamespaceId(Long namespaceId);
}
