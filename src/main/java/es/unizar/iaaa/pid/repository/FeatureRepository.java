package es.unizar.iaaa.pid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
