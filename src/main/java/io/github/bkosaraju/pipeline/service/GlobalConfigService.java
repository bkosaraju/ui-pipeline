package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.domain.GlobalConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link GlobalConfig}.
 */
public interface GlobalConfigService {

    /**
     * Save a globalConfig.
     *
     * @param globalConfig the entity to save.
     * @return the persisted entity.
     */
    GlobalConfig save(GlobalConfig globalConfig);

    /**
     * Get all the globalConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GlobalConfig> findAll(Pageable pageable);


    /**
     * Get the "id" globalConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GlobalConfig> findOne(Long id);

    /**
     * Delete the "id" globalConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
