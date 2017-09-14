package es.unizar.iaaa.pid.repository;

import es.unizar.iaaa.pid.domain.OrganizationMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the OrganizationMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {

    @Query("select organization_member from OrganizationMember organization_member where organization_member.user.login = ?#{principal.username}")
    List<OrganizationMember> findByUserIsCurrentUser();

    @Query("select om1 from OrganizationMember om1, OrganizationMember om2 where om1.organization = om2.organization and om2.user.login = ?#{principal.username}")
    Page<OrganizationMember> findAllInPrincipalOrganizations(Pageable pageable);

    @Query("select om1 from OrganizationMember om1, OrganizationMember om2 where om1.id = ?1 and om1.organization = om2.organization and om2.user.login = ?#{principal.username}")
    OrganizationMember findOneInPrincipalOrganizations(Long id);
}
