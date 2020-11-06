package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;
import io.github.bkosaraju.pipeline.repository.TaskExecutionConfigRepository;
import io.github.bkosaraju.pipeline.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.bkosaraju.pipeline.domain.TaskExecutionConfig}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TaskExecutionConfigResource {

    private final Logger log = LoggerFactory.getLogger(TaskExecutionConfigResource.class);

    private static final String ENTITY_NAME = "taskExecutionConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskExecutionConfigRepository taskExecutionConfigRepository;

    public TaskExecutionConfigResource(TaskExecutionConfigRepository taskExecutionConfigRepository) {
        this.taskExecutionConfigRepository = taskExecutionConfigRepository;
    }

    /**
     * {@code POST  /task-execution-configs} : Create a new taskExecutionConfig.
     *
     * @param taskExecutionConfig the taskExecutionConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskExecutionConfig, or with status {@code 400 (Bad Request)} if the taskExecutionConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-execution-configs")
    public ResponseEntity<TaskExecutionConfig> createTaskExecutionConfig(@RequestBody TaskExecutionConfig taskExecutionConfig) throws URISyntaxException {
        log.debug("REST request to save TaskExecutionConfig : {}", taskExecutionConfig);
        if (taskExecutionConfig.getId() != null) {
            throw new BadRequestAlertException("A new taskExecutionConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskExecutionConfig result = taskExecutionConfigRepository.save(taskExecutionConfig);
        return ResponseEntity.created(new URI("/api/task-execution-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-execution-configs} : Updates an existing taskExecutionConfig.
     *
     * @param taskExecutionConfig the taskExecutionConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskExecutionConfig,
     * or with status {@code 400 (Bad Request)} if the taskExecutionConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskExecutionConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-execution-configs")
    public ResponseEntity<TaskExecutionConfig> updateTaskExecutionConfig(@RequestBody TaskExecutionConfig taskExecutionConfig) throws URISyntaxException {
        log.debug("REST request to update TaskExecutionConfig : {}", taskExecutionConfig);
        if (taskExecutionConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaskExecutionConfig result = taskExecutionConfigRepository.save(taskExecutionConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskExecutionConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /task-execution-configs} : get all the taskExecutionConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskExecutionConfigs in body.
     */
    @GetMapping("/task-execution-configs")
    public List<TaskExecutionConfig> getAllTaskExecutionConfigs() {
        log.debug("REST request to get all TaskExecutionConfigs");
        return taskExecutionConfigRepository.findAll();
    }

    /**
     * {@code GET  /task-execution-configs/:id} : get the "id" taskExecutionConfig.
     *
     * @param id the id of the taskExecutionConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskExecutionConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-execution-configs/{id}")
    public ResponseEntity<TaskExecutionConfig> getTaskExecutionConfig(@PathVariable Long id) {
        log.debug("REST request to get TaskExecutionConfig : {}", id);
        Optional<TaskExecutionConfig> taskExecutionConfig = taskExecutionConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(taskExecutionConfig);
    }

    /**
     * {@code DELETE  /task-execution-configs/:id} : delete the "id" taskExecutionConfig.
     *
     * @param id the id of the taskExecutionConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-execution-configs/{id}")
    public ResponseEntity<Void> deleteTaskExecutionConfig(@PathVariable Long id) {
        log.debug("REST request to delete TaskExecutionConfig : {}", id);
        taskExecutionConfigRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
