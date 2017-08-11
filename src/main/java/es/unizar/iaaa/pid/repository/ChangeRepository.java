package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Change;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Change entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChangeRepository extends JpaRepository<Change,Long> {

    // TODO missing Task relationship
    // List<Change> findByTask(Task task);

    // TODO missing Task relationship
    //Page<Change> findByTaskOrderById(Task task, Pageable pageable);
}
