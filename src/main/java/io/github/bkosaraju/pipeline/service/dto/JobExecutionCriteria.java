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
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.JobExecution} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.JobExecutionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-executions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobExecutionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter jobOrderTimestamp;

    private StringFilter jobExecutionStatus;

    private ZonedDateTimeFilter jobExecutionEndTimestamp;

    private ZonedDateTimeFilter jobExecutionStartTimestamp;

    private LongFilter taskExecutionId;

    private LongFilter jobId;

    public JobExecutionCriteria() {
    }

    public JobExecutionCriteria(JobExecutionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.jobOrderTimestamp = other.jobOrderTimestamp == null ? null : other.jobOrderTimestamp.copy();
        this.jobExecutionStatus = other.jobExecutionStatus == null ? null : other.jobExecutionStatus.copy();
        this.jobExecutionEndTimestamp = other.jobExecutionEndTimestamp == null ? null : other.jobExecutionEndTimestamp.copy();
        this.jobExecutionStartTimestamp = other.jobExecutionStartTimestamp == null ? null : other.jobExecutionStartTimestamp.copy();
        this.taskExecutionId = other.taskExecutionId == null ? null : other.taskExecutionId.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
    }

    @Override
    public JobExecutionCriteria copy() {
        return new JobExecutionCriteria(this);
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

    public StringFilter getJobExecutionStatus() {
        return jobExecutionStatus;
    }

    public void setJobExecutionStatus(StringFilter jobExecutionStatus) {
        this.jobExecutionStatus = jobExecutionStatus;
    }

    public ZonedDateTimeFilter getJobExecutionEndTimestamp() {
        return jobExecutionEndTimestamp;
    }

    public void setJobExecutionEndTimestamp(ZonedDateTimeFilter jobExecutionEndTimestamp) {
        this.jobExecutionEndTimestamp = jobExecutionEndTimestamp;
    }

    public ZonedDateTimeFilter getJobExecutionStartTimestamp() {
        return jobExecutionStartTimestamp;
    }

    public void setJobExecutionStartTimestamp(ZonedDateTimeFilter jobExecutionStartTimestamp) {
        this.jobExecutionStartTimestamp = jobExecutionStartTimestamp;
    }

    public LongFilter getTaskExecutionId() {
        return taskExecutionId;
    }

    public void setTaskExecutionId(LongFilter taskExecutionId) {
        this.taskExecutionId = taskExecutionId;
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
        final JobExecutionCriteria that = (JobExecutionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobOrderTimestamp, that.jobOrderTimestamp) &&
            Objects.equals(jobExecutionStatus, that.jobExecutionStatus) &&
            Objects.equals(jobExecutionEndTimestamp, that.jobExecutionEndTimestamp) &&
            Objects.equals(jobExecutionStartTimestamp, that.jobExecutionStartTimestamp) &&
            Objects.equals(taskExecutionId, that.taskExecutionId) &&
            Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobOrderTimestamp,
        jobExecutionStatus,
        jobExecutionEndTimestamp,
        jobExecutionStartTimestamp,
        taskExecutionId,
        jobId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobExecutionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobOrderTimestamp != null ? "jobOrderTimestamp=" + jobOrderTimestamp + ", " : "") +
                (jobExecutionStatus != null ? "jobExecutionStatus=" + jobExecutionStatus + ", " : "") +
                (jobExecutionEndTimestamp != null ? "jobExecutionEndTimestamp=" + jobExecutionEndTimestamp + ", " : "") +
                (jobExecutionStartTimestamp != null ? "jobExecutionStartTimestamp=" + jobExecutionStartTimestamp + ", " : "") +
                (taskExecutionId != null ? "taskExecutionId=" + taskExecutionId + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
            "}";
    }

}
