package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.config.ApplicationProperties;
import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.TaskStatus;
import es.unizar.iaaa.pid.harvester.connectors.FileHarvester;
import es.unizar.iaaa.pid.harvester.connectors.SpatialHarvester;
import es.unizar.iaaa.pid.harvester.connectors.shp.SHPHarvester;
import es.unizar.iaaa.pid.harvester.connectors.wfs.WFSSpatialHarvester;
import es.unizar.iaaa.pid.harvester.tasks.es.unizar.iaaa.pid.harvester.tasks.ext.FileToolsUtil;
import es.unizar.iaaa.pid.service.FeatureService;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.*;


@Component
@Scope("prototype")
class HarvestTask extends AbstractTaskRunner {

	private ApplicationProperties properties;

	private FeatureService featureService;

    protected ApplicationContext context;

    private static final String TMP_PATH = "tmp";
    private static final String ZIP_FILE_NAME = "shpZip.zip";
    private static final String SHP_EXTENSION = ".shp";

    @Autowired
    public HarvestTask(ApplicationContext context, NamespaceService namespaceService, TaskService taskService,
    		FeatureService featureService, ApplicationProperties properties) {
        super(namespaceService, taskService);
        this.context = context;
        this.properties = properties;
        this.featureService = featureService;
    }

    @Override
    protected void runTask() {
    	//get what is the kind of harvert which is needed
    	switch (task.getNamespace().getSource().getSourceType()){
    	case WFS:
    		doWFSHarvest();
    		break;
    	case SHP:
    		doSHPHarvest();
    		break;
    	default:
    		break;
    	}
    }

    public void doWFSHarvest(){
    	SpatialHarvester harvester = getWFSHarvester();
        //Para cada una de las features de la fuente
        List<Feature> featureList = featureService.findAllByNamespace(task.getNamespace());

        for(int index = 0; index < featureList.size(); index++){

        	Feature feature = featureList.get(index);

	        Queue<BoundingBox> queue = new LinkedList<>();
	        queue.add(feature.getBoundingBox());

	        int threshold = feature.getFeaturesThreshold();

	        //control de timeOuts
            Queue<Integer> timeOutQueue = new LinkedList<Integer>();
            timeOutQueue.add(0);

	        if(feature.getHitsRequest()){
	        	 while (!queue.isEmpty() && task.getNumErrors() < properties.getHarvester().getMaxNumErrors()) {
	                 sleepIntervalBetweenRequests();

	                 BoundingBox boundingBox = queue.remove();
	                 int numTimeOut = timeOutQueue.remove();

	                 log("feature={}, boundingBox={}, queue size={}",feature, boundingBox, queue.size());

	                 //tiene disponible la peticion de numero de hits

	             	 int hits = harvester.getHitsTotal(feature, boundingBox);
	                 log("feature={}, hits={}",feature, hits);

	                 if (hits == -1) {
	                     log("fail, enqueue boundingBox={}", feature, boundingBox);
	                     queue.add(boundingBox);
	                     timeOutQueue.add(numTimeOut);
	                     incNumErrors(task);
	                 }
	                 else if(hits == -2){
                    	 if(numTimeOut < properties.getHarvester().getMaxNumTimeouts()){
                    		 log("TimeOut, split={}", boundingBox);
	                    	 queue.addAll(boundingBox.split());
	                    	 Integer [] newTimeOuts = {numTimeOut+1,numTimeOut+1,numTimeOut+1,numTimeOut+1};
	                    	 timeOutQueue.addAll(Arrays.asList(newTimeOuts));
                    	 }
                    	 else{
                    		 log("Maximun number of TimeOut reach, Discard boundingBox {}",boundingBox);
                    	 }
                     }
	                 else if (hits > threshold) {
	                     log("hits over threshold, split={}", boundingBox);
	                     queue.addAll(boundingBox.split());
	                     Integer [] newTimeOuts = {numTimeOut,numTimeOut,numTimeOut,numTimeOut};
                    	 timeOutQueue.addAll(Arrays.asList(newTimeOuts));
	                 }
	                 else if (hits > 0) {
	                     log("hits over threshold, extract");
	                     int ids = harvester.extractIdentifiers(feature,boundingBox);
	                     if (ids == -1) {
	                         log("fail, enqueue boundingBox={}", boundingBox);
	                         queue.add(boundingBox);
	                         timeOutQueue.add(numTimeOut);
	                         incNumErrors(task);
	                     }
	                     else if(ids == -2){
                        	 if(numTimeOut < properties.getHarvester().getMaxNumTimeouts()){
                        		 log("TimeOut, split={}", boundingBox);
    	                    	 queue.addAll(boundingBox.split());
    	                    	 Integer [] newTimeOuts = {numTimeOut+1,numTimeOut+1,numTimeOut+1,numTimeOut+1};
    	                    	 timeOutQueue.addAll(Arrays.asList(newTimeOuts));
                        	 }
                        	 else{
                        		 log("Maximun number of TimeOut reach, Discard boundingBox {}",boundingBox);
                        	 }
                         }
	                     else {
	                         log("feature={}, extracted={}", feature,ids);
	                     }
	                     sleepIntervalBetweenRequests();
	                 }
	        	 }
	        }

	        //no disponible hits request
	        else{
	        	log("no hits request available");

	        	Queue<BoundingBox> errorBoundingBox = new LinkedList<>();
	        	int factor = feature.getFactorK();
	        	BoundingBox boundingBox = feature.getBoundingBox();

	    		double disX = (boundingBox.getMaxX() - boundingBox.getMinX())/factor;
	    		double disY = (boundingBox.getMaxY() - boundingBox.getMinY())/factor;

	    		double positionX = boundingBox.getMinX();

	    		for(int i = 0; i< factor; i++){
	    			double positionY = boundingBox.getMinY();
	    			for(int j = 0 ; j < factor; j++){
	    				BoundingBox bbox = new BoundingBox();
	        			bbox.setMinY(positionY);bbox.setMinX(positionX);
	        			positionY  = positionY + disY;
	        			bbox.setMaxY(positionY);bbox.setMaxX(positionX+disX);

	        			int ids = harvester.extractIdentifiers(feature, bbox);
	                	if(ids == -1){
	                		log("fail, enqueue boundingBox={}", bbox);
	                		errorBoundingBox.add(bbox);
	                        incNumErrors(task);
	                	}
	                	else{
	                		log("feature={}, {} : extracted={}",feature,j,ids);
	                	}

	                    sleepIntervalBetweenRequests();
	                }
	    			positionX = positionX + disX;
	    		}

	    		//vuelvo a intentar los bbox fallidos
	    		while(!errorBoundingBox.isEmpty() && task.getNumErrors() < properties.getHarvester().getMaxNumErrors()){
	    			BoundingBox bbox = errorBoundingBox.remove();
	    			int ids = harvester.extractIdentifiers(feature, bbox);
	            	if(ids == -1){
	            		log("fail, enqueue boundingBox={}", bbox);
	            		errorBoundingBox.add(bbox);
	                    incNumErrors(task);
	            	}
	            	else{
	            		log("feature={}, extracted={}",feature, ids);
	            	}
	    		}
	        }
        }
    }

