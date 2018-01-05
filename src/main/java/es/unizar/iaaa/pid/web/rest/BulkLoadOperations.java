package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;
import es.unizar.iaaa.pid.harvester.tasks.UpdatingTask;
import es.unizar.iaaa.pid.service.FeatureService;
import es.unizar.iaaa.pid.service.NamespaceDTOService;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import es.unizar.iaaa.pid.service.TaskService;
import es.unizar.iaaa.pid.service.dto.NamespaceDTO;
import es.unizar.iaaa.pid.service.mapper.NamespaceMapper;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.web.rest.vm.CsvData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BulkLoadOperations {

    private final Logger log = LoggerFactory.getLogger(NamespaceResource.class);

    private final NamespaceDTOService namespaceService;
    private final NamespaceMapper namespaceMapper;
    private final TaskService taskService;
    private final PersistentIdentifierService persistentIdentifierService;
    private final FeatureService featureService;

    public BulkLoadOperations(NamespaceDTOService namespaceService, NamespaceMapper namespaceMapper,
                              TaskService taskService,
                              PersistentIdentifierService persistentIdentifierService, FeatureService featureService) {
        this.namespaceService = namespaceService;
        this.namespaceMapper = namespaceMapper;
        this.taskService = taskService;
        this.persistentIdentifierService = persistentIdentifierService;
        this.featureService = featureService;
    }

    /**
     * POST /namespace/updateCSV : update the PIDs
     *
     * @param csvData the csvData information to update
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/namespaces/updateCSV")
    @Timed
    public ResponseEntity<Void> updateCSVNamespace(@RequestBody CsvData csvData) {
        log.debug("REST request to update with csv data the PIDs of namespace : {}", csvData.getNamespaceId());

        //check if there is a task executing over the namespace
        NamespaceDTO namespace = namespaceService.findOne(csvData.getNamespaceId());
        if (namespace.getProcessStatus() != ProcessStatus.NONE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(HeaderUtil.createFailureAlert("Updating proccess", "taskInExecuting",
                "There is a task in executing over this Namespace")).body(null);
        }
        namespace.setProcessStatus(ProcessStatus.UPDATING_PIDS);
        namespace = namespaceService.save(namespace);


        UpdatingTask updatingTask = new UpdatingTask(taskService, namespaceService, namespaceMapper,
            persistentIdentifierService, featureService, csvData, namespace);

        Thread thread = new Thread(updatingTask);
        thread.start();

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Updating proccess", csvData.toString())).build();
    }
}
