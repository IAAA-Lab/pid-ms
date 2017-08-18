package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Change entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChangeRepository extends JpaRepository<Change,Long> {

    List<Change> findByTask(Task task);

    Page<Change> findByTaskOrderById(Task task, Pageable pageable);
}
