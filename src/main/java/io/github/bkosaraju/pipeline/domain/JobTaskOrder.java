package io.github.bkosaraju.pipeline.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JobTaskOrder.
 */
@Entity
@Table(name = "job_task_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobTaskOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "task_seq_id")
    private Integer taskSeqId;

    @Column(name = "job_task_status_flag")
    private Boolean jobTaskStatusFlag;

    @Column(name = "config_version")
    private Float configVersion;

    @ManyToOne
    @JsonIgnoreProperties(value = "jobTaskOrders", allowSetters = true)
    private Job job;

    @ManyToOne
    @JsonIgnoreProperties(value = "jobTaskOrders", allowSetters = true)
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaskSeqId() {
        return taskSeqId;
    }

    public JobTaskOrder taskSeqId(Integer taskSeqId) {
        this.taskSeqId = taskSeqId;
        return this;
    }

    public void setTaskSeqId(Integer taskSeqId) {
        this.taskSeqId = taskSeqId;
    }

    public Boolean isJobTaskStatusFlag() {
        return jobTaskStatusFlag;
    }

    public JobTaskOrder jobTaskStatusFlag(Boolean jobTaskStatusFlag) {
        this.jobTaskStatusFlag = jobTaskStatusFlag;
        return this;
    }

    public void setJobTaskStatusFlag(Boolean jobTaskStatusFlag) {
        this.jobTaskStatusFlag = jobTaskStatusFlag;
    }

    public Float getConfigVersion() {
        return configVersion;
    }

    public JobTaskOrder configVersion(Float configVersion) {
        this.configVersion = configVersion;
        return this;
    }

    public void setConfigVersion(Float configVersion) {
        this.configVersion = configVersion;
    }

    public Job getJob() {
        return job;
    }

    public JobTaskOrder job(Job job) {
        this.job = job;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Task getTask() {
        return task;
    }

    public JobTaskOrder task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobTaskOrder)) {
            return false;
        }
        return id != null && id.equals(((JobTaskOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobTaskOrder{" +
            "id=" + getId() +
            ", taskSeqId=" + getTaskSeqId() +
            ", jobTaskStatusFlag='" + isJobTaskStatusFlag() + "'" +
            ", configVersion=" + getConfigVersion() +
            "}";
    }
}
