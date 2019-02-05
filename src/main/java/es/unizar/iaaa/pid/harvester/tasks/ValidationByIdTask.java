package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.SourceType;
import es.unizar.iaaa.pid.harvester.connectors.ValidatorById;
import es.unizar.iaaa.pid.service.FeatureService;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.*;

@Component
@Scope("prototype")
public class ValidationByIdTask extends AbstractTaskRunner {

    protected ApplicationContext context;

    private PersistentIdentifierService persistentIdentifierService;
    private FeatureService featureService;

    @Autowired
    public ValidationByIdTask(NamespaceService namespaceService, TaskService taskService,
                              PersistentIdentifierService persistentIdentifierService,
                              FeatureService featureService, ApplicationContext context) {
        super(namespaceService, taskService);
        this.persistentIdentifierService = persistentIdentifierService;
        this.featureService = featureService;
        this.context = context;
    }

    @Override
    protected void runTask() {

    	//En el caso de que se trate de un fuente SHP no se hace validacion
    	if(task.getNamespace().getSource().getSourceType() == SourceType.SHP){
    		return;
    	}

        ValidatorById validatorById = getValidatorById();

        //get all features of the Namespaces
        for(Feature feature : featureService.findAllByNamespace(task.getNamespace())){
        	//get all identifier for each feature
        	for(PersistentIdentifier pid : persistentIdentifierService.findByFeatureAndNamespaceLapsed(feature, task.getNamespace())){
        		try{
            		if(task.getNamespace().getSource().getMaxNumRequest() != 0){
            			int timeSleep = 1000/task.getNamespace().getSource().getMaxNumRequest();
            			log("Waiting {} ms to next request", timeSleep);
            			Thread.sleep(timeSleep);
            		}

            	}catch(Exception e){
            		error("waiting to next request", e);
            	}

            	int isValid = validatorById.validateGmlId(pid.getFeature(), pid.getIdentifier());
                if(isValid == -1){
                    task.setNumErrors(task.getNumErrors() + 1);
                }
        	}
        }
    }

    private ValidatorById getValidatorById() {
        ValidatorById validateById = context.getBean(ValidatorById.class);
        validateById.setTask(task);
        return validateById;
    }

    @Override
    protected ProcessStatus getNextStep() {
        return PENDING_TRANSFERRING_VALIDATION_BY_ID;
    }

    @Override
    protected List<ProcessStatus> getPossibleCurrentSteps() {
        return Collections.singletonList(VALIDATION_BY_ID);
    }
}
