package io.github.bkosaraju.pipeline.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import io.github.bkosaraju.pipeline.domain.enumeration.ConfigType;

/**
 * A TaskConfig.
 */
@Entity
@Table(name = "task_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaskConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "config_key")
    private String configKey;

    @Column(name = "config_value")
    private String configValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "config_type")
    private ConfigType configType;

    @Column(name = "config_version")
    private Float configVersion;

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

    public TaskConfig configKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public TaskConfig configValue(String configValue) {
        this.configValue = configValue;
        return this;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public TaskConfig configType(ConfigType configType) {
        this.configType = configType;
        return this;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public Float getConfigVersion() {
        return configVersion;
    }

    public TaskConfig configVersion(Float configVersion) {
        this.configVersion = configVersion;
        return this;
    }

    public void setConfigVersion(Float configVersion) {
        this.configVersion = configVersion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskConfig)) {
            return false;
        }
        return id != null && id.equals(((TaskConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskConfig{" +
            "id=" + getId() +
            ", configKey='" + getConfigKey() + "'" +
            ", configValue='" + getConfigValue() + "'" +
            ", configType='" + getConfigType() + "'" +
            ", configVersion=" + getConfigVersion() +
            "}";
    }
}
