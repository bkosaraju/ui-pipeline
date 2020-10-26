package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.JobConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link JobConfig}.
 */
public interface JobConfigService {

    /**
     * Save a jobConfig.
     *
     * @param jobConfig the entity to save.
     * @return the persisted entity.
     */
    JobConfig save(JobConfig jobConfig);

    /**
     * Get all the jobConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JobConfig> findAll(Pageable pageable);


    /**
     * Get the "id" jobConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobConfig> findOne(Long id);

    /**
     * Delete the "id" jobConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
