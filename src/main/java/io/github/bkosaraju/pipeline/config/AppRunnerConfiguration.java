package io.github.bkosaraju.pipeline.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bkosaraju.pipeline.functions.RunJob;



@Configuration
public class AppRunnerConfiguration {

    @Bean
    public RunJob AppRunner() {
        return  new RunJob();
    }

}
