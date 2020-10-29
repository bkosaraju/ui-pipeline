package io.github.bkosaraju.pipeline.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_status_flag")
    private Integer jobStatusFlag;

    @Column(name = "create_timestamp")
    private ZonedDateTime createTimestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public Job jobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobStatusFlag() {
        return jobStatusFlag;
    }

    public Job jobStatusFlag(Integer jobStatusFlag) {
        this.jobStatusFlag = jobStatusFlag;
        return this;
    }

    public void setJobStatusFlag(Integer jobStatusFlag) {
        this.jobStatusFlag = jobStatusFlag;
    }

    public ZonedDateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public Job createTimestamp(ZonedDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public void setCreateTimestamp(ZonedDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", jobName='" + getJobName() + "'" +
            ", jobStatusFlag=" + getJobStatusFlag() +
            ", createTimestamp='" + getCreateTimestamp() + "'" +
            "}";
    }
}
