package es.unizar.iaaa.pid.harvester.connectors.shp;

import java.io.File;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.Resource;
import es.unizar.iaaa.pid.domain.Source;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
import es.unizar.iaaa.pid.harvester.connectors.FileHarvester;
import es.unizar.iaaa.pid.service.ChangeService;

@Component
@Scope("prototype")
public class SHPHarvester implements FileHarvester {
    private static final Logger LOGGER = LoggerFactory.getLogger(SHPHarvester.class);
    private final ChangeService changeService;
    private Task task;
    private Source source;

    @Autowired
    public SHPHarvester(ChangeService changeService) {
        this.changeService = changeService;
    }

    @Override
    public void setTask(Task task) {
        this.task = task;
        this.source = task.getNamespace().getSource();
    }
    
    @Override
    public int extractIdentifiers(String featureName, File shpFile) {
    	int valid = 0;
    	FeatureCollection<SimpleFeatureType, SimpleFeature> collection = null;
    	
    	try{
            Map<String, Object> map = new HashMap<>();
            map.put("url", shpFile.toURI().toURL());
            ShapefileDataStore dataStore = (ShapefileDataStore)DataStoreFinder.getDataStore(map);
            dataStore.setCharset(Charset.forName("UTF-8"));
            String typeName = dataStore.getTypeNames()[0];

            FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
                    .getFeatureSource(typeName);
            
            collection = source.getFeatures();
    	}
    	catch(Exception e){
    		error("No se ha podido abrir el fichero DBF.",e);
    		return -1;
    	}
    	
    	FeatureIterator<SimpleFeature> features = collection.features();
    	
    	while (features.hasNext()) {
    		SimpleFeature feature = features.next();
    		//Se el identificador
        	String localId = (String) feature.getAttribute(source.getNameItem()).toString();
        	if(localId == null || localId.equals("0") || localId.equals("")){
        		error("Error el atributo seleccionado como NameItem no existe");
        		continue;
        	}
        	
        	Identifier identifier = new Identifier()
						                    .namespace(task.getNamespace().getNamespace())
						                    .localId(localId)
						                    .versionId(null)
						                    .beginLifespanVersion(null)
						                    .alternateId(null);
        	
        	Resource resource = new Resource()
        								.locator(source.getEndpointLocation())
        								.resourceType(ResourceType.SPATIAL_OBJECT);
        	
        	Change change = new Change();
        	change.setIdentifier(identifier);
        	change.setResource(resource);
            change.setTask(this.task);
            change.setAction(ChangeAction.ISSUED);
            change.setChangeTimestamp(Instant.now());
            change.setFeature(featureName);
            changeService.createChange(change);
            debug("{} is FOUND ", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
            valid++;
    	}
    	log("{} were retrieved", valid);
    	return valid;
    }

    private void log(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.info("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    private void error(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.error("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

    private void debug(String msg, Object... objects) {
        List<Object> l = new ArrayList<>();
        l.addAll(Arrays.asList(task.getType(), task.getId(), task.getNamespace().getNamespace()));
        l.addAll(Arrays.asList(objects));
        LOGGER.debug("Task \"{}:{}\" for namespace \"{}\" : " + msg, l.toArray());
    }

}
