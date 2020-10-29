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

    public JobCriteria() {
    }

    public JobCriteria(JobCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.jobName = other.jobName == null ? null : other.jobName.copy();
        this.jobStatusFlag = other.jobStatusFlag == null ? null : other.jobStatusFlag.copy();
        this.createTimestamp = other.createTimestamp == null ? null : other.createTimestamp.copy();
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
            Objects.equals(createTimestamp, that.createTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobName,
        jobStatusFlag,
        createTimestamp
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
            "}";
    }

}
