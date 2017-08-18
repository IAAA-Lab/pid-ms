package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Namespace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NamespaceRepository extends JpaRepository<Namespace,Long> {

    Namespace findFirstByRegistrationProcessStatus(ProcessStatus processStatus);

    Optional<Namespace> findFirstByRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ProcessStatus none, Instant now);

    List<Namespace> findByRegistrationItemStatusAndRegistrationProcessStatus(ItemStatus itemStatus, ProcessStatus processStatus);

    List<Namespace> findByRegistrationProcessStatus(ProcessStatus itemStatus);

    List<Namespace> findByRegistrationItemStatusAndRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ItemStatus itemStatus, ProcessStatus processStatus, Instant
        dateTime);

    // was findOneByExternal
    Namespace findOneByNamespace(String namespace);

}
