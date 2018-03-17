package es.unizar.iaaa.pid.harvester.connectors.shp;

import es.unizar.iaaa.pid.domain.*;
import es.unizar.iaaa.pid.domain.enumeration.ChangeAction;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
import es.unizar.iaaa.pid.harvester.connectors.FileHarvester;
import es.unizar.iaaa.pid.service.ChangeService;
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

import java.io.File;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.*;

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
    public int extractIdentifiers(Feature featureType, File shpFile) {
    	int valid = 0;
    	FeatureCollection<SimpleFeatureType, SimpleFeature> collection;

    	try{
            Map<String, Object> map = new HashMap<>();
            map.put("url", shpFile.toURI().toURL());
            ShapefileDataStore dataStore = (ShapefileDataStore)DataStoreFinder.getDataStore(map);
            dataStore.setCharset(Charset.forName("UTF-8"));
            String typeName = dataStore.getTypeNames()[0];

            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore
                    .getFeatureSource(typeName);

            collection = featureSource.getFeatures();
    	}
    	catch(Exception e){
    		error("No se ha podido abrir el fichero DBF.",e);
    		return -1;
    	}

    	FeatureIterator<SimpleFeature> features = collection.features();

    	while (features.hasNext()) {
    		SimpleFeature feature = features.next();
    		//Se el identificador
    		String localId = "";
    		try{
    			localId = (String) feature.getAttribute(featureType.getNameItem()).toString();
    		}
    		catch(Exception e){
    			error("No se encuentra el campo {} en el fichero DBF.",featureType.getNameItem(), e);
    			return -1;
    		}
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
        								.locator(source.getEndpointLocation() + "#" + shpFile.getName() +"/" +
        											task.getNamespace().getNamespace() +"/" + localId)
        								.resourceType(ResourceType.SPATIAL_OBJECT);

        	Change change = new Change();
        	change.setIdentifier(identifier);
        	change.setResource(resource);
            change.setTask(this.task);
            change.setAction(ChangeAction.ISSUED);
            change.setChangeTimestamp(Instant.now());
            change.setFeature(featureType);
            changeService.createChange(change);
            debug("{} is FOUND ", PersistentIdentifier.computeExternalUrnFromIdentifier(identifier));
            valid++;
    	}
    	log("{} were retrieved", valid);
    	return valid;
    }

    private String TASK_FOR_NAMESPACE = "Task \"{}:{}\" for namespace \"{}\" : ";

    public void log(String msg, Object... objects) {
        LOGGER.info(TASK_FOR_NAMESPACE + msg, buildLoggerParameters(objects));
    }

    public void error(String msg, Object... objects) {
        LOGGER.error(TASK_FOR_NAMESPACE + msg, buildLoggerParameters(objects));
    }

    public void debug(String msg, Object... objects) {
        LOGGER.debug(TASK_FOR_NAMESPACE + msg, buildLoggerParameters(objects));
    }

    private Object[] buildLoggerParameters(Object[] objects) {
        List<Object> l = new ArrayList<>();
        l.add(task.getType());
        l.add(task.getId());
        l.add(task.getNamespace().getNamespace());
        l.addAll(Arrays.asList(objects));
        return l.toArray();
    }
}
