package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.domain.JobExecution;
import io.github.bkosaraju.pipeline.repository.JobExecutionRepository;
import io.github.bkosaraju.pipeline.service.JobExecutionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link JobExecution}.
 */
@Service
@Transactional
public class JobExecutionServiceImpl implements JobExecutionService {
    private final Logger log = LoggerFactory.getLogger(JobExecutionServiceImpl.class);

    private final JobExecutionRepository jobExecutionRepository;

    public JobExecutionServiceImpl(JobExecutionRepository jobExecutionRepository) {
        this.jobExecutionRepository = jobExecutionRepository;
    }

    @Override
    public JobExecution save(JobExecution jobExecution) {
        log.debug("Request to save JobExecution : {}", jobExecution);
        return jobExecutionRepository.save(jobExecution);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecution> findAll(Pageable pageable) {
        log.debug("Request to get all JobExecutions");
        return jobExecutionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findOne(Long id) {
        log.debug("Request to get JobExecution : {}", id);
        return jobExecutionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobExecution : {}", id);
        jobExecutionRepository.deleteById(id);
    }
}
