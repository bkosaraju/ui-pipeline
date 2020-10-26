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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.TaskExecution} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.TaskExecutionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /task-executions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskExecutionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter jobOrderTimestamp;

    private StringFilter taskExecutionStatus;

    private ZonedDateTimeFilter taskExecutionStartTimestamp;

    private ZonedDateTimeFilter taskExecutionEndTimestamp;

    private LongFilter taskExecutionConfigId;

    private LongFilter taskId;

    private LongFilter jobExecutionId;

    public TaskExecutionCriteria() {
    }

    public TaskExecutionCriteria(TaskExecutionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.jobOrderTimestamp = other.jobOrderTimestamp == null ? null : other.jobOrderTimestamp.copy();
        this.taskExecutionStatus = other.taskExecutionStatus == null ? null : other.taskExecutionStatus.copy();
        this.taskExecutionStartTimestamp = other.taskExecutionStartTimestamp == null ? null : other.taskExecutionStartTimestamp.copy();
        this.taskExecutionEndTimestamp = other.taskExecutionEndTimestamp == null ? null : other.taskExecutionEndTimestamp.copy();
        this.taskExecutionConfigId = other.taskExecutionConfigId == null ? null : other.taskExecutionConfigId.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.jobExecutionId = other.jobExecutionId == null ? null : other.jobExecutionId.copy();
    }

    @Override
    public TaskExecutionCriteria copy() {
        return new TaskExecutionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getJobOrderTimestamp() {
        return jobOrderTimestamp;
    }

    public void setJobOrderTimestamp(ZonedDateTimeFilter jobOrderTimestamp) {
        this.jobOrderTimestamp = jobOrderTimestamp;
    }

    public StringFilter getTaskExecutionStatus() {
        return taskExecutionStatus;
    }

    public void setTaskExecutionStatus(StringFilter taskExecutionStatus) {
        this.taskExecutionStatus = taskExecutionStatus;
    }

    public ZonedDateTimeFilter getTaskExecutionStartTimestamp() {
        return taskExecutionStartTimestamp;
    }

    public void setTaskExecutionStartTimestamp(ZonedDateTimeFilter taskExecutionStartTimestamp) {
        this.taskExecutionStartTimestamp = taskExecutionStartTimestamp;
    }

    public ZonedDateTimeFilter getTaskExecutionEndTimestamp() {
        return taskExecutionEndTimestamp;
    }

    public void setTaskExecutionEndTimestamp(ZonedDateTimeFilter taskExecutionEndTimestamp) {
        this.taskExecutionEndTimestamp = taskExecutionEndTimestamp;
    }

    public LongFilter getTaskExecutionConfigId() {
        return taskExecutionConfigId;
    }

    public void setTaskExecutionConfigId(LongFilter taskExecutionConfigId) {
        this.taskExecutionConfigId = taskExecutionConfigId;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(LongFilter jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskExecutionCriteria that = (TaskExecutionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobOrderTimestamp, that.jobOrderTimestamp) &&
            Objects.equals(taskExecutionStatus, that.taskExecutionStatus) &&
            Objects.equals(taskExecutionStartTimestamp, that.taskExecutionStartTimestamp) &&
            Objects.equals(taskExecutionEndTimestamp, that.taskExecutionEndTimestamp) &&
            Objects.equals(taskExecutionConfigId, that.taskExecutionConfigId) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(jobExecutionId, that.jobExecutionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobOrderTimestamp,
        taskExecutionStatus,
        taskExecutionStartTimestamp,
        taskExecutionEndTimestamp,
        taskExecutionConfigId,
        taskId,
        jobExecutionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskExecutionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobOrderTimestamp != null ? "jobOrderTimestamp=" + jobOrderTimestamp + ", " : "") +
                (taskExecutionStatus != null ? "taskExecutionStatus=" + taskExecutionStatus + ", " : "") +
                (taskExecutionStartTimestamp != null ? "taskExecutionStartTimestamp=" + taskExecutionStartTimestamp + ", " : "") +
                (taskExecutionEndTimestamp != null ? "taskExecutionEndTimestamp=" + taskExecutionEndTimestamp + ", " : "") +
                (taskExecutionConfigId != null ? "taskExecutionConfigId=" + taskExecutionConfigId + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
                (jobExecutionId != null ? "jobExecutionId=" + jobExecutionId + ", " : "") +
            "}";
    }

}
