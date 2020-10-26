package io.github.bkosaraju.pipeline.repository;

import io.github.bkosaraju.pipeline.domain.JobConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the JobConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobConfigRepository extends JpaRepository<JobConfig, Long>, JpaSpecificationExecutor<JobConfig> {
}
