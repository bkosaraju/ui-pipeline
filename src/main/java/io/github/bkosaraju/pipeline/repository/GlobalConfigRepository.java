package io.github.bkosaraju.pipeline.repository;

import io.github.bkosaraju.pipeline.domain.GlobalConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the GlobalConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalConfigRepository extends JpaRepository<GlobalConfig, Long>, JpaSpecificationExecutor<GlobalConfig> {
}
