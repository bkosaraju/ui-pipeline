package io.github.bkosaraju.pipeline.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.TaskExecutionConfig} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.TaskExecutionConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /task-execution-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskExecutionConfigCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter configKey;

    private StringFilter configValue;

    private StringFilter configVersion;

    private LongFilter taskExecutionId;

    public TaskExecutionConfigCriteria() {}

    public TaskExecutionConfigCriteria(TaskExecutionConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.configKey = other.configKey == null ? null : other.configKey.copy();
        this.configValue = other.configValue == null ? null : other.configValue.copy();
        this.configVersion = other.configVersion == null ? null : other.configVersion.copy();
        this.taskExecutionId = other.taskExecutionId == null ? null : other.taskExecutionId.copy();
    }

    @Override
    public TaskExecutionConfigCriteria copy() {
        return new TaskExecutionConfigCriteria(this);
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

    public StringFilter getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(StringFilter configVersion) {
        this.configVersion = configVersion;
    }

    public LongFilter getTaskExecutionId() {
        return taskExecutionId;
    }

    public void setTaskExecutionId(LongFilter taskExecutionId) {
        this.taskExecutionId = taskExecutionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskExecutionConfigCriteria that = (TaskExecutionConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(configKey, that.configKey) &&
            Objects.equals(configValue, that.configValue) &&
            Objects.equals(configVersion, that.configVersion) &&
            Objects.equals(taskExecutionId, that.taskExecutionId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configKey, configValue, configVersion, taskExecutionId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskExecutionConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (configKey != null ? "configKey=" + configKey + ", " : "") +
                (configValue != null ? "configValue=" + configValue + ", " : "") +
                (configVersion != null ? "configVersion=" + configVersion + ", " : "") +
                (taskExecutionId != null ? "taskExecutionId=" + taskExecutionId + ", " : "") +
            "}";
    }
}
