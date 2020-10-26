package io.github.bkosaraju.pipeline.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import io.github.bkosaraju.pipeline.domain.enumeration.TaskType;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "task_name")
    private String taskName;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType;

    @Column(name = "create_time_stamp")
    private ZonedDateTime createTimeStamp;

    @OneToMany(mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TaskConfig> taskConfigs = new HashSet<>();

    @OneToMany(mappedBy = "task")CacheConcurrencyStrategy
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<JobTaskOrder> jobTaskOrders = new HashSet<>();

    @OneToMany(mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TaskExecution> taskExecutions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public Task taskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Task taskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public ZonedDateTime getCreateTimeStamp() {
        return createTimeStamp;
    }

    public Task createTimeStamp(ZonedDateTime createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
        return this;
    }

    public void setCreateTimeStamp(ZonedDateTime createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Set<TaskConfig> getTaskConfigs() {
        return taskConfigs;
    }

    public Task taskConfigs(Set<TaskConfig> taskConfigs) {
        this.taskConfigs = taskConfigs;
        return this;
    }

    public Task addTaskConfig(TaskConfig taskConfig) {
        this.taskConfigs.add(taskConfig);
        taskConfig.setTask(this);
        return this;
    }

    public Task removeTaskConfig(TaskConfig taskConfig) {
        this.taskConfigs.remove(taskConfig);
        taskConfig.setTask(null);
        return this;
    }

    public void setTaskConfigs(Set<TaskConfig> taskConfigs) {
        this.taskConfigs = taskConfigs;
    }

    public Set<JobTaskOrder> getJobTaskOrders() {
        return jobTaskOrders;
    }

    public Task jobTaskOrders(Set<JobTaskOrder> jobTaskOrders) {
        this.jobTaskOrders = jobTaskOrders;
        return this;
    }

    public Task addJobTaskOrder(JobTaskOrder jobTaskOrder) {
        this.jobTaskOrders.add(jobTaskOrder);
        jobTaskOrder.setTask(this);
        return this;
    }

    public Task removeJobTaskOrder(JobTaskOrder jobTaskOrder) {
        this.jobTaskOrders.remove(jobTaskOrder);
        jobTaskOrder.setTask(null);
        return this;
    }

    public void setJobTaskOrders(Set<JobTaskOrder> jobTaskOrders) {
        this.jobTaskOrders = jobTaskOrders;
    }

    public Set<TaskExecution> getTaskExecutions() {
        return taskExecutions;
    }

    public Task taskExecutions(Set<TaskExecution> taskExecutions) {
        this.taskExecutions = taskExecutions;
        return this;
    }

    public Task addTaskExecution(TaskExecution taskExecution) {
        this.taskExecutions.add(taskExecution);
        taskExecution.setTask(this);
        return this;
    }

    public Task removeTaskExecution(TaskExecution taskExecution) {
        this.taskExecutions.remove(taskExecution);
        taskExecution.setTask(null);
        return this;
    }

    public void setTaskExecutions(Set<TaskExecution> taskExecutions) {
        this.taskExecutions = taskExecutions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", taskName='" + getTaskName() + "'" +
            ", taskType='" + getTaskType() + "'" +
            ", createTimeStamp='" + getCreateTimeStamp() + "'" +
            "}";
    }
}
