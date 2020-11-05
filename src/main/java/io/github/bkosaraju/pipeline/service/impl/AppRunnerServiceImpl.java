package io.github.bkosaraju.pipeline.service.impl;

import io.github.bkosaraju.pipeline.config.AppMetaConfig;
import io.github.bkosaraju.pipeline.functions.RunJob;
import io.github.bkosaraju.pipeline.service.AppRunnerService;
import io.github.bkosaraju.pipeline.service.dto.AppRunnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class AppRunnerServiceImpl implements AppRunnerService{

    private final RunJob runJob;
    private final AppMetaConfig metaConfig;

    @Autowired
    public AppRunnerServiceImpl(RunJob runJob, AppMetaConfig metaConfig ) {
        this.runJob = runJob;
        this.metaConfig = metaConfig;
    }

    @Override
    public void runPipelineApp(AppRunnerDTO runConfig) {
        scala.collection.mutable.Map<String,String> sConfig = scala.collection.mutable.Map$.MODULE$.<String, String>empty();
        sConfig.put("jdbcDatabase",metaConfig.getJdbcDatabase());
        sConfig.put("jdbcHostname",metaConfig.getJdbcHostname());
        sConfig.put("jdbcPort",metaConfig.getJdbcPort());
        sConfig.put("jdbcUsername",metaConfig.getJdbcUsername());
        sConfig.put("jdbcPassword",metaConfig.getJdbcPassword());
        sConfig.put("jdbcType",metaConfig.getJdbcType());
        sConfig.put("jobId",Long.toString(runConfig.getJobId()));
        sConfig.put("jobOrderTimestamp",DateTimeFormatter.ISO_INSTANT.format(runConfig.getJobOrderTimestamp()));
        sConfig.put("rerunFlag",Boolean.toString(runConfig.getRerunFlag()));
        sConfig.put("endRunFlag",Boolean.toString(runConfig.getEndRunFlag()));
        runJob.runJob(sConfig);
    }
}
