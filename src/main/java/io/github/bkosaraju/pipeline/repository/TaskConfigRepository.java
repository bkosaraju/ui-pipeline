package io.github.bkosaraju.pipeline.repository;

import io.github.bkosaraju.pipeline.domain.TaskConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TaskConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskConfigRepository extends JpaRepository<TaskConfig, Long>, JpaSpecificationExecutor<TaskConfig> {
}
