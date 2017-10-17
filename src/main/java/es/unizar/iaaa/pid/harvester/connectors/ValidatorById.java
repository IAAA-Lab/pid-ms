package es.unizar.iaaa.pid.harvester.connectors;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.Task;

public interface ValidatorById {

    void setTask(Task task);

	int validateGmlId(Feature feature, Identifier identifier);
}
