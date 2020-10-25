package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.domain.TaskExecution;
import io.github.bkosaraju.pipeline.repository.TaskExecutionRepository;
import io.github.bkosaraju.pipeline.service.TaskExecutionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaskExecution}.
 */
@Service
@Transactional
public class TaskExecutionServiceImpl implements TaskExecutionService {
    private final Logger log = LoggerFactory.getLogger(TaskExecutionServiceImpl.class);

    private final TaskExecutionRepository taskExecutionRepository;

    public TaskExecutionServiceImpl(TaskExecutionRepository taskExecutionRepository) {
        this.taskExecutionRepository = taskExecutionRepository;
    }

    @Override
    public TaskExecution save(TaskExecution taskExecution) {
        log.debug("Request to save TaskExecution : {}", taskExecution);
        return taskExecutionRepository.save(taskExecution);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskExecution> findAll(Pageable pageable) {
        log.debug("Request to get all TaskExecutions");
        return taskExecutionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskExecution> findOne(Long id) {
        log.debug("Request to get TaskExecution : {}", id);
        return taskExecutionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskExecution : {}", id);
        taskExecutionRepository.deleteById(id);
    }
}
