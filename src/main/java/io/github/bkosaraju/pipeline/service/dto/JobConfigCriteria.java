package io.github.bkosaraju.pipeline.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.JobConfig} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.JobConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter configKey;

    private StringFilter configValue;

    private StringFilter configType;

    private LongFilter jobId;

    public JobConfigCriteria() {
    }

    public JobConfigCriteria(JobConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.configKey = other.configKey == null ? null : other.configKey.copy();
        this.configValue = other.configValue == null ? null : other.configValue.copy();
        this.configType = other.configType == null ? null : other.configType.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
    }

    @Override
    public JobConfigCriteria copy() {
        return new JobConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getConfigKey() {
        return configKey;
    }

    public void setConfigKey(StringFilter configKey) {
        this.configKey = configKey;
    }

    public StringFilter getConfigValue() {
        return configValue;
    }

    public void setConfigValue(StringFilter configValue) {
        this.configValue = configValue;
    }

    public StringFilter getConfigType() {
        return configType;
    }

    public void setConfigType(StringFilter configType) {
        this.configType = configType;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobConfigCriteria that = (JobConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(configKey, that.configKey) &&
            Objects.equals(configValue, that.configValue) &&
            Objects.equals(configType, that.configType) &&
            Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        configKey,
        configValue,
        configType,
        jobId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (configKey != null ? "configKey=" + configKey + ", " : "") +
                (configValue != null ? "configValue=" + configValue + ", " : "") +
                (configType != null ? "configType=" + configType + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
            "}";
    }

}
