package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.service.JobTaskOrderService;
import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import io.github.bkosaraju.pipeline.repository.JobTaskOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link JobTaskOrder}.
 */
@Service
@Transactional
public class JobTaskOrderServiceImpl implements JobTaskOrderService {

    private final Logger log = LoggerFactory.getLogger(JobTaskOrderServiceImpl.class);

    private final JobTaskOrderRepository jobTaskOrderRepository;

    public JobTaskOrderServiceImpl(JobTaskOrderRepository jobTaskOrderRepository) {
        this.jobTaskOrderRepository = jobTaskOrderRepository;
    }

    @Override
    public JobTaskOrder save(JobTaskOrder jobTaskOrder) {
        log.debug("Request to save JobTaskOrder : {}", jobTaskOrder);
        return jobTaskOrderRepository.save(jobTaskOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobTaskOrder> findAll(Pageable pageable) {
        log.debug("Request to get all JobTaskOrders");
        return jobTaskOrderRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<JobTaskOrder> findOne(Long id) {
        log.debug("Request to get JobTaskOrder : {}", id);
        return jobTaskOrderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobTaskOrder : {}", id);
        jobTaskOrderRepository.deleteById(id);
    }
}
