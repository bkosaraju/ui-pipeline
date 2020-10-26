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

import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;
import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.repository.TaskExecutionConfigRepository;
import io.github.bkosaraju.pipeline.service.dto.TaskExecutionConfigCriteria;

/**
 * Service for executing complex queries for {@link TaskExecutionConfig} entities in the database.
 * The main input is a {@link TaskExecutionConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskExecutionConfig} or a {@link Page} of {@link TaskExecutionConfig} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskExecutionConfigQueryService extends QueryService<TaskExecutionConfig> {

    private final Logger log = LoggerFactory.getLogger(TaskExecutionConfigQueryService.class);

    private final TaskExecutionConfigRepository taskExecutionConfigRepository;

    public TaskExecutionConfigQueryService(TaskExecutionConfigRepository taskExecutionConfigRepository) {
        this.taskExecutionConfigRepository = taskExecutionConfigRepository;
    }

    /**
     * Return a {@link List} of {@link TaskExecutionConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskExecutionConfig> findByCriteria(TaskExecutionConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaskExecutionConfig> specification = createSpecification(criteria);
        return taskExecutionConfigRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TaskExecutionConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskExecutionConfig> findByCriteria(TaskExecutionConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaskExecutionConfig> specification = createSpecification(criteria);
        return taskExecutionConfigRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskExecutionConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaskExecutionConfig> specification = createSpecification(criteria);
        return taskExecutionConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskExecutionConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaskExecutionConfig> createSpecification(TaskExecutionConfigCriteria criteria) {
        Specification<TaskExecutionConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaskExecutionConfig_.id));
            }
            if (criteria.getConfigKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigKey(), TaskExecutionConfig_.configKey));
            }
            if (criteria.getConfigValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigValue(), TaskExecutionConfig_.configValue));
            }
            if (criteria.getConfigVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConfigVersion(), TaskExecutionConfig_.configVersion));
            }
            if (criteria.getTaskExecutionId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaskExecutionId(),
                    root -> root.join(TaskExecutionConfig_.taskExecution, JoinType.LEFT).get(TaskExecution_.id)));
            }
        }
        return specification;
    }
}
