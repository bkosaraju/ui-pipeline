package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link JobTaskOrder}.
 */
public interface JobTaskOrderService {
    /**
     * Save a jobTaskOrder.
     *
     * @param jobTaskOrder the entity to save.
     * @return the persisted entity.
     */
    JobTaskOrder save(JobTaskOrder jobTaskOrder);

    /**
     * Get all the jobTaskOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JobTaskOrder> findAll(Pageable pageable);

    /**
     * Get the "id" jobTaskOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JobTaskOrder> findOne(Long id);

    /**
     * Delete the "id" jobTaskOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
