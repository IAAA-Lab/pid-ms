package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Namespace;
import es.unizar.iaaa.pid.repository.FeatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Features.
 */
@Service
@Transactional
public class FeatureService {

	private final Logger log = LoggerFactory.getLogger(FeatureService.class);

	private FeatureRepository featureRepository;

	public FeatureService(FeatureRepository featureRepository){
		this.featureRepository = featureRepository;
	}

	public void createOrUpdateFeature(Feature feature) {
		featureRepository.save(feature);
        log.debug("Created Information for Feature: {}", feature);
    }

    public void deleteAll() {
    	featureRepository.deleteAll();
    }

    public List<Feature> findAll() {
        return featureRepository.findAll();
    }

    public List<Feature> findAllByNamespace(Namespace namespace){
    	return featureRepository.findAllByNamespace(namespace);
    }
}
