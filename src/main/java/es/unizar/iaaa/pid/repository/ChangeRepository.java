package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Change entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChangeRepository extends JpaRepository<Change, Long> {

    List<Change> findByTask(Task task);

    Page<Change> findByTaskOrderById(Task task, Pageable pageable);

    @Query("select c1 from Change c1 where c1.task.namespace.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username})")
    Page<Change> findAllInPrincipalOrganizations(Pageable pageable);

    @Query("select c1 from Change c1 where c1.id=?1 and c1.task.namespace.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username})")
    Change findOneInPrincipalOrganizations(Long id);
    
    @Modifying
    @Query("delete from Change c1 where c1.task.id = ?1")
    void deleteAllByTaskId(Long taskId);
}
