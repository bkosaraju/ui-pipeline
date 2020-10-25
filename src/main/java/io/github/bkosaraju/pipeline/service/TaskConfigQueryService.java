package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.domain.TaskConfig;
import io.github.bkosaraju.pipeline.repository.TaskConfigRepository;
import io.github.bkosaraju.pipeline.service.dto.TaskConfigCriteria;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link TaskConfig} entities in the database.
 * The main input is a {@link TaskConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskConfig} or a {@link Page} of {@link TaskConfig} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskConfigQueryService extends QueryService<TaskConfig> {
    private final Logger log = LoggerFactory.getLogger(TaskConfigQueryService.class);

    private final TaskConfigRepository taskConfigRepository;

    public TaskConfigQueryService(TaskConfigRepository taskConfigRepository) {
        this.taskConfigRepository = taskConfigRepository;
    }

    /**
     * Return a {@link List} of {@link TaskConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskConfig> findByCriteria(TaskConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaskConfig> specification = createSpecification(criteria);
        return taskConfigRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TaskConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskConfig> findByCriteria(TaskConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaskConfig> specification = createSpecification(criteria);
        return taskConfigRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaskConfig> specification = createSpecification(criteria);
        return taskConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaskConfig> createSpecification(TaskConfigCriteria criteria) {
        Specification<TaskConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaskConfig_.id));
            }
            if (criteria.getConfigKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigKey(), TaskConfig_.configKey));
            }
            if (criteria.getConfigValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigValue(), TaskConfig_.configValue));
            }
            if (criteria.getConfigType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigType(), TaskConfig_.configType));
            }
            if (criteria.getConfigVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigVersion(), TaskConfig_.configVersion));
            }
            if (criteria.getTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTaskId(), root -> root.join(TaskConfig_.task, JoinType.LEFT).get(Task_.id))
                    );
            }
        }
        return specification;
    }
}
