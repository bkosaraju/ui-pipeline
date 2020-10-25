package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.domain.Job;
import io.github.bkosaraju.pipeline.repository.JobRepository;
import io.github.bkosaraju.pipeline.service.dto.JobCriteria;
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
 * Service for executing complex queries for {@link Job} entities in the database.
 * The main input is a {@link JobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Job} or a {@link Page} of {@link Job} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobQueryService extends QueryService<Job> {
    private final Logger log = LoggerFactory.getLogger(JobQueryService.class);

    private final JobRepository jobRepository;

    public JobQueryService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Return a {@link List} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Job> findByCriteria(JobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Job} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Job> findByCriteria(JobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Job> specification = createSpecification(criteria);
        return jobRepository.count(specification);
    }

    /**
     * Function to convert {@link JobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Job> createSpecification(JobCriteria criteria) {
        Specification<Job> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Job_.id));
            }
            if (criteria.getJobName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobName(), Job_.jobName));
            }
            if (criteria.getJobStatusFlag() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobStatusFlag(), Job_.jobStatusFlag));
            }
            if (criteria.getCreateTimeStamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateTimeStamp(), Job_.createTimeStamp));
            }
            if (criteria.getJobConfigId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJobConfigId(), root -> root.join(Job_.jobConfigs, JoinType.LEFT).get(JobConfig_.id))
                    );
            }
            if (criteria.getJobTaskOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJobTaskOrderId(),
                            root -> root.join(Job_.jobTaskOrders, JoinType.LEFT).get(JobTaskOrder_.id)
                        )
                    );
            }
            if (criteria.getJobExecutionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJobExecutionId(),
                            root -> root.join(Job_.jobExecutions, JoinType.LEFT).get(JobExecution_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
