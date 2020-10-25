package io.github.bkosaraju.pipeline.repository;

import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the JobTaskOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobTaskOrderRepository extends JpaRepository<JobTaskOrder, Long>, JpaSpecificationExecutor<JobTaskOrder> {}
