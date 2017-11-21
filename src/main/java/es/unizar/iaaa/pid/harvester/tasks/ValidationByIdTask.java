package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.SourceType;
import es.unizar.iaaa.pid.harvester.connectors.ValidatorById;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_TRANSFERRING_VALIDATION_BY_ID;

@Component
@Scope("prototype")
public class ValidationByIdTask extends AbstractTaskRunner {

    protected ApplicationContext context;

    private PersistentIdentifierService persistentIdentifierService;

    @Autowired
    public ValidationByIdTask(NamespaceService namespaceService, TaskService taskService,
                              PersistentIdentifierService persistentIdentifierService,
                              ApplicationContext context) {
        super(namespaceService, taskService);
        this.persistentIdentifierService = persistentIdentifierService;
        this.context = context;
    }
    @Override
    protected void doTask() {
    	//En el caso de que se trate de un fuente SHP no se hace validacion
    	if(task.getNamespace().getSource().getSourceType() == SourceType.SHP){
    		return;
    	}
    	
        ValidatorById validatorById = getValidatorById();

        for(PersistentIdentifier pid: persistentIdentifierService.findByNamespaceLapsed(task.getNamespace())) {
        	try{
        		if(task.getNamespace().getSource().getMaxNumRequest() != 0){
        			int timeSleep = 1000/task.getNamespace().getSource().getMaxNumRequest();
        			log("Esperando {} milisegundos para realizar la siguiente peticion", timeSleep);
        			Thread.sleep(timeSleep);
        		}

        	}catch(Exception e){
        		log("Error al esperar para hacer la siguiente petición");
        	}

        	int isValid = validatorById.validateGmlId(pid.getFeature(),pid.getIdentifier());
            if(isValid == -1){
                task.setNumErrors(task.getNumErrors() + 1);
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
