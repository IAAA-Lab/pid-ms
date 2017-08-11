package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Namespace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NamespaceRepository extends JpaRepository<Namespace,Long> {

    Namespace findFirstByRegistrationProcessStatus(ProcessStatus processStatus);

    Namespace findFirstByRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ProcessStatus none, DateTime now);

    List<Namespace> findByRegistrationItemStatusAndRegistrationProcessStatus(ItemStatus itemStatus, ProcessStatus processStatus);

    List<Namespace> findByRegistrationProcessStatus(ProcessStatus itemStatus);

    List<Namespace> findByRegistrationItemStatusAndRegistrationProcessStatusAndRegistrationNextRenewalDateLessThan(ItemStatus itemStatus, ProcessStatus processStatus, DateTime dateTime);

    // was findOneByExternal
    Namespace findOneByNamespace(String namespace);

}
