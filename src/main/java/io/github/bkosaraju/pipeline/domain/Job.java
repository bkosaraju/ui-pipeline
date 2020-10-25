package io.github.bkosaraju.pipeline.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @Column(name = "create_time_stamp")
    private ZonedDateTime createTimeStamp;

    @OneToMany(mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<JobConfig> jobConfigs = new HashSet<>();

    @OneToMany(mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<JobTaskOrder> jobTaskOrders = new HashSet<>();

    @OneToMany(mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<JobExecution> jobExecutions = new HashSet<>();

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

    public ZonedDateTime getCreateTimeStamp() {
        return createTimeStamp;
    }

    public Job createTimeStamp(ZonedDateTime createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
        return this;
    }

    public void setCreateTimeStamp(ZonedDateTime createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
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

    public Set<JobTaskOrder> getJobTaskOrders() {
        return jobTaskOrders;
    }

    public Job jobTaskOrders(Set<JobTaskOrder> jobTaskOrders) {
        this.jobTaskOrders = jobTaskOrders;
        return this;
    }

    public Job addJobTaskOrder(JobTaskOrder jobTaskOrder) {
        this.jobTaskOrders.add(jobTaskOrder);
        jobTaskOrder.setJob(this);
        return this;
    }

    public Job removeJobTaskOrder(JobTaskOrder jobTaskOrder) {
        this.jobTaskOrders.remove(jobTaskOrder);
        jobTaskOrder.setJob(null);
        return this;
    }

    public void setJobTaskOrders(Set<JobTaskOrder> jobTaskOrders) {
        this.jobTaskOrders = jobTaskOrders;
    }

    public Set<JobExecution> getJobExecutions() {
        return jobExecutions;
    }

    public Job jobExecutions(Set<JobExecution> jobExecutions) {
        this.jobExecutions = jobExecutions;
        return this;
    }

    public Job addJobExecution(JobExecution jobExecution) {
        this.jobExecutions.add(jobExecution);
        jobExecution.setJob(this);
        return this;
    }

    public Job removeJobExecution(JobExecution jobExecution) {
        this.jobExecutions.remove(jobExecution);
        jobExecution.setJob(null);
        return this;
    }

    public void setJobExecutions(Set<JobExecution> jobExecutions) {
        this.jobExecutions = jobExecutions;
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
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            "}";
    }
}
