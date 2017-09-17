package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Namespace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, Long> {

    Namespace findFirstByRegistrationProcessStatus(ProcessStatus processStatus);

    Optional<Namespace> findFirstByRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ProcessStatus none, Instant now);

    List<Namespace> findByRegistrationItemStatusAndRegistrationProcessStatus(ItemStatus itemStatus, ProcessStatus processStatus);

    List<Namespace> findByRegistrationProcessStatus(ProcessStatus itemStatus);

    List<Namespace> findByRegistrationItemStatusAndRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ItemStatus itemStatus, ProcessStatus processStatus, Instant
        dateTime);

    // was findOneByExternal
    Namespace findOneByNamespace(String namespace);

    Page<Namespace> findAllByPublicNamespaceIsTrue(Pageable pageable);

    Namespace findByIdAndPublicNamespaceIsTrue(Long id);

    @Query("select ns from Namespace ns where ns.publicNamespace = true or ns.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username})")
    Page<Namespace> findAllPublicOrBelongsToPrincipalOrganizations(Pageable pageable);

    @Query("select ns from Namespace ns where ns.id = ?1 and (ns.publicNamespace = true  or ns.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username}))")
    Namespace findByIdAndPublicOrBelongsToPrincipalOrganizations(Long id);
}
