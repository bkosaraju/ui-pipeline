package io.github.bkosaraju.pipeline.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A TaskExecutionConfig.
 */
@Entity
@Table(name = "task_execution_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaskExecutionConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "config_key")
    private String configKey;

    @Column(name = "config_value")
    private String configValue;

    @Column(name = "config_version")
    private Float configVersion;

    @ManyToOne
    @JsonIgnoreProperties(value = "taskExecutionConfigs", allowSetters = true)
    private TaskExecution taskExecution;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public TaskExecutionConfig configKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public TaskExecutionConfig configValue(String configValue) {
        this.configValue = configValue;
        return this;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Float getConfigVersion() {
        return configVersion;
    }

    public TaskExecutionConfig configVersion(Float configVersion) {
        this.configVersion = configVersion;
        return this;
    }

    public void setConfigVersion(Float configVersion) {
        this.configVersion = configVersion;
    }

    public TaskExecution getTaskExecution() {
        return taskExecution;
    }

    public TaskExecutionConfig taskExecution(TaskExecution taskExecution) {
        this.taskExecution = taskExecution;
        return this;
    }

    public void setTaskExecution(TaskExecution taskExecution) {
        this.taskExecution = taskExecution;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskExecutionConfig)) {
            return false;
        }
        return id != null && id.equals(((TaskExecutionConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskExecutionConfig{" +
            "id=" + getId() +
            ", configKey='" + getConfigKey() + "'" +
            ", configValue='" + getConfigValue() + "'" +
            ", configVersion=" + getConfigVersion() +
            "}";
    }
}
