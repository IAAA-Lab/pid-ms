package es.unizar.iaaa.pid.harvester.connectors;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Task;

import java.io.File;

public interface FileHarvester {

    void setTask(Task source);

    int extractIdentifiers(Feature feature,  File featureFile);

}
