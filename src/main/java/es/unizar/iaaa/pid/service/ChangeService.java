package es.unizar.iaaa.pid.service;

import es.unizar.iaaa.pid.domain.Change;
import es.unizar.iaaa.pid.domain.Task;
import es.unizar.iaaa.pid.repository.ChangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

    /**
 * Service class for managing changes.
     */
@Service
@Transactional
public class ChangeService {

    private final Logger log = LoggerFactory.getLogger(ChangeService.class);

    private final ChangeRepository changeRepository;

    public ChangeService(ChangeRepository changeRepository) {
        this.changeRepository = changeRepository;
}

    public void createChange(Change change) {
        changeRepository.save(change);
        log.debug("Created Information for Change: {}", change);
    }

    public List<Change> findAll() {
        return changeRepository.findAll();
    }

    public void deleteAll() {
        changeRepository.deleteAll();;
    }

    public List<Change> findByTask(Task task) {
        return changeRepository.findByTask(task);
    }

    public Page<Change> findByTaskOrderById(Task task, Pageable pageable) {
        return changeRepository.findByTaskOrderById(task, pageable);
    }

}

