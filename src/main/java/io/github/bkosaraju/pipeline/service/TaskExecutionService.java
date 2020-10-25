package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.TaskExecution;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TaskExecution}.
 */
public interface TaskExecutionService {
    /**
     * Save a taskExecution.
     *
     * @param taskExecution the entity to save.
     * @return the persisted entity.
     */
    TaskExecution save(TaskExecution taskExecution);

    /**
     * Get all the taskExecutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskExecution> findAll(Pageable pageable);

    /**
     * Get the "id" taskExecution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskExecution> findOne(Long id);

    /**
     * Delete the "id" taskExecution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
