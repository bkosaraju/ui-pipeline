package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.domain.JobConfig;
import io.github.bkosaraju.pipeline.service.JobConfigQueryService;
import io.github.bkosaraju.pipeline.service.JobConfigService;
import io.github.bkosaraju.pipeline.service.dto.JobConfigCriteria;
import io.github.bkosaraju.pipeline.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link io.github.bkosaraju.pipeline.domain.JobConfig}.
 */
@RestController
@RequestMapping("/api")
public class JobConfigResource {
    private final Logger log = LoggerFactory.getLogger(JobConfigResource.class);

    private static final String ENTITY_NAME = "jobConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobConfigService jobConfigService;

    private final JobConfigQueryService jobConfigQueryService;

    public JobConfigResource(JobConfigService jobConfigService, JobConfigQueryService jobConfigQueryService) {
        this.jobConfigService = jobConfigService;
        this.jobConfigQueryService = jobConfigQueryService;
    }

    /**
     * {@code POST  /job-configs} : Create a new jobConfig.
     *
     * @param jobConfig the jobConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobConfig, or with status {@code 400 (Bad Request)} if the jobConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-configs")
    public ResponseEntity<JobConfig> createJobConfig(@RequestBody JobConfig jobConfig) throws URISyntaxException {
        log.debug("REST request to save JobConfig : {}", jobConfig);
        if (jobConfig.getId() != null) {
            throw new BadRequestAlertException("A new jobConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobConfig result = jobConfigService.save(jobConfig);
        return ResponseEntity
            .created(new URI("/api/job-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-configs} : Updates an existing jobConfig.
     *
     * @param jobConfig the jobConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobConfig,
     * or with status {@code 400 (Bad Request)} if the jobConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-configs")
    public ResponseEntity<JobConfig> updateJobConfig(@RequestBody JobConfig jobConfig) throws URISyntaxException {
        log.debug("REST request to update JobConfig : {}", jobConfig);
        if (jobConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobConfig result = jobConfigService.save(jobConfig);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-configs} : get all the jobConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobConfigs in body.
     */
    @GetMapping("/job-configs")
    public ResponseEntity<List<JobConfig>> getAllJobConfigs(JobConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobConfigs by criteria: {}", criteria);
        Page<JobConfig> page = jobConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-configs/count} : count all the jobConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/job-configs/count")
    public ResponseEntity<Long> countJobConfigs(JobConfigCriteria criteria) {
        log.debug("REST request to count JobConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-configs/:id} : get the "id" jobConfig.
     *
     * @param id the id of the jobConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-configs/{id}")
    public ResponseEntity<JobConfig> getJobConfig(@PathVariable Long id) {
        log.debug("REST request to get JobConfig : {}", id);
        Optional<JobConfig> jobConfig = jobConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobConfig);
    }

    /**
     * {@code DELETE  /job-configs/:id} : delete the "id" jobConfig.
     *
     * @param id the id of the jobConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-configs/{id}")
    public ResponseEntity<Void> deleteJobConfig(@PathVariable Long id) {
        log.debug("REST request to delete JobConfig : {}", id);
        jobConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
