package io.github.bkosaraju.pipeline.repository;

import io.github.bkosaraju.pipeline.domain.TaskExecution;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TaskExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long>, JpaSpecificationExecutor<TaskExecution> {
}
