package es.unizar.iaaa.pid.harvester.tasks.es.unizar.iaaa.pid.harvester.tasks.ext;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
import es.unizar.iaaa.pid.domain.enumeration.TaskStatus;
import es.unizar.iaaa.pid.service.FeatureService;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
import es.unizar.iaaa.pid.web.rest.vm.CsvData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

import static es.unizar.iaaa.pid.domain.enumeration.ItemStatus.*;

public class UpdatingTask implements Runnable{
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdatingTask.class);

	TaskService taskService;
	NamespaceDTOService namespaceDTOService;
	FeatureService featureService;
	NamespaceMapper namespaceMapper;
	PersistentIdentifierService persistentIdentifierService;

	CsvData csvData;
	NamespaceDTO namespace;

	private static String DELETE_ACTION = "delete";
	private static String ADD_ACTION = "add";
	private static String UPDATE_ACTION = "update";

	public UpdatingTask(TaskService taskService, NamespaceDTOService namespaceDTOService, NamespaceMapper namespaceMaper,
			PersistentIdentifierService persistentIdentifierService, FeatureService featureService,
			CsvData csvData,NamespaceDTO namespace){
		this.taskService = taskService;
		this.namespaceDTOService = namespaceDTOService;
		this.namespaceMapper = namespaceMaper;
		this.persistentIdentifierService = persistentIdentifierService;
		this.featureService = featureService;
		this.csvData = csvData;
		this.namespace = namespace;

	}

	@Override
	public void run() {
		Task task = taskService.createTask(namespaceMapper.toEntity(namespace),ProcessStatus.UPDATING_PIDS ,Instant.now());
		taskService.changeStatus(task, TaskStatus.EXECUTING);

    	//llamo al proceso de actualizacion
    	updatePIDs(task);

    	//actualizo el estado de la tarea
    	taskService.changeStatus(task, TaskStatus.DONE);

    	//actualizo el estado del namespace
    	namespace = namespaceDTOService.findOne(namespace.getId());
    	namespace.setProcessStatus(ProcessStatus.NONE);
    	namespaceDTOService.save(namespace);
	}

	private void updatePIDs(Task task){
		Scanner scanner = new Scanner(csvData.getData());

		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] parameters = line.split(",");

			if(parameters.length < 2){
				error(task, "El formato de la línea no es correcto, debe poseer como mínimo dos atributos");
				task.setNumErrors(task.getNumErrors()+1);
				continue;
			}

			String action = parameters[0];
			if(action.equalsIgnoreCase(DELETE_ACTION)){
				if(!deleteAction(parameters,task)){
					task.setNumErrors(task.getNumErrors()+1);
				}
			}
			else if(action.equalsIgnoreCase(ADD_ACTION)){
				if(!addAction(parameters,task)){
					task.setNumErrors(task.getNumErrors()+1);
				}
			}
			else if(action.equalsIgnoreCase(UPDATE_ACTION)){
				if(!updateAction(parameters,task)){
					task.setNumErrors(task.getNumErrors()+1);
				}
			}
			else{
				error(task,"La accion {} no se encuentra entre las disponibles: add,delete,update",action);
				task.setNumErrors(task.getNumErrors()+1);
			}
		}
		scanner.close();
		log(task,"Tarea de actualizacion de PIDs finalizada");
	}

	private boolean deleteAction(String[] parameters, Task task){
		UUID identifier = UUID.fromString(parameters[1]);

		//borro el PID con dicho identificador y dicho namespace
		PersistentIdentifier pid = persistentIdentifierService.findByUUID(identifier);

		if(pid.getIdentifier().getNamespace().equals(namespace.getNamespace())){
			log(task,"Borrado PIDs con id {}, del namespace {}",identifier.toString(), namespace.getNamespace());
			persistentIdentifierService.delete(identifier);
			return true;
		}
		else{
			error(task,"El PIDs con id {} no pertenece al namespace {}",identifier.toString(), namespace.getNamespace());
			return false;
		}
	}

	private boolean addAction(String[] parameters, Task task){
		//parametros necesarios -> add,featureType, schemaPrefix, localId,versionId,beginLifeSpanVersion,endLifeSpanVersion,alternateId,locator
		if(parameters.length != 10){
			error(task,"El número de parámetros no coincide con el necesario.\n add,feature,schemaPrefix,localId,"
					+ "versionId,beginLifeSpanVersion,endLifeSpanVersion,alternateId,locator,resolveProxyMode");
			return false;
		}

		//compruebo si la feature se encuentra entre las disponibles en el Namespace
		String feature = parameters[1];
		String schemaPrefix = parameters[2];

		boolean isFeature = false;

		//obtengo la lista de features del namespace
		List<Feature> featureList = featureService.findAllByNamespace(namespaceMapper.toEntity(namespace));
		Feature featureType = null;
        for (Feature aFeatureList : featureList) {
            String featureAux = aFeatureList.getFeatureType().trim();
            String schemaPrefixAux = aFeatureList.getSchemaPrefix().trim();
            if (featureAux.equals(feature.trim()) && schemaPrefixAux.equals(schemaPrefix.trim())) {
                isFeature = true;
                featureType = aFeatureList;
                break;
            }
        }
		if(!isFeature){
			error(task,"El tipo de feature tiene que encontrarse en el namespace, {}",featureList.toString());
			return false;
		}

		//compruebo que el elemento localId esta, ya que es obligatorio
		String localId = parameters[3];
		if(localId.equals("") || localId.equals("#")){
			error(task,"El atributo LocalId no puede estar vacío");
			return false;
		}

		String versionId = parameters[4];
		if(versionId.equals("") || versionId.equals("#")){
			versionId = "";
		}

		//compruebo si el elemento beginLifeSpanVersion posee un formato correcto
		String beginLifeSpanVersion = parameters[5];
		Instant beginInstant = null;
		try{
			if(!beginLifeSpanVersion.equals("") && !beginLifeSpanVersion.equals("#")){
				beginInstant = Instant.parse(beginLifeSpanVersion);
			}
		}
		catch(Exception e){
			error(task,"El formato del attributo beginLifeSpanVersion no es correcto, ej:2017-11-23T09:40:53.440Z");
			return false;
		}

		//compruebo si el elemento endLifeSpanVersion posee un formato correcto
		String endLifeSpanVersion = parameters[6];
		Instant endInstant = null;
		try{
			if(!endLifeSpanVersion.equals("") && !endLifeSpanVersion.equals("#")){
				endInstant = Instant.parse(endLifeSpanVersion);
			}
		}
		catch(Exception e){
			error(task,"El formato del attributo endLifeSpanVersion no es correcto, ej:2017-11-23T09:40:53.440Z");
			return false;
		}

		String alternateId = parameters[7];
		if(alternateId.equals("") || alternateId.equals("#")){
			alternateId ="";
		}

		String locator = parameters[8];
		if(locator.equals("") || alternateId.equals("#")){
			locator ="";
		}

		//compruebo si el elemento resolveProxyMode posee un formato correcto
		String resolveProxyMode = parameters[9];
		boolean resolve = false;
		try{
			resolve = Boolean.getBoolean(resolveProxyMode);
		}
		catch(Exception e){
			error(task,"El formato del attributo resolverProxyMode no es correcto,ej: false ej; true");
		}

		//genero el identificador persisten
		Identifier identifier = new Identifier()
				.namespace(task.getNamespace().getNamespace())
				.localId(localId)
				.versionId(versionId)
		 		.beginLifespanVersion(beginInstant)
		 		.endLifespanVersion(endInstant)
		 		.alternateId(alternateId);

		Resource resource = new Resource()
				.locator(locator)
				.resourceType(ResourceType.SPATIAL_OBJECT);

		UUID uuid = PersistentIdentifier.computeSurrogateFromIdentifier(identifier);
        PersistentIdentifier pid = persistentIdentifierService.findByUUID(uuid);

        if(pid == null){
        	Registration registration = new Registration();
            registration.setItemStatus(ItemStatus.ISSUED);
            Instant instant = Instant.now();
            registration.setLastRevisionDate(instant);
            registration.setRegistrationDate(instant);
            registration.setLastChangeDate(instant);
            registration.setProcessStatus(ProcessStatus.NONE);
            pid = new PersistentIdentifier()
                .identifier(identifier)
                .resource(resource)
                .registration(registration)
                .resolverProxyMode(resolve)
                .feature(featureType);
            persistentIdentifierService.save(pid);
            log(task,"PID añadido correctamente");
            return true;
        }
        else{
        	error(task,"El PIDs ya existe, no se puede volver a añadir");
        	return false;
        }
	}

	private boolean updateAction(String[]parameters, Task task){

		//parametros necesarios -> update,id,beginLifeSpanVersion,endLifeSpanVersion,alternateId,locator
		if(parameters.length != 6){
			error(task,"El número de parámetros no coincide con el necesario.\n update,id"
					+ ",beginLifeSpanVersion,endLifeSpanVersion,alternateId,locator");
			return false;
		}

		UUID uuid = UUID.fromString(parameters[1]);
		PersistentIdentifier pid = persistentIdentifierService.findByUUID(uuid);

		if(pid != null){
			String beginLifeSpanVersion = parameters[2];
			Instant beginInstant;
			try{
				// si es vacio se deja el valor existente en el pid
				if(beginLifeSpanVersion.equals("") || beginLifeSpanVersion.equals("#")){
					beginInstant = pid.getIdentifier().getBeginLifespanVersion();
				}
				else{
					beginInstant = Instant.parse(beginLifeSpanVersion);
				}
			}
			catch(Exception e){
				error(task,"El formato del attributo beginLifeSpanVersion no es correcto, ej:2017-11-23T09:40:53.440Z");
				return false;
			}
			String endLifeSpanVersion = parameters[3];
			Instant endInstant;
			try{
				// si es vacio se deja el valor existente en el pid
				if(endLifeSpanVersion.equals("") || endLifeSpanVersion.equals("#")){
					endInstant = pid.getIdentifier().getEndLifespanVersion();
				}
				else{
					endInstant = Instant.parse(endLifeSpanVersion);
				}
			}
			catch(Exception e){
				error(task,"El formato del attributo endLifeSpanVersion no es correcto, ej:2017-11-23T09:40:53.440Z");
				return false;
			}

			String alternateId = parameters[4];
			// si es vacio se deja el valor existente en el pid
			if(alternateId.equals("") || alternateId.equals("#")){
				alternateId = pid.getIdentifier().getAlternateId();
			}

			String locator = parameters[5];
			// si es vacio se deja el valor existente en el pid
			if(locator.equals("") || locator.equals("#")){
				locator = pid.getResource().getLocator();
			}

			Instant instant = Instant.now();
			if(canUpdateExtingingPid(pid,instant)){
				Registration registration = pid.getRegistration();

	            registration.setLastRevisionDate(instant);
	            registration.setLastChangeDate(instant);
	            registration.setProcessStatus(ProcessStatus.NONE);
	            pid.getIdentifier().setBeginLifespanVersion(beginInstant);
	            pid.getIdentifier().setEndLifespanVersion(endInstant);
	            pid.getIdentifier().setAlternateId(alternateId);
	            pid.getResource().setLocator(locator);
	            persistentIdentifierService.save(pid);
	            log(task,"PID añadido correctamente");
	            return true;
			}
			else{
				return false;
			}
		}
		else{
			error(task,"El PIDs No existe en necesario añadirlo primero");
        	return false;
		}
	}

	private boolean canUpdateExtingingPid(PersistentIdentifier pid, Instant timeStamp) {
        return (pid.getRegistration().getItemStatus() == VALIDATED) ||
        		pid.getRegistration().getItemStatus() == ISSUED ||
                (pid.getRegistration().getItemStatus() == LAPSED &&
                        timeStamp.isAfter(pid.getRegistration().getLastRevisionDate()));
    }


	private void log(Task task, String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    private void error(Task task,String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.error("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

}
