package es.unizar.iaaa.pid.harvester.tasks;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_TRANSFERRING_HARVEST;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import es.unizar.iaaa.pid.config.ApplicationProperties;
import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.domain.enumeration.TaskStatus;
import es.unizar.iaaa.pid.harvester.connectors.FileHarvester;
import es.unizar.iaaa.pid.harvester.connectors.SpatialHarvester;
import es.unizar.iaaa.pid.harvester.connectors.shp.SHPHarvester;
import es.unizar.iaaa.pid.harvester.connectors.wfs.WFSSpatialHarvester;
import es.unizar.iaaa.pid.harvester.tasks.util.FileToolsUtil;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.TaskService;


@Component
@Scope("prototype")
class HarvestTask extends AbstractTaskRunner {
	
	private ApplicationProperties properties;
	
    protected ApplicationContext context;
    
    private static final String TMP_PATH = "tmp";
    private static final String ZIP_FILE_NAME = "shpZip.zip";
	private static final String SHP_EXTENSION = ".shp";

    @Autowired
    public HarvestTask(ApplicationContext context, NamespaceService namespaceService, TaskService taskService,
    		ApplicationProperties properties) {
        super(namespaceService, taskService);
        this.context = context;
        this.properties = properties;
    }

    @Override
    protected void doTask() {
    	
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
        int threshold = getThreshold();
        
        //Para cada una de las features del WFS
        String[]featureList = task.getNamespace().getSource().getFeatureType().split(",");

        for(int index = 0; index< featureList.length; index++){
	        Queue<BoundingBox> queue = new LinkedList<>();
	        queue.add(initialBoundingBox());
	        
	        //control de timeOuts
            Queue<Integer> timeOutQueue = new LinkedList<Integer>();
            timeOutQueue.add(0);
	        
	        String feature = featureList[index].trim();
	
	        if(task.getNamespace().getSource().isHitsRequest()){
	        	 while (!queue.isEmpty() && task.getNumErrors() < properties.getHarvester().getMAX_NUM_ERRORS()) {
	                 sleepIntervalBetweenRequests();
	
	                 BoundingBox boundingBox = queue.remove();
	                 int numTimeOut = timeOutQueue.remove();
	                 
	                 log("feature={}, boundingBox={}, queue size={}",feature, boundingBox, queue.size());
	
	                 //tiene disponible la peticion de numero de hits
	
	             	 int hits = harvester.getHitsTotal(feature,boundingBox);
	                 log("feature={}, hits={}",feature, hits);
	
	                 if (hits == -1) {
	                     log("fail, enqueue boundingBox={}", feature, boundingBox);
	                     queue.add(boundingBox);
	                     timeOutQueue.add(numTimeOut);
	                     incNumErrors(task);
	                 } 
	                 else if(hits == -2){
                    	 if(numTimeOut < properties.getHarvester().getMAX_NUM_TIMEOUTS()){
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
                        	 if(numTimeOut < properties.getHarvester().getMAX_NUM_TIMEOUTS()){
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
	        	int factor = task.getNamespace().getSource().getFactorK();
	        	BoundingBox boundingBox = task.getNamespace().getSource().getBoundingBox();
	
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
	
	        			int ids = harvester.extractIdentifiers(feature,bbox);
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
	    		while(!errorBoundingBox.isEmpty() && task.getNumErrors() < properties.getHarvester().getMAX_NUM_ERRORS()){
	    			BoundingBox bbox = errorBoundingBox.remove();
	    			int ids = harvester.extractIdentifiers(feature,bbox);
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
                log("Esperando {} milisegundos para realizar la siguiente peticion", timeSleep);
                Thread.sleep(timeSleep);
            }

        }catch(Exception e){
            log("Error al esperar para hacer la siguiente petición");
        }
    }

    private void incNumErrors(Task task) {
        task.setNumErrors(task.getNumErrors() + 1);
    }

    private int getThreshold() {
        return task.getNamespace().getSource().getFeaturesThreshold();
    }

    private BoundingBox initialBoundingBox() {
    	return task.getNamespace().getSource().getBoundingBox();
    }


    private SpatialHarvester getWFSHarvester() {
        SpatialHarvester harvester = context.getBean(WFSSpatialHarvester.class);
        harvester.setTask(task);
        return harvester;
    }
    
    public void doSHPHarvest(){
    	FileHarvester harvester = getSHPHarvester();
        
    	//download zip with the SHP
    	File tmpDirectory = new File(TMP_PATH + "_" + Calendar.getInstance().getTimeInMillis());
    	String fileZipName = tmpDirectory + File.separator + ZIP_FILE_NAME;
    	
    	if(!FileToolsUtil.downloadFileHTTP(task.getNamespace().getSource().getEndpointLocation(), fileZipName)){
    		taskService.changeStatus(task, TaskStatus.ERROR);
    		log("Error descargando fichero " + task.getNamespace().getSource().getEndpointLocation());
    		return;
    	}
    	
    	File zipFile = new File(fileZipName);
    	//descomprimo el zip
    	if(!FileToolsUtil.unzipFile(zipFile, tmpDirectory.getAbsolutePath())){
    		taskService.changeStatus(task, TaskStatus.ERROR);
    		log("Error descomprimiendo fichero " + zipFile.getAbsolutePath());
    		return;
    	}
    	//borro zip descargado
    	zipFile.delete();
    	
        //Para cada una de las features del SHP
        String[]featureList = task.getNamespace().getSource().getFeatureType().split(",");

        for(int index = 0; index< featureList.length; index++){
			if(featureList[index].split("#").length != 2){
				incNumErrors(task);
				log("Error, la feature no posee el formato correcto.");
				continue;
			}
    		
        	String feature = featureList[index].split("#")[0].trim();
        	String featureFile = featureList[index].split("#")[1].trim();
        	
        	//obtengo el nombre del SHP de la feature
        	String shpFileName = FileToolsUtil.getFilePath(tmpDirectory,featureFile + SHP_EXTENSION);
        	
        	File shpFile = new File(shpFileName);
	       
        	//extraigo los identifiers
        	log("Begin identifiers extraction");
        	int ids = harvester.extractIdentifiers(feature,shpFile);
        	
        	if(ids == -1){
        		incNumErrors(task);
        		log("fail, idendifier extraction was wrong");
        	}
        	else{
        		log("feature={}, extracted={}", feature,ids);
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
}
