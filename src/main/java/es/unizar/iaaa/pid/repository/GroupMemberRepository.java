package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.GroupMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the GroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {

    @Query("select group_member from GroupMember group_member where group_member.user.login = ?#{principal.username}")
    List<GroupMember> findByUserIsCurrentUser();
    
}
