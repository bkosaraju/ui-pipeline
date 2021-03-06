package io.github.bkosaraju.pipeline.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A JobExecution.
 */
@Entity
@Table(name = "job_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "job_order_timestamp")
    private ZonedDateTime jobOrderTimestamp;

    @Column(name = "job_execution_status")
    private String jobExecutionStatus;

    @Column(name = "job_execution_end_timestamp")
    private ZonedDateTime jobExecutionEndTimestamp;

    @Column(name = "job_execution_start_timestamp")
    private ZonedDateTime jobExecutionStartTimestamp;

    @OneToMany(mappedBy = "jobExecution")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TaskExecution> taskExecutions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "jobExecutions", allowSetters = true)
    private Job job;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getJobOrderTimestamp() {
        return jobOrderTimestamp;
    }

    public JobExecution jobOrderTimestamp(ZonedDateTime jobOrderTimestamp) {
        this.jobOrderTimestamp = jobOrderTimestamp;
        return this;
    }

    public void setJobOrderTimestamp(ZonedDateTime jobOrderTimestamp) {
        this.jobOrderTimestamp = jobOrderTimestamp;
    }

    public String getJobExecutionStatus() {
        return jobExecutionStatus;
    }

    public JobExecution jobExecutionStatus(String jobExecutionStatus) {
        this.jobExecutionStatus = jobExecutionStatus;
        return this;
    }

    public void setJobExecutionStatus(String jobExecutionStatus) {
        this.jobExecutionStatus = jobExecutionStatus;
    }

    public ZonedDateTime getJobExecutionEndTimestamp() {
        return jobExecutionEndTimestamp;
    }

    public JobExecution jobExecutionEndTimestamp(ZonedDateTime jobExecutionEndTimestamp) {
        this.jobExecutionEndTimestamp = jobExecutionEndTimestamp;
        return this;
    }

    public void setJobExecutionEndTimestamp(ZonedDateTime jobExecutionEndTimestamp) {
        this.jobExecutionEndTimestamp = jobExecutionEndTimestamp;
    }

    public ZonedDateTime getJobExecutionStartTimestamp() {
        return jobExecutionStartTimestamp;
    }

    public JobExecution jobExecutionStartTimestamp(ZonedDateTime jobExecutionStartTimestamp) {
        this.jobExecutionStartTimestamp = jobExecutionStartTimestamp;
        return this;
    }

    public void setJobExecutionStartTimestamp(ZonedDateTime jobExecutionStartTimestamp) {
        this.jobExecutionStartTimestamp = jobExecutionStartTimestamp;
    }

    public Set<TaskExecution> getTaskExecutions() {
        return taskExecutions;
    }

    public JobExecution taskExecutions(Set<TaskExecution> taskExecutions) {
        this.taskExecutions = taskExecutions;
        return this;
    }

    public JobExecution addTaskExecution(TaskExecution taskExecution) {
        this.taskExecutions.add(taskExecution);
        taskExecution.setJobExecution(this);
        return this;
    }

    public JobExecution removeTaskExecution(TaskExecution taskExecution) {
        this.taskExecutions.remove(taskExecution);
        taskExecution.setJobExecution(null);
        return this;
    }

    public void setTaskExecutions(Set<TaskExecution> taskExecutions) {
        this.taskExecutions = taskExecutions;
    }

    public Job getJob() {
        return job;
    }

    public JobExecution job(Job job) {
        this.job = job;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobExecution)) {
            return false;
        }
        return id != null && id.equals(((JobExecution) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobExecution{" +
            "id=" + getId() +
            ", jobOrderTimestamp='" + getJobOrderTimestamp() + "'" +
            ", jobExecutionStatus='" + getJobExecutionStatus() + "'" +
            ", jobExecutionEndTimestamp='" + getJobExecutionEndTimestamp() + "'" +
            ", jobExecutionStartTimestamp='" + getJobExecutionStartTimestamp() + "'" +
            "}";
    }
}
