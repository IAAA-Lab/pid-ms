package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

//    @Query("select t1 from Task t1 where t1.namespace = ?1 and t1.taskType = ?2 and t1.timeStamp = " +
//        " (select max(t2.timeStamp) from Task t2 where t2.namespace = ?1 and t2.taskType = ?2)")
//    Task findMostRecentHarvestTask(Namespace namespace, ProcessStatus harvest);

//    Page<Task> findByNamespaceExternalOrderByTimeStamp(String namespace, Pageable pageable);

//    @Query("select t1 from Task t1 where t1.namespace = ?1 and t1.status = 'EXECUTING'")
//    List<Task> findExecutingTaksByNamespace(Namespace namespace);

}
