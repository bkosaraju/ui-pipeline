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
 * Criteria class for the {@link io.github.bkosaraju.pipeline.domain.Job} entity. This class is used
 * in {@link io.github.bkosaraju.pipeline.web.rest.JobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter jobName;

    private IntegerFilter jobStatusFlag;

    private ZonedDateTimeFilter createTimestamp;

    private LongFilter jobConfigId;

    private LongFilter jobTaskOrderId;

    private LongFilter jobExecutionId;

    public JobCriteria() {
    }

    public JobCriteria(JobCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.jobName = other.jobName == null ? null : other.jobName.copy();
        this.jobStatusFlag = other.jobStatusFlag == null ? null : other.jobStatusFlag.copy();
        this.createTimestamp = other.createTimestamp == null ? null : other.createTimestamp.copy();
        this.jobConfigId = other.jobConfigId == null ? null : other.jobConfigId.copy();
        this.jobTaskOrderId = other.jobTaskOrderId == null ? null : other.jobTaskOrderId.copy();
        this.jobExecutionId = other.jobExecutionId == null ? null : other.jobExecutionId.copy();
    }

    @Override
    public JobCriteria copy() {
        return new JobCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getJobName() {
        return jobName;
    }

    public void setJobName(StringFilter jobName) {
        this.jobName = jobName;
    }

    public IntegerFilter getJobStatusFlag() {
        return jobStatusFlag;
    }

    public void setJobStatusFlag(IntegerFilter jobStatusFlag) {
        this.jobStatusFlag = jobStatusFlag;
    }

    public ZonedDateTimeFilter getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(ZonedDateTimeFilter createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public LongFilter getJobConfigId() {
        return jobConfigId;
    }

    public void setJobConfigId(LongFilter jobConfigId) {
        this.jobConfigId = jobConfigId;
    }

    public LongFilter getJobTaskOrderId() {
        return jobTaskOrderId;
    }

    public void setJobTaskOrderId(LongFilter jobTaskOrderId) {
        this.jobTaskOrderId = jobTaskOrderId;
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
        final JobCriteria that = (JobCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobName, that.jobName) &&
            Objects.equals(jobStatusFlag, that.jobStatusFlag) &&
            Objects.equals(createTimestamp, that.createTimestamp) &&
            Objects.equals(jobConfigId, that.jobConfigId) &&
            Objects.equals(jobTaskOrderId, that.jobTaskOrderId) &&
            Objects.equals(jobExecutionId, that.jobExecutionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobName,
        jobStatusFlag,
        createTimestamp,
        jobConfigId,
        jobTaskOrderId,
        jobExecutionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobName != null ? "jobName=" + jobName + ", " : "") +
                (jobStatusFlag != null ? "jobStatusFlag=" + jobStatusFlag + ", " : "") +
                (createTimestamp != null ? "createTimestamp=" + createTimestamp + ", " : "") +
                (jobConfigId != null ? "jobConfigId=" + jobConfigId + ", " : "") +
                (jobTaskOrderId != null ? "jobTaskOrderId=" + jobTaskOrderId + ", " : "") +
                (jobExecutionId != null ? "jobExecutionId=" + jobExecutionId + ", " : "") +
            "}";
    }

}
