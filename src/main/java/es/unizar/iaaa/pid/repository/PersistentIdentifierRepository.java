package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PersistentIdentifier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersistentIdentifierRepository extends JpaRepository<PersistentIdentifier, Long> {

}
