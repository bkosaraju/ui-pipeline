package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.service.TaskExecutionConfigService;
import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;
import io.github.bkosaraju.pipeline.repository.TaskExecutionConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TaskExecutionConfig}.
 */
@Service
@Transactional
public class TaskExecutionConfigServiceImpl implements TaskExecutionConfigService {

    private final Logger log = LoggerFactory.getLogger(TaskExecutionConfigServiceImpl.class);

    private final TaskExecutionConfigRepository taskExecutionConfigRepository;

    public TaskExecutionConfigServiceImpl(TaskExecutionConfigRepository taskExecutionConfigRepository) {
        this.taskExecutionConfigRepository = taskExecutionConfigRepository;
    }

    @Override
    public TaskExecutionConfig save(TaskExecutionConfig taskExecutionConfig) {
        log.debug("Request to save TaskExecutionConfig : {}", taskExecutionConfig);
        return taskExecutionConfigRepository.save(taskExecutionConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskExecutionConfig> findAll(Pageable pageable) {
        log.debug("Request to get all TaskExecutionConfigs");
        return taskExecutionConfigRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TaskExecutionConfig> findOne(Long id) {
        log.debug("Request to get TaskExecutionConfig : {}", id);
        return taskExecutionConfigRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskExecutionConfig : {}", id);
        taskExecutionConfigRepository.deleteById(id);
    }
}
