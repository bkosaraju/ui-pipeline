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

    @Column(name = "create_timestamp")
    private ZonedDateTime createTimestamp;

    @OneToMany(mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TaskConfig> taskConfigs = new HashSet<>();

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

    public ZonedDateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public Task createTimestamp(ZonedDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public void setCreateTimestamp(ZonedDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
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
            ", createTimestamp='" + getCreateTimestamp() + "'" +
            "}";
    }
}
