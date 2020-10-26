package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.domain.JobExecution;
import io.github.bkosaraju.pipeline.service.JobExecutionService;
import io.github.bkosaraju.pipeline.web.rest.errors.BadRequestAlertException;
import io.github.bkosaraju.pipeline.service.dto.JobExecutionCriteria;
import io.github.bkosaraju.pipeline.service.JobExecutionQueryService;

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
 * REST controller for managing {@link io.github.bkosaraju.pipeline.domain.JobExecution}.
 */
@RestController
@RequestMapping("/api")
public class JobExecutionResource {

    private final Logger log = LoggerFactory.getLogger(JobExecutionResource.class);

    private static final String ENTITY_NAME = "jobExecution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobExecutionService jobExecutionService;

    private final JobExecutionQueryService jobExecutionQueryService;

    public JobExecutionResource(JobExecutionService jobExecutionService, JobExecutionQueryService jobExecutionQueryService) {
        this.jobExecutionService = jobExecutionService;
        this.jobExecutionQueryService = jobExecutionQueryService;
    }

    /**
     * {@code POST  /job-executions} : Create a new jobExecution.
     *
     * @param jobExecution the jobExecution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobExecution, or with status {@code 400 (Bad Request)} if the jobExecution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-executions")
    public ResponseEntity<JobExecution> createJobExecution(@RequestBody JobExecution jobExecution) throws URISyntaxException {
        log.debug("REST request to save JobExecution : {}", jobExecution);
        if (jobExecution.getId() != null) {
            throw new BadRequestAlertException("A new jobExecution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobExecution result = jobExecutionService.save(jobExecution);
        return ResponseEntity.created(new URI("/api/job-executions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-executions} : Updates an existing jobExecution.
     *
     * @param jobExecution the jobExecution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobExecution,
     * or with status {@code 400 (Bad Request)} if the jobExecution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobExecution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-executions")
    public ResponseEntity<JobExecution> updateJobExecution(@RequestBody JobExecution jobExecution) throws URISyntaxException {
        log.debug("REST request to update JobExecution : {}", jobExecution);
        if (jobExecution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobExecution result = jobExecutionService.save(jobExecution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobExecution.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-executions} : get all the jobExecutions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobExecutions in body.
     */
    @GetMapping("/job-executions")
    public ResponseEntity<List<JobExecution>> getAllJobExecutions(JobExecutionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobExecutions by criteria: {}", criteria);
        Page<JobExecution> page = jobExecutionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-executions/count} : count all the jobExecutions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/job-executions/count")
    public ResponseEntity<Long> countJobExecutions(JobExecutionCriteria criteria) {
        log.debug("REST request to count JobExecutions by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobExecutionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-executions/:id} : get the "id" jobExecution.
     *
     * @param id the id of the jobExecution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobExecution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-executions/{id}")
    public ResponseEntity<JobExecution> getJobExecution(@PathVariable Long id) {
        log.debug("REST request to get JobExecution : {}", id);
        Optional<JobExecution> jobExecution = jobExecutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobExecution);
    }

    /**
     * {@code DELETE  /job-executions/:id} : delete the "id" jobExecution.
     *
     * @param id the id of the jobExecution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-executions/{id}")
    public ResponseEntity<Void> deleteJobExecution(@PathVariable Long id) {
        log.debug("REST request to delete JobExecution : {}", id);
        jobExecutionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
