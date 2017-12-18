package es.unizar.iaaa.pid.harvester.tasks;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_TRANSFERRING_VALIDATION_BY_ID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.harvester.connectors.ValidatorById;
import es.unizar.iaaa.pid.service.FeatureService;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;

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
    protected void doTask() {
        ValidatorById validatorById = getValidatorById();

        //get all features of the Namespaces
        for(Feature feature : featureService.findAllByNamespace(task.getNamespace())){
        	//get all identifier for each feature
        	for(PersistentIdentifier pid : persistentIdentifierService.findByFeatureAndNamespaceLapsed(feature, task.getNamespace())){
        		try{
            		if(task.getNamespace().getSource().getMaxNumRequest() != 0){
            			int timeSleep = 1000/task.getNamespace().getSource().getMaxNumRequest();
            			log("Esperando {} milisegundos para realizar la siguiente peticion", timeSleep);
            			Thread.sleep(timeSleep);
            		}

            	}catch(Exception e){
            		log("Error al esperar para hacer la siguiente petición");
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
}
