package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.*; // for static metamodels
import io.github.bkosaraju.pipeline.domain.GlobalConfig;
import io.github.bkosaraju.pipeline.repository.GlobalConfigRepository;
import io.github.bkosaraju.pipeline.service.dto.GlobalConfigCriteria;
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
 * Service for executing complex queries for {@link GlobalConfig} entities in the database.
 * The main input is a {@link GlobalConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GlobalConfig} or a {@link Page} of {@link GlobalConfig} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GlobalConfigQueryService extends QueryService<GlobalConfig> {
    private final Logger log = LoggerFactory.getLogger(GlobalConfigQueryService.class);

    private final GlobalConfigRepository globalConfigRepository;

    public GlobalConfigQueryService(GlobalConfigRepository globalConfigRepository) {
        this.globalConfigRepository = globalConfigRepository;
    }

    /**
     * Return a {@link List} of {@link GlobalConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GlobalConfig> findByCriteria(GlobalConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GlobalConfig> specification = createSpecification(criteria);
        return globalConfigRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GlobalConfig} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GlobalConfig> findByCriteria(GlobalConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GlobalConfig> specification = createSpecification(criteria);
        return globalConfigRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GlobalConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GlobalConfig> specification = createSpecification(criteria);
        return globalConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link GlobalConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GlobalConfig> createSpecification(GlobalConfigCriteria criteria) {
        Specification<GlobalConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GlobalConfig_.id));
            }
            if (criteria.getConfigKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigKey(), GlobalConfig_.configKey));
            }
            if (criteria.getConfigValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigValue(), GlobalConfig_.configValue));
            }
            if (criteria.getConfigType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigType(), GlobalConfig_.configType));
            }
        }
        return specification;
    }
}
