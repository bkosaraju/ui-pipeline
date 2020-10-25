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
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.JobTaskOrder} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.JobTaskOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-task-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobTaskOrderCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter taskSeqId;

    private BooleanFilter jobTaskStatusFlag;

    private FloatFilter configVersion;

    private LongFilter jobId;

    private LongFilter taskId;

    public JobTaskOrderCriteria() {}

    public JobTaskOrderCriteria(JobTaskOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taskSeqId = other.taskSeqId == null ? null : other.taskSeqId.copy();
        this.jobTaskStatusFlag = other.jobTaskStatusFlag == null ? null : other.jobTaskStatusFlag.copy();
        this.configVersion = other.configVersion == null ? null : other.configVersion.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
    }

    @Override
    public JobTaskOrderCriteria copy() {
        return new JobTaskOrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getTaskSeqId() {
        return taskSeqId;
    }

    public void setTaskSeqId(IntegerFilter taskSeqId) {
        this.taskSeqId = taskSeqId;
    }

    public BooleanFilter getJobTaskStatusFlag() {
        return jobTaskStatusFlag;
    }

    public void setJobTaskStatusFlag(BooleanFilter jobTaskStatusFlag) {
        this.jobTaskStatusFlag = jobTaskStatusFlag;
    }

    public FloatFilter getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(FloatFilter configVersion) {
        this.configVersion = configVersion;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
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
        final JobTaskOrderCriteria that = (JobTaskOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(taskSeqId, that.taskSeqId) &&
            Objects.equals(jobTaskStatusFlag, that.jobTaskStatusFlag) &&
            Objects.equals(configVersion, that.configVersion) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(taskId, that.taskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskSeqId, jobTaskStatusFlag, configVersion, jobId, taskId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobTaskOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taskSeqId != null ? "taskSeqId=" + taskSeqId + ", " : "") +
                (jobTaskStatusFlag != null ? "jobTaskStatusFlag=" + jobTaskStatusFlag + ", " : "") +
                (configVersion != null ? "configVersion=" + configVersion + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
            "}";
    }
}
