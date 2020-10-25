package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.JobExecution;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link JobExecution}.
 */
public interface JobExecutionService {
    /**
     * Save a jobExecution.
     *
     * @param jobExecution the entity to save.
     * @return the persisted entity.
     */
    JobExecution save(JobExecution jobExecution);

    /**
     * Get all the jobExecutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JobExecution> findAll(Pageable pageable);

    /**
     * Get the "id" jobExecution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobExecution> findOne(Long id);

    /**
     * Delete the "id" jobExecution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
