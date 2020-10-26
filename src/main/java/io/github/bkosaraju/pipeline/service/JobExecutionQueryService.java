package io.github.bkosaraju.pipeline.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.bkosaraju.pipeline.domain.JobExecution;
import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.repository.JobExecutionRepository;
import io.github.bkosaraju.pipeline.service.dto.JobExecutionCriteria;

/**
 * Service for executing complex queries for {@link JobExecution} entities in the database.
 * The main input is a {@link JobExecutionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobExecution} or a {@link Page} of {@link JobExecution} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobExecutionQueryService extends QueryService<JobExecution> {

    private final Logger log = LoggerFactory.getLogger(JobExecutionQueryService.class);

    private final JobExecutionRepository jobExecutionRepository;

    public JobExecutionQueryService(JobExecutionRepository jobExecutionRepository) {
        this.jobExecutionRepository = jobExecutionRepository;
    }

    /**
     * Return a {@link List} of {@link JobExecution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobExecution> findByCriteria(JobExecutionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobExecution> specification = createSpecification(criteria);
        return jobExecutionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobExecution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobExecution> findByCriteria(JobExecutionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobExecution> specification = createSpecification(criteria);
        return jobExecutionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobExecutionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobExecution> specification = createSpecification(criteria);
        return jobExecutionRepository.count(specification);
    }

    /**
     * Function to convert {@link JobExecutionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobExecution> createSpecification(JobExecutionCriteria criteria) {
        Specification<JobExecution> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobExecution_.id));
            }
            if (criteria.getJobOrderTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobOrderTimestamp(), JobExecution_.jobOrderTimestamp));
            }
            if (criteria.getJobExecutionStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobExecutionStatus(), JobExecution_.jobExecutionStatus));
            }
            if (criteria.getJobExecutionEndTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobExecutionEndTimestamp(), JobExecution_.jobExecutionEndTimestamp));
            }
            if (criteria.getJobExecutionStartTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobExecutionStartTimestamp(), JobExecution_.jobExecutionStartTimestamp));
            }
            if (criteria.getTaskExecutionId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaskExecutionId(),
                    root -> root.join(JobExecution_.taskExecutions, JoinType.LEFT).get(TaskExecution_.id)));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobId(),
                    root -> root.join(JobExecution_.job, JoinType.LEFT).get(Job_.id)));
            }
        }
        return specification;
    }
}
