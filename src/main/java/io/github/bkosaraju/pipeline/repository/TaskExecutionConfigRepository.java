package io.github.bkosaraju.pipeline.repository;

import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TaskExecutionConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskExecutionConfigRepository extends JpaRepository<TaskExecutionConfig, Long>, JpaSpecificationExecutor<TaskExecutionConfig> {
}
