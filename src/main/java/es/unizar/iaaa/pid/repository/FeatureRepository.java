package es.unizar.iaaa.pid.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Namespace;

/**
 * Spring Data JPA repository for the Feature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long>{
	
	List<Feature> findAllByNamespace(Namespace namespace);
	
	@Query("select f1 from Feature f1 where f1.namespace.publicNamespace = true")
	Page<Feature> findAllByTheirPublicNamespaceIsTrue(Pageable pageable);
	
	@Query("select f1 from Feature f1 where f1.id = ?1 and f1.namespace.publicNamespace = true")
	Feature findByIdAndTheirPublicNamespaceIsTrue(Long id);
	
	@Query("select f1 from Feature f1 where f1.namespace.publicNamespace = true or f1.namespace.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username})")
	Page<Feature> findAllByTheirNamespacePublicOrBelongsToPrincipalOrganizations(Pageable pageable);
	
	@Query("select f1 from Feature f1 where f1.id = ?1 and (f1.namespace.publicNamespace = true or f1.namespace.owner in (select om.organization from OrganizationMember om where om.user.login = ?#{principal.username}))")
	Feature findByIdAndTheirNamespacePublicOrBelongsToPrincipalOrganizations(Long id);
	
	Feature findByFeatureTypeAndSchemaPrefix(String featureType, String schemaPrefix);
	
	@Query("select f1 from Feature f1 where f1.namespace.id = ?1")
	List<Feature> findAllByNamespaceId(Long namespaceId);
}
