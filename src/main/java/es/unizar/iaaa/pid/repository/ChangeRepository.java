package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Change;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Change entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChangeRepository extends JpaRepository<Change,Long> {
    
}
