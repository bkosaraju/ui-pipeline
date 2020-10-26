package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.domain.TaskConfig;
import io.github.bkosaraju.pipeline.service.TaskConfigService;
import io.github.bkosaraju.pipeline.web.rest.errors.BadRequestAlertException;
import io.github.bkosaraju.pipeline.service.dto.TaskConfigCriteria;
import io.github.bkosaraju.pipeline.service.TaskConfigQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.bkosaraju.pipeline.domain.TaskConfig}.
 */
@RestController
@RequestMapping("/api")
public class TaskConfigResource {

    private final Logger log = LoggerFactory.getLogger(TaskConfigResource.class);

    private static final String ENTITY_NAME = "taskConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskConfigService taskConfigService;

    private final TaskConfigQueryService taskConfigQueryService;

    public TaskConfigResource(TaskConfigService taskConfigService, TaskConfigQueryService taskConfigQueryService) {
        this.taskConfigService = taskConfigService;
        this.taskConfigQueryService = taskConfigQueryService;
    }

    /**
     * {@code POST  /task-configs} : Create a new taskConfig.
     *
     * @param taskConfig the taskConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskConfig, or with status {@code 400 (Bad Request)} if the taskConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-configs")
    public ResponseEntity<TaskConfig> createTaskConfig(@RequestBody TaskConfig taskConfig) throws URISyntaxException {
        log.debug("REST request to save TaskConfig : {}", taskConfig);
        if (taskConfig.getId() != null) {
            throw new BadRequestAlertException("A new taskConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskConfig result = taskConfigService.save(taskConfig);
        return ResponseEntity.created(new URI("/api/task-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-configs} : Updates an existing taskConfig.
     *
     * @param taskConfig the taskConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskConfig,
     * or with status {@code 400 (Bad Request)} if the taskConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-configs")
    public ResponseEntity<TaskConfig> updateTaskConfig(@RequestBody TaskConfig taskConfig) throws URISyntaxException {
        log.debug("REST request to update TaskConfig : {}", taskConfig);
        if (taskConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaskConfig result = taskConfigService.save(taskConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /task-configs} : get all the taskConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskConfigs in body.
     */
    @GetMapping("/task-configs")
    public ResponseEntity<List<TaskConfig>> getAllTaskConfigs(TaskConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TaskConfigs by criteria: {}", criteria);
        Page<TaskConfig> page = taskConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /task-configs/count} : count all the taskConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/task-configs/count")
    public ResponseEntity<Long> countTaskConfigs(TaskConfigCriteria criteria) {
        log.debug("REST request to count TaskConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(taskConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /task-configs/:id} : get the "id" taskConfig.
     *
     * @param id the id of the taskConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-configs/{id}")
    public ResponseEntity<TaskConfig> getTaskConfig(@PathVariable Long id) {
        log.debug("REST request to get TaskConfig : {}", id);
        Optional<TaskConfig> taskConfig = taskConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskConfig);
    }

    /**
     * {@code DELETE  /task-configs/:id} : delete the "id" taskConfig.
     *
     * @param id the id of the taskConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-configs/{id}")
    public ResponseEntity<Void> deleteTaskConfig(@PathVariable Long id) {
        log.debug("REST request to delete TaskConfig : {}", id);
        taskConfigService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
