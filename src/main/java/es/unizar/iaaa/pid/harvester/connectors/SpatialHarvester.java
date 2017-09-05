package es.unizar.iaaa.pid.harvester.connectors;

import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Task;

public interface SpatialHarvester {
    void setTask(Task source);

    int getHitsTotal(String feature,BoundingBox boundingBox);

    int extractIdentifiers(String feature,BoundingBox boundingBox);
}
