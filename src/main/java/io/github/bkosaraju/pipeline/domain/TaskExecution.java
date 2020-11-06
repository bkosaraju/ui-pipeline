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
 * A TaskExecution.
 */
@Entity
@Table(name = "task_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaskExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="task_execution_id_seq")
    @SequenceGenerator(name="task_execution_id_seq", sequenceName="task_execution_id_seq", allocationSize=1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "job_order_timestamp")
    private ZonedDateTime jobOrderTimestamp;

    @Column(name = "task_execution_status")
    private String taskExecutionStatus;

    @Column(name = "task_execution_start_timestamp")
    private ZonedDateTime taskExecutionStartTimestamp;

    @Column(name = "task_execution_end_timestamp")
    private ZonedDateTime taskExecutionEndTimestamp;

    @OneToMany(mappedBy = "taskExecution")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TaskExecutionConfig> taskExecutionConfigs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "taskExecutions", allowSetters = true)
    private Task task;

    @ManyToOne
    @JsonIgnoreProperties(value = "taskExecutions", allowSetters = true)
    private JobExecution jobExecution;

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

    public TaskExecution jobOrderTimestamp(ZonedDateTime jobOrderTimestamp) {
        this.jobOrderTimestamp = jobOrderTimestamp;
        return this;
    }

    public void setJobOrderTimestamp(ZonedDateTime jobOrderTimestamp) {
        this.jobOrderTimestamp = jobOrderTimestamp;
    }

    public String getTaskExecutionStatus() {
        return taskExecutionStatus;
    }

    public TaskExecution taskExecutionStatus(String taskExecutionStatus) {
        this.taskExecutionStatus = taskExecutionStatus;
        return this;
    }

    public void setTaskExecutionStatus(String taskExecutionStatus) {
        this.taskExecutionStatus = taskExecutionStatus;
    }

    public ZonedDateTime getTaskExecutionStartTimestamp() {
        return taskExecutionStartTimestamp;
    }

    public TaskExecution taskExecutionStartTimestamp(ZonedDateTime taskExecutionStartTimestamp) {
        this.taskExecutionStartTimestamp = taskExecutionStartTimestamp;
        return this;
    }

    public void setTaskExecutionStartTimestamp(ZonedDateTime taskExecutionStartTimestamp) {
        this.taskExecutionStartTimestamp = taskExecutionStartTimestamp;
    }

    public ZonedDateTime getTaskExecutionEndTimestamp() {
        return taskExecutionEndTimestamp;
    }

    public TaskExecution taskExecutionEndTimestamp(ZonedDateTime taskExecutionEndTimestamp) {
        this.taskExecutionEndTimestamp = taskExecutionEndTimestamp;
        return this;
    }

    public void setTaskExecutionEndTimestamp(ZonedDateTime taskExecutionEndTimestamp) {
        this.taskExecutionEndTimestamp = taskExecutionEndTimestamp;
    }

    public Set<TaskExecutionConfig> getTaskExecutionConfigs() {
        return taskExecutionConfigs;
    }

    public TaskExecution taskExecutionConfigs(Set<TaskExecutionConfig> taskExecutionConfigs) {
        this.taskExecutionConfigs = taskExecutionConfigs;
        return this;
    }

    public TaskExecution addTaskExecutionConfig(TaskExecutionConfig taskExecutionConfig) {
        this.taskExecutionConfigs.add(taskExecutionConfig);
        taskExecutionConfig.setTaskExecution(this);
        return this;
    }

    public TaskExecution removeTaskExecutionConfig(TaskExecutionConfig taskExecutionConfig) {
        this.taskExecutionConfigs.remove(taskExecutionConfig);
        taskExecutionConfig.setTaskExecution(null);
        return this;
    }

    public void setTaskExecutionConfigs(Set<TaskExecutionConfig> taskExecutionConfigs) {
        this.taskExecutionConfigs = taskExecutionConfigs;
    }

    public Task getTask() {
        return task;
    }

    public TaskExecution task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public JobExecution getJobExecution() {
        return jobExecution;
    }

    public TaskExecution jobExecution(JobExecution jobExecution) {
        this.jobExecution = jobExecution;
        return this;
    }

    public void setJobExecution(JobExecution jobExecution) {
        this.jobExecution = jobExecution;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskExecution)) {
            return false;
        }
        return id != null && id.equals(((TaskExecution) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskExecution{" +
            "id=" + getId() +
            ", jobOrderTimestamp='" + getJobOrderTimestamp() + "'" +
            ", taskExecutionStatus='" + getTaskExecutionStatus() + "'" +
            ", taskExecutionStartTimestamp='" + getTaskExecutionStartTimestamp() + "'" +
            ", taskExecutionEndTimestamp='" + getTaskExecutionEndTimestamp() + "'" +
            "}";
    }
}
