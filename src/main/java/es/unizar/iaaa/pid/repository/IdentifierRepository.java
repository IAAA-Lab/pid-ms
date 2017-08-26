package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Identifier;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Identifier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentifierRepository extends JpaRepository<Identifier,Long> {
    
}
