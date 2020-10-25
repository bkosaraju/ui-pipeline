package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import io.github.bkosaraju.pipeline.service.JobTaskOrderQueryService;
import io.github.bkosaraju.pipeline.service.JobTaskOrderService;
import io.github.bkosaraju.pipeline.service.dto.JobTaskOrderCriteria;
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
 * REST controller for managing {@link io.github.bkosaraju.pipeline.domain.JobTaskOrder}.
 */
@RestController
@RequestMapping("/api")
public class JobTaskOrderResource {
    private final Logger log = LoggerFactory.getLogger(JobTaskOrderResource.class);

    private static final String ENTITY_NAME = "jobTaskOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobTaskOrderService jobTaskOrderService;

    private final JobTaskOrderQueryService jobTaskOrderQueryService;

    public JobTaskOrderResource(JobTaskOrderService jobTaskOrderService, JobTaskOrderQueryService jobTaskOrderQueryService) {
        this.jobTaskOrderService = jobTaskOrderService;
        this.jobTaskOrderQueryService = jobTaskOrderQueryService;
    }

    /**
     * {@code POST  /job-task-orders} : Create a new jobTaskOrder.
     *
     * @param jobTaskOrder the jobTaskOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobTaskOrder, or with status {@code 400 (Bad Request)} if the jobTaskOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-task-orders")
    public ResponseEntity<JobTaskOrder> createJobTaskOrder(@RequestBody JobTaskOrder jobTaskOrder) throws URISyntaxException {
        log.debug("REST request to save JobTaskOrder : {}", jobTaskOrder);
        if (jobTaskOrder.getId() != null) {
            throw new BadRequestAlertException("A new jobTaskOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobTaskOrder result = jobTaskOrderService.save(jobTaskOrder);
        return ResponseEntity
            .created(new URI("/api/job-task-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-task-orders} : Updates an existing jobTaskOrder.
     *
     * @param jobTaskOrder the jobTaskOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobTaskOrder,
     * or with status {@code 400 (Bad Request)} if the jobTaskOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobTaskOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-task-orders")
    public ResponseEntity<JobTaskOrder> updateJobTaskOrder(@RequestBody JobTaskOrder jobTaskOrder) throws URISyntaxException {
        log.debug("REST request to update JobTaskOrder : {}", jobTaskOrder);
        if (jobTaskOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobTaskOrder result = jobTaskOrderService.save(jobTaskOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobTaskOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /job-task-orders} : get all the jobTaskOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobTaskOrders in body.
     */
    @GetMapping("/job-task-orders")
    public ResponseEntity<List<JobTaskOrder>> getAllJobTaskOrders(JobTaskOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobTaskOrders by criteria: {}", criteria);
        Page<JobTaskOrder> page = jobTaskOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-task-orders/count} : count all the jobTaskOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/job-task-orders/count")
    public ResponseEntity<Long> countJobTaskOrders(JobTaskOrderCriteria criteria) {
        log.debug("REST request to count JobTaskOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobTaskOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-task-orders/:id} : get the "id" jobTaskOrder.
     *
     * @param id the id of the jobTaskOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobTaskOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-task-orders/{id}")
    public ResponseEntity<JobTaskOrder> getJobTaskOrder(@PathVariable Long id) {
        log.debug("REST request to get JobTaskOrder : {}", id);
        Optional<JobTaskOrder> jobTaskOrder = jobTaskOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobTaskOrder);
    }

    /**
     * {@code DELETE  /job-task-orders/:id} : delete the "id" jobTaskOrder.
     *
     * @param id the id of the jobTaskOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-task-orders/{id}")
    public ResponseEntity<Void> deleteJobTaskOrder(@PathVariable Long id) {
        log.debug("REST request to delete JobTaskOrder : {}", id);
        jobTaskOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
