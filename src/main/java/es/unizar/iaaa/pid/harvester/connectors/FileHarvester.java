package es.unizar.iaaa.pid.harvester.connectors;

import java.io.File;

import es.unizar.iaaa.pid.domain.Task;

public interface FileHarvester {
    void setTask(Task source);

    int extractIdentifiers(String feature, File featureFile);
}