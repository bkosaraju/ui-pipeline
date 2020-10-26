package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.TaskConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TaskConfig}.
 */
public interface TaskConfigService {

    /**
     * Save a taskConfig.
     *
     * @param taskConfig the entity to save.
     * @return the persisted entity.
     */
    TaskConfig save(TaskConfig taskConfig);

    /**
     * Get all the taskConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskConfig> findAll(Pageable pageable);


    /**
     * Get the "id" taskConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskConfig> findOne(Long id);

    /**
     * Delete the "id" taskConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
