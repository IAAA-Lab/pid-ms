package es.unizar.iaaa.pid.harvester.tasks;

import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.harvester.connectors.SpatialHarvester;
import es.unizar.iaaa.pid.harvester.connectors.wfs.WFSSpatialHarvester;
import es.unizar.iaaa.pid.service.NamespaceService;
import es.unizar.iaaa.pid.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static es.unizar.iaaa.pid.domain.enumeration.ProcessStatus.PENDING_TRANSFERRING_HARVEST;


@Component
@Scope("prototype")
class HarvestTask extends AbstractTaskRunner {
    private static final int MAX_NUM_ERRORS = 20;
    private static final int MAX_NUM_TIMEOUTS = 8;
    protected ApplicationContext context;

    @Autowired
    public HarvestTask(ApplicationContext context, NamespaceService namespaceService, TaskService taskService) {
        super(namespaceService, taskService);
        this.context = context;
    }

    @Override
    protected void doTask() {
        SpatialHarvester harvester = getHarvester();
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
	        	 while (!queue.isEmpty() && task.getNumErrors() < MAX_NUM_ERRORS) {
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
                    	 if(numTimeOut < MAX_NUM_TIMEOUTS){
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
                        	 if(numTimeOut < MAX_NUM_TIMEOUTS){
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
	    		while(!errorBoundingBox.isEmpty() && task.getNumErrors() < MAX_NUM_ERRORS){
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


    private SpatialHarvester getHarvester() {
        SpatialHarvester harvester = context.getBean(WFSSpatialHarvester.class);
        harvester.setTask(task);
        return harvester;
    }

    @Override
    protected ProcessStatus getNextStep() {
        return PENDING_TRANSFERRING_HARVEST;
    }
}
