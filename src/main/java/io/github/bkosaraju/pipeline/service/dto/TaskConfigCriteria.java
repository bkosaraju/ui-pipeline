package io.github.bkosaraju.pipeline.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.bkosaraju.pipeline.domain.enumeration.ConfigType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.TaskConfig} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.TaskConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /task-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskConfigCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ConfigType
     */
    public static class ConfigTypeFilter extends Filter<ConfigType> {

        public ConfigTypeFilter() {
        }

        public ConfigTypeFilter(ConfigTypeFilter filter) {
            super(filter);
        }

        @Override
        public ConfigTypeFilter copy() {
            return new ConfigTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter configKey;

    private StringFilter configValue;

    private ConfigTypeFilter configType;

    private FloatFilter configVersion;

    private LongFilter taskId;

    public TaskConfigCriteria() {
    }

    public TaskConfigCriteria(TaskConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.configKey = other.configKey == null ? null : other.configKey.copy();
        this.configValue = other.configValue == null ? null : other.configValue.copy();
        this.configType = other.configType == null ? null : other.configType.copy();
        this.configVersion = other.configVersion == null ? null : other.configVersion.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
    }

    @Override
    public TaskConfigCriteria copy() {
        return new TaskConfigCriteria(this);
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

    public ConfigTypeFilter getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigTypeFilter configType) {
        this.configType = configType;
    }

    public FloatFilter getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(FloatFilter configVersion) {
        this.configVersion = configVersion;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskConfigCriteria that = (TaskConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(configKey, that.configKey) &&
            Objects.equals(configValue, that.configValue) &&
            Objects.equals(configType, that.configType) &&
            Objects.equals(configVersion, that.configVersion) &&
            Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        configKey,
        configValue,
        configType,
        configVersion,
        taskId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (configKey != null ? "configKey=" + configKey + ", " : "") +
                (configValue != null ? "configValue=" + configValue + ", " : "") +
                (configType != null ? "configType=" + configType + ", " : "") +
                (configVersion != null ? "configVersion=" + configVersion + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
            "}";
    }

}
