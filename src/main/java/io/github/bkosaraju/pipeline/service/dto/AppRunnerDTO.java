package io.github.bkosaraju.pipeline.service.dto;

import io.github.jhipster.service.Criteria;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class AppRunnerDTO implements Serializable {
    Long jobId;
    ZonedDateTime jobOrderTimestamp;
    Boolean rerunFlag;
    Boolean endRunFlag;

    public AppRunnerDTO(AppRunnerDTO other) {
        this.jobId = other.jobId == null ? null : other.jobId;
        this.jobOrderTimestamp = other.jobOrderTimestamp == null ? null : other.jobOrderTimestamp;
        this.rerunFlag = other.rerunFlag != null && other.rerunFlag;
        this.endRunFlag = other.endRunFlag != null && other.endRunFlag;
    }
}
