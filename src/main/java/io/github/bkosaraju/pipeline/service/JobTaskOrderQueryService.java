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

import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.repository.JobTaskOrderRepository;
import io.github.bkosaraju.pipeline.service.dto.JobTaskOrderCriteria;

/**
 * Service for executing complex queries for {@link JobTaskOrder} entities in the database.
 * The main input is a {@link JobTaskOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobTaskOrder} or a {@link Page} of {@link JobTaskOrder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobTaskOrderQueryService extends QueryService<JobTaskOrder> {

    private final Logger log = LoggerFactory.getLogger(JobTaskOrderQueryService.class);

    private final JobTaskOrderRepository jobTaskOrderRepository;

    public JobTaskOrderQueryService(JobTaskOrderRepository jobTaskOrderRepository) {
        this.jobTaskOrderRepository = jobTaskOrderRepository;
    }

    /**
     * Return a {@link List} of {@link JobTaskOrder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobTaskOrder> findByCriteria(JobTaskOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobTaskOrder> specification = createSpecification(criteria);
        return jobTaskOrderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobTaskOrder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobTaskOrder> findByCriteria(JobTaskOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobTaskOrder> specification = createSpecification(criteria);
        return jobTaskOrderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobTaskOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobTaskOrder> specification = createSpecification(criteria);
        return jobTaskOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link JobTaskOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobTaskOrder> createSpecification(JobTaskOrderCriteria criteria) {
        Specification<JobTaskOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobTaskOrder_.id));
            }
            if (criteria.getTaskSeqId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaskSeqId(), JobTaskOrder_.taskSeqId));
            }
            if (criteria.getJobTaskStatusFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getJobTaskStatusFlag(), JobTaskOrder_.jobTaskStatusFlag));
            }
            if (criteria.getConfigVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConfigVersion(), JobTaskOrder_.configVersion));
            }
            if (criteria.getJobId() != null) {
                specification = specification.and(buildSpecification(criteria.getJobId(),
                    root -> root.join(JobTaskOrder_.job, JoinType.LEFT).get(Job_.id)));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaskId(),
                    root -> root.join(JobTaskOrder_.task, JoinType.LEFT).get(Task_.id)));
            }
        }
        return specification;
    }
}
