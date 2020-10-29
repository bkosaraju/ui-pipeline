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

    private ZonedDateTimeFilter createTimestamp;

    public TaskCriteria() {
    }

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taskName = other.taskName == null ? null : other.taskName.copy();
        this.taskType = other.taskType == null ? null : other.taskType.copy();
        this.createTimestamp = other.createTimestamp == null ? null : other.createTimestamp.copy();
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

    public ZonedDateTimeFilter getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(ZonedDateTimeFilter createTimestamp) {
        this.createTimestamp = createTimestamp;
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
            Objects.equals(createTimestamp, that.createTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        taskName,
        taskType,
        createTimestamp
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taskName != null ? "taskName=" + taskName + ", " : "") +
                (taskType != null ? "taskType=" + taskType + ", " : "") +
                (createTimestamp != null ? "createTimestamp=" + createTimestamp + ", " : "") +
            "}";
    }

}
