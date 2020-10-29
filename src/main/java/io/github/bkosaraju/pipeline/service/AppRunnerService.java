package io.github.bkosaraju.pipeline.service;

import io.github.bkosaraju.pipeline.service.dto.AppRunnerDTO;

import java.util.HashMap;

public interface AppRunnerService {
    void runPipelineApp(AppRunnerDTO appRunnerDTO);
}
