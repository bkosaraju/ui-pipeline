package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TaskExecutionConfig}.
 */
public interface TaskExecutionConfigService {

    /**
     * Save a taskExecutionConfig.
     *
     * @param taskExecutionConfig the entity to save.
     * @return the persisted entity.
     */
    TaskExecutionConfig save(TaskExecutionConfig taskExecutionConfig);

    /**
     * Get all the taskExecutionConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskExecutionConfig> findAll(Pageable pageable);


    /**
     * Get the "id" taskExecutionConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskExecutionConfig> findOne(Long id);

    /**
     * Delete the "id" taskExecutionConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
