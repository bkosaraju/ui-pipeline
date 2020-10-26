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

import io.github.bkosaraju.pipeline.domain.JobConfig;
import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.repository.JobConfigRepository;
import io.github.bkosaraju.pipeline.service.dto.JobConfigCriteria;

/**
 * Service for executing complex queries for {@link JobConfig} entities in the database.
 * The main input is a {@link JobConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobConfig} or a {@link Page} of {@link JobConfig} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobConfigQueryService extends QueryService<JobConfig> {

    private final Logger log = LoggerFactory.getLogger(JobConfigQueryService.class);

    private final JobConfigRepository jobConfigRepository;

    public JobConfigQueryService(JobConfigRepository jobConfigRepository) {
        this.jobConfigRepository = jobConfigRepository;
    }

    /**
     * Return a {@link List} of {@link JobConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobConfig> findByCriteria(JobConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobConfig> specification = createSpecification(criteria);
        return jobConfigRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobConfig> findByCriteria(JobConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobConfig> specification = createSpecification(criteria);
        return jobConfigRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobConfig> specification = createSpecification(criteria);
        return jobConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link JobConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobConfig> createSpecification(JobConfigCriteria criteria) {
        Specification<JobConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobConfig_.id));
            }
            if (criteria.getConfigKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigKey(), JobConfig_.configKey));
            }
            if (criteria.getConfigValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigValue(), JobConfig_.configValue));
            }
            if (criteria.getConfigType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigType(), JobConfig_.configType));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobId(),
                    root -> root.join(JobConfig_.job, JoinType.LEFT).get(Job_.id)));
            }
        }
        return specification;
    }
}
