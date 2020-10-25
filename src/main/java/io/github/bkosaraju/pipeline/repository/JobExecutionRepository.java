package io.github.bkosaraju.pipeline.repository;

import io.github.bkosaraju.pipeline.domain.JobExecution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the JobExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, Long>, JpaSpecificationExecutor<JobExecution> {}
