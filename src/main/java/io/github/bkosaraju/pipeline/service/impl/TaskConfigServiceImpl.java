package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.service.TaskConfigService;
import io.github.bkosaraju.pipeline.domain.TaskConfig;
import io.github.bkosaraju.pipeline.repository.TaskConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TaskConfig}.
 */
@Service
@Transactional
public class TaskConfigServiceImpl implements TaskConfigService {

    private final Logger log = LoggerFactory.getLogger(TaskConfigServiceImpl.class);

    private final TaskConfigRepository taskConfigRepository;

    public TaskConfigServiceImpl(TaskConfigRepository taskConfigRepository) {
        this.taskConfigRepository = taskConfigRepository;
    }

    @Override
    public TaskConfig save(TaskConfig taskConfig) {
        log.debug("Request to save TaskConfig : {}", taskConfig);
        return taskConfigRepository.save(taskConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskConfig> findAll(Pageable pageable) {
        log.debug("Request to get all TaskConfigs");
        return taskConfigRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TaskConfig> findOne(Long id) {
        log.debug("Request to get TaskConfig : {}", id);
        return taskConfigRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskConfig : {}", id);
        taskConfigRepository.deleteById(id);
    }
}
