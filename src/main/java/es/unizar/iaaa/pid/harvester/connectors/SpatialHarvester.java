package es.unizar.iaaa.pid.harvester.connectors;

import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Task;

public interface SpatialHarvester {
    void setTask(Task source);

    int getHitsTotal(BoundingBox boundingBox);

    int extractIdentifiers(BoundingBox boundingBox);
}
