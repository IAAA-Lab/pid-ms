package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Namespace;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Namespace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, Long> {

}
