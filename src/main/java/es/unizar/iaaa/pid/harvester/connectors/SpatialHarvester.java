package es.unizar.iaaa.pid.harvester.connectors;

import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Task;

public interface SpatialHarvester {
    void setTask(Task source);

    int getHitsTotal(Feature feature,BoundingBox boundingBox);

    int extractIdentifiers(Feature feature,BoundingBox boundingBox);
}
