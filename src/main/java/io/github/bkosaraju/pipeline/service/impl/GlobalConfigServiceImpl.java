package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.service.GlobalConfigService;
import io.github.bkosaraju.pipeline.domain.GlobalConfig;
import io.github.bkosaraju.pipeline.repository.GlobalConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link GlobalConfig}.
 */
@Service
@Transactional
public class GlobalConfigServiceImpl implements GlobalConfigService {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigServiceImpl.class);

    private final GlobalConfigRepository globalConfigRepository;

    public GlobalConfigServiceImpl(GlobalConfigRepository globalConfigRepository) {
        this.globalConfigRepository = globalConfigRepository;
    }

    @Override
    public GlobalConfig save(GlobalConfig globalConfig) {
        log.debug("Request to save GlobalConfig : {}", globalConfig);
        return globalConfigRepository.save(globalConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GlobalConfig> findAll(Pageable pageable) {
        log.debug("Request to get all GlobalConfigs");
        return globalConfigRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<GlobalConfig> findOne(Long id) {
        log.debug("Request to get GlobalConfig : {}", id);
        return globalConfigRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GlobalConfig : {}", id);
        globalConfigRepository.deleteById(id);
    }
}
