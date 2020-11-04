package io.github.bkosaraju.pipeline.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private Boolean jobStatusFlag;

    @Column(name = "create_timestamp")
    private ZonedDateTime createTimestamp;

    @OneToMany(mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<JobConfig> jobConfigs = new HashSet<>();

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

    public Boolean isJobStatusFlag() {
        return jobStatusFlag;
    }

    public Job jobStatusFlag(Boolean jobStatusFlag) {
        this.jobStatusFlag = jobStatusFlag;
        return this;
    }

    public void setJobStatusFlag(Boolean jobStatusFlag) {
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

    public Set<JobConfig> getJobConfigs() {
        return jobConfigs;
    }

    public Job jobConfigs(Set<JobConfig> jobConfigs) {
        this.jobConfigs = jobConfigs;
        return this;
    }

    public Job addJobConfig(JobConfig jobConfig) {
        this.jobConfigs.add(jobConfig);
        jobConfig.setJob(this);
        return this;
    }

    public Job removeJobConfig(JobConfig jobConfig) {
        this.jobConfigs.remove(jobConfig);
        jobConfig.setJob(null);
        return this;
    }

    public void setJobConfigs(Set<JobConfig> jobConfigs) {
        this.jobConfigs = jobConfigs;
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
            ", jobStatusFlag='" + isJobStatusFlag() + "'" +
            ", createTimestamp='" + getCreateTimestamp() + "'" +
            "}";
    }
}
