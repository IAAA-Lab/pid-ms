package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("select t1 from Task t1 where t1.namespace = ?1 and t1.type = ?2 and t1.timestamp = " +
        " (select max(t2.timestamp) from Task t2 where t2.namespace = ?1 and t2.type = ?2)")
    Task findMostRecentHarvestTask(Namespace namespace, ProcessStatus harvest);

    Page<Task> findByNamespaceNamespaceOrderByTimestamp(String namespace, Pageable pageable);

    @Query("select t1 from Task t1 where t1.namespace = ?1 and t1.status = 'EXECUTING'")
    List<Task> findExecutingTaksByNamespace(Namespace namespace);

}