    private void sleepIntervalBetweenRequests() {
        try{
            if(task.getNamespace().getSource().getMaxNumRequest() != 0){
                int timeSleep = 1000/task.getNamespace().getSource().getMaxNumRequest();
                log("Waiting {} ms for next request", timeSleep);
                Thread.sleep(timeSleep);
            }

        }catch(Exception e){
            error("Waiting next request", e);
        }
    }

    private void incNumErrors(Task task) {
        task.setNumErrors(task.getNumErrors() + 1);
    }

    private SpatialHarvester getWFSHarvester() {
        SpatialHarvester harvester = context.getBean(WFSSpatialHarvester.class);
        harvester.setTask(task);
        return harvester;
    }

    public void doSHPHarvest(){
    	FileHarvester harvester = getSHPHarvester();
    	List<Feature> featureTypeList = featureService.findAllByNamespace(task.getNamespace());

    	if(featureTypeList.size() == 0){
    		return;
    	}

    	//download zip with the SHP
    	File tmpDirectory = new File(TMP_PATH + "_" + Calendar.getInstance().getTimeInMillis());
    	String fileZipName = tmpDirectory + File.separator + ZIP_FILE_NAME;

    	if(!FileToolsUtil.downloadFileHTTP(task.getNamespace().getSource().getEndpointLocation(), fileZipName)){
    		taskService.changeStatus(task, TaskStatus.ERROR);
    		log("Can't download the file {}", task.getNamespace().getSource().getEndpointLocation());
    		return;
    	}

    	File zipFile = new File(fileZipName);
    	//descomprimo el zip
    	if(!FileToolsUtil.unzipFile(zipFile, tmpDirectory.getAbsolutePath())){
    		taskService.changeStatus(task, TaskStatus.ERROR);
    		log("Can't unzip the file {}", zipFile.getAbsolutePath());
    		return;
    	}
    	//borro zip descargado
    	zipFile.delete();

        for(int index = 0; index< featureTypeList.size(); index++){
        	Feature featureType = featureTypeList.get(index);

			if(featureTypeList.get(index).getFeatureType().split("#").length != 2){
				incNumErrors(task);
				log("Feature ignored due to errors.");
				continue;
			}

        	String featureTypeName = featureTypeList.get(index).getFeatureType().split("#")[0].trim();
        	String featureFile = featureTypeList.get(index).getFeatureType().split("#")[1].trim();

        	//obtengo el nombre del SHP de la feature
        	String shpFileName = FileToolsUtil.getFilePath(tmpDirectory,featureFile + SHP_EXTENSION);

        	File shpFile = new File(shpFileName);

        	//extraigo los identifiers
        	log("Begin identifiers extraction");
        	int ids = harvester.extractIdentifiers(featureType,shpFile);

        	if(ids == -1){
        		incNumErrors(task);
        		log("fail, idendifier extraction was wrong");
        	}
        	else{
        		log("feature={}, extracted={}", featureTypeName,ids);
        	}
        }
        //borro directorio temporal
        FileToolsUtil.deleteDirectory(tmpDirectory);
    }

    private FileHarvester getSHPHarvester() {
    	FileHarvester harvester = context.getBean(SHPHarvester.class);
        harvester.setTask(task);
        return harvester;
    }

    @Override
    protected ProcessStatus getNextStep() {
        return PENDING_TRANSFERRING_HARVEST;
    }

    @Override
    protected List<ProcessStatus> getPossibleCurrentSteps() {
        return Collections.singletonList(HARVEST);
    }
}
