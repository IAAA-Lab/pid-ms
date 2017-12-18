package es.unizar.iaaa.pid.service.mapper;

import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.google.common.collect.ImmutableMap;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.service.dto.FeatureDTO;

/**
 * Mapper for the entity Feature and its DTO FeatureDTO.
 */
@Mapper(componentModel = "spring", uses = {NamespaceMapper.class})
public interface FeatureMapper extends EntityMapper <FeatureDTO, Feature>{

	@Mappings({
      @Mapping(source = "srsName", target = "srsName"),
      @Mapping(source = "schemaUri", target = "schemaUri"),
      @Mapping(source = "schemaUriGML", target = "schemaUriGML"),
      @Mapping(source = "schemaUriBase", target = "schemaUriBase"),
      @Mapping(source = "schemaPrefix", target = "schemaPrefix"),
      @Mapping(source = "featureType", target = "featureType"),
      @Mapping(source = "geometryProperty", target = "geometryProperty"),
      @Mapping(source = "beginLifespanVersionProperty", target = "beginLifespanVersionProperty"),
      @Mapping(source = "featuresThreshold", target = "featuresThreshold"),
      @Mapping(source = "hitsRequest", target = "hitsRequest"),
      @Mapping(source = "factorK", target = "factorK"),
      @Mapping(source = "xpath", target = "xpath"),
      @Mapping(source = "nameItem", target = "nameItem"),
      @Mapping(source = "boundingBox.maxX", target = "maxX"),
      @Mapping(source = "boundingBox.maxY", target = "maxY"),
      @Mapping(source = "boundingBox.minX", target = "minX"),
      @Mapping(source = "boundingBox.minY", target = "minY"),
      @Mapping(source = "namespace.id", target = "namespaceId")
	})
	FeatureDTO toDto(Feature feature);
	
	@Mappings({
      @Mapping(source = "srsName", target = "srsName"),
      @Mapping(source = "schemaUri", target = "schemaUri"),
      @Mapping(source = "schemaUriGML", target = "schemaUriGML"),
      @Mapping(source = "schemaUriBase", target = "schemaUriBase"),
      @Mapping(source = "schemaPrefix", target = "schemaPrefix"),
      @Mapping(source = "featureType", target = "featureType"),
      @Mapping(source = "geometryProperty", target = "geometryProperty"),
      @Mapping(source = "beginLifespanVersionProperty", target = "beginLifespanVersionProperty"),
      @Mapping(source = "featuresThreshold", target = "featuresThreshold"),
      @Mapping(source = "hitsRequest", target = "hitsRequest"),
      @Mapping(source = "factorK", target = "factorK"),
      @Mapping(source = "xpath", target = "xpath"),
      @Mapping(source = "nameItem", target = "nameItem"),
      @Mapping(source = "maxX", target = "boundingBox.maxX"),
      @Mapping(source = "maxY", target = "boundingBox.maxY"),
      @Mapping(source = "minX", target = "boundingBox.minX"),
      @Mapping(source = "minY", target = "boundingBox.minY"),
      @Mapping(source = "namespaceId", target = "namespace.id"),
	})
	Feature toEntity(FeatureDTO featureDTO); 
    default Feature fromId(Long id) {
        if (id == null) {
            return null;
        }
        Feature feature = new Feature();
        feature.setId(id);
        return feature;
    }
    
    Map<String, String> conversions = ImmutableMap.<String, String>builder().
        put("srsName", "srsName").
        put("schemaUri", "schemaUri").
        put("schemaUriGML", "schemaUriGML").
        put("schemaUriBase", "schemaUriBase").
        put("schemaPrefix", "schemaPrefix").
        put("featureType", "featureType").
        put("geometryProperty", "geometryProperty").
        put("beginLifespanVersionProperty", "beginLifespanVersionProperty").
        put("featuresThreshold", "featuresThreshold").
        put("hitsRequest", "hitsRequest").
        put("factorK", "factorK").
        put("xpath", "xpath").
        put("nameItem", "nameItem").
        put("maxX", "boundingBox.maxX").
        put("maxY", "boundingBox.maxY").
        put("minX", "boundingBox.minX").
        put("minY", "boundingBox.minY").
        put("namespaceId","namespace.id").
        build();

    @Override
    default Map<String, String> getConversions() {
        return conversions;
    }
}
