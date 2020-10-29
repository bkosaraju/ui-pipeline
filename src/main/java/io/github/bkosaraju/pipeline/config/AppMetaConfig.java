package io.github.bkosaraju.pipeline.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppMetaConfig {
    @Value("${pipeline.app.jdbcHostname}")
    private String jdbcHostname;
    @Value("${pipeline.app.jdbcPort}")
    private String jdbcPort;
    @Value("${pipeline.app.jdbcDatabase}")
    private String jdbcDatabase;
    @Value("${pipeline.app.jdbcUsername}")
    private String jdbcUsername;
    @Value("${pipeline.app.jdbcPassword}")
    private String jdbcPassword;
    @Value("${pipeline.app.jdbcType:postgresql}")
    private String jdbcType;
}
