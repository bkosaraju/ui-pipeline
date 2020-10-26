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

import io.github.bkosaraju.pipeline.domain.TaskExecution;
import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.repository.TaskExecutionRepository;
import io.github.bkosaraju.pipeline.service.dto.TaskExecutionCriteria;

/**
 * Service for executing complex queries for {@link TaskExecution} entities in the database.
 * The main input is a {@link TaskExecutionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskExecution} or a {@link Page} of {@link TaskExecution} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskExecutionQueryService extends QueryService<TaskExecution> {

    private final Logger log = LoggerFactory.getLogger(TaskExecutionQueryService.class);

    private final TaskExecutionRepository taskExecutionRepository;

    public TaskExecutionQueryService(TaskExecutionRepository taskExecutionRepository) {
        this.taskExecutionRepository = taskExecutionRepository;
    }

    /**
     * Return a {@link List} of {@link TaskExecution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskExecution> findByCriteria(TaskExecutionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaskExecution> specification = createSpecification(criteria);
        return taskExecutionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TaskExecution} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskExecution> findByCriteria(TaskExecutionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaskExecution> specification = createSpecification(criteria);
        return taskExecutionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskExecutionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaskExecution> specification = createSpecification(criteria);
        return taskExecutionRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskExecutionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaskExecution> createSpecification(TaskExecutionCriteria criteria) {
        Specification<TaskExecution> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaskExecution_.id));
            }
            if (criteria.getTaskExecutionTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaskExecutionTimestamp(), TaskExecution_.taskExecutionTimestamp));
            }
            if (criteria.getJobOrderTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobOrderTimestamp(), TaskExecution_.jobOrderTimestamp));
            }
            if (criteria.getTaskExecutionStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaskExecutionStatus(), TaskExecution_.taskExecutionStatus));
            }
            if (criteria.getTaskExecutionConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaskExecutionConfigId(),
                    root -> root.join(TaskExecution_.taskExecutionConfigs, JoinType.LEFT).get(TaskExecutionConfig_.id)));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaskId(),
                    root -> root.join(TaskExecution_.task, JoinType.LEFT).get(Task_.id)));
            }
            if (criteria.getJobExecutionId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobExecutionId(),
                    root -> root.join(TaskExecution_.jobExecution, JoinType.LEFT).get(JobExecution_.id)));
            }
        }
        return specification;
    }
}
