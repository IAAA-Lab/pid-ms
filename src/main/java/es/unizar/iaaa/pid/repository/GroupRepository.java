package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Group;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Group entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
    
}
