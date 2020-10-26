package io.github.bkosaraju.pipeline.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.bkosaraju.pipeline.domain.enumeration.TaskType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.Task} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TaskType
     */
    public static class TaskTypeFilter extends Filter<TaskType> {

        public TaskTypeFilter() {
        }

        public TaskTypeFilter(TaskTypeFilter filter) {
            super(filter);
        }

        @Override
        public TaskTypeFilter copy() {
            return new TaskTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter taskName;

    private TaskTypeFilter taskType;

    private ZonedDateTimeFilter createTimeStamp;

    private LongFilter taskConfigId;

    private LongFilter jobTaskOrderId;

    private LongFilter taskExecutionId;

    public TaskCriteria() {
    }

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taskName = other.taskName == null ? null : other.taskName.copy();
        this.taskType = other.taskType == null ? null : other.taskType.copy();
        this.createTimeStamp = other.createTimeStamp == null ? null : other.createTimeStamp.copy();
        this.taskConfigId = other.taskConfigId == null ? null : other.taskConfigId.copy();
        this.jobTaskOrderId = other.jobTaskOrderId == null ? null : other.jobTaskOrderId.copy();
        this.taskExecutionId = other.taskExecutionId == null ? null : other.taskExecutionId.copy();
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTaskName() {
        return taskName;
    }

    public void setTaskName(StringFilter taskName) {
        this.taskName = taskName;
    }

    public TaskTypeFilter getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeFilter taskType) {
        this.taskType = taskType;
    }

    public ZonedDateTimeFilter getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(ZonedDateTimeFilter createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public LongFilter getTaskConfigId() {
        return taskConfigId;
    }

    public void setTaskConfigId(LongFilter taskConfigId) {
        this.taskConfigId = taskConfigId;
    }

    public LongFilter getJobTaskOrderId() {
        return jobTaskOrderId;
    }

    public void setJobTaskOrderId(LongFilter jobTaskOrderId) {
        this.jobTaskOrderId = jobTaskOrderId;
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
        final TaskCriteria that = (TaskCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(taskName, that.taskName) &&
            Objects.equals(taskType, that.taskType) &&
            Objects.equals(createTimeStamp, that.createTimeStamp) &&
            Objects.equals(taskConfigId, that.taskConfigId) &&
            Objects.equals(jobTaskOrderId, that.jobTaskOrderId) &&
            Objects.equals(taskExecutionId, that.taskExecutionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        taskName,
        taskType,
        createTimeStamp,
        taskConfigId,
        jobTaskOrderId,
        taskExecutionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taskName != null ? "taskName=" + taskName + ", " : "") +
                (taskType != null ? "taskType=" + taskType + ", " : "") +
                (createTimeStamp != null ? "createTimeStamp=" + createTimeStamp + ", " : "") +
                (taskConfigId != null ? "taskConfigId=" + taskConfigId + ", " : "") +
                (jobTaskOrderId != null ? "jobTaskOrderId=" + jobTaskOrderId + ", " : "") +
                (taskExecutionId != null ? "taskExecutionId=" + taskExecutionId + ", " : "") +
            "}";
    }

}
