package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.service.AppRunnerService;
import io.github.bkosaraju.pipeline.service.dto.AppRunnerDTO;
import io.github.bkosaraju.pipeline.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class AppRunnerResource {

    private final Logger log = LoggerFactory.getLogger(AppRunnerResource.class);

    private final AppRunnerService appRunnerService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AppRunnerResource(AppRunnerService appRunnerService) {
        this.appRunnerService = appRunnerService;
    }

    private static final String ENTITY_NAME = "appRunnerConfig";

    @PostMapping("/apprunner/run")
    public ResponseEntity<Void> runPipelineJob(@RequestBody AppRunnerDTO runnerConfig) throws URISyntaxException {
        log.debug("REST request to Run application : {}", runnerConfig);
        if (runnerConfig.getJobId() == null) {
            throw new BadRequestAlertException("Invalid JobIDid", ENTITY_NAME, "jobIdnull");
        }
        appRunnerService.runPipelineApp(runnerConfig);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, runnerConfig.getJobId().toString()))
            .build();
    }
}
