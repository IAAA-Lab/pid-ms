package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.OrganizationMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the OrganizationMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember,Long> {

    @Query("select organization_member from OrganizationMember organization_member where organization_member.user.login = ?#{principal.username}")
    List<OrganizationMember> findByUserIsCurrentUser();
    
}
