package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.service.JobConfigService;
import io.github.bkosaraju.pipeline.domain.JobConfig;
import io.github.bkosaraju.pipeline.repository.JobConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link JobConfig}.
 */
@Service
@Transactional
public class JobConfigServiceImpl implements JobConfigService {

    private final Logger log = LoggerFactory.getLogger(JobConfigServiceImpl.class);

    private final JobConfigRepository jobConfigRepository;

    public JobConfigServiceImpl(JobConfigRepository jobConfigRepository) {
        this.jobConfigRepository = jobConfigRepository;
    }

    @Override
    public JobConfig save(JobConfig jobConfig) {
        log.debug("Request to save JobConfig : {}", jobConfig);
        return jobConfigRepository.save(jobConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobConfig> findAll(Pageable pageable) {
        log.debug("Request to get all JobConfigs");
        return jobConfigRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<JobConfig> findOne(Long id) {
        log.debug("Request to get JobConfig : {}", id);
        return jobConfigRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobConfig : {}", id);
        jobConfigRepository.deleteById(id);
    }
}
